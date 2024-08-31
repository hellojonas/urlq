package com.hjonas.urlq.ast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser {
	private final List<Token> tokens;
	private int current;

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
		this.current = 0;
	}

	public Expr parse() {
		return logicOr();
	}

	private Expr logicOr() {
		Expr expr = logicAnd();

		while (match(TokenType.OR)) {
			Token operator = advance();
			expr = new Expr.Binary(operator, expr, logicAnd());
		}

		return expr;
	}

	private Expr logicAnd() {
		Expr expr = expression();

		while (match(TokenType.AND)) {
			Token operator = advance();
			expr = new Expr.Binary(operator, expr, expression());
		}

		return expr;
	}

	private Expr expression() {
		if (match(TokenType.L_PAREN)) {
			advance();
			return group();
		}

		Expr left = path();
		Token operator = peek();
		Expr expr = null;

		switch (operator.type) {
			case EQUAL:
			case NOT_EQUAL:
			case CONTAIN:
			case NOT_CONTAIN:
			case LESS:
			case LESS_EQUAL:
			case GREATER:
			case GREATER_EQUAL: {
				advance();
				expr = new Expr.Binary(operator, left, primary());
				break;
			}
			case INCLUDE:
			case NOT_INCLUDE:
			case BETWEEN: {
				advance();
				expr = new Expr.Binary(operator, left, array());
				break;
			}
		}

		return expr;
	}

	private Expr group() {
		Expr expr = logicOr();

		if (!match(TokenType.R_PAREN)) {
			throw new ParserError(error("expected ')' after group.", prev()));
		}

		advance();
		return new Expr.Group(expr);
	}

	private Expr path() {
		List<Token> identifiers = new ArrayList<>();
		if (!match(TokenType.IDENTIFIER)) {
			throw new ParserError(error("expected identifier.", prev()));
		}

		identifiers.add(advance());

		while (match(TokenType.UNDERSCORE)) {
			advance();
			if (match(TokenType.IDENTIFIER)) {
				identifiers.add(advance());
				continue;
			}
			throw new ParserError(error("Invalid path, expected identifier after '_'.", prev()));
		}

		return new Expr.Path(identifiers);
	}

	private Expr array() {
		List<Expr> elements = new ArrayList<>();
		elements.add(primary());

		while (match(TokenType.COMMA)) {
			advance();
			elements.add(primary());
		}

		return new Expr.Array(elements);
	}

	private Expr primary() {
		Token token = peek();

		switch (token.type) {
			case STRING: {
				advance();
				return new Expr.Literal((String) token.literal);
			}
			case NUMBER: {
				advance();
				return new Expr.Literal((Double) token.literal);
			}
			case DATE: {
				advance();
				return new Expr.Literal((Date) token.literal);
			}
			case IDENTIFIER: {
				return path();
			}
			case TRUE: {
				advance();
				return new Expr.Literal(true);
			}
			case FALSE: {
				advance();
				return new Expr.Literal(false);
			}
		}

		throw new ParserError(error("Expected literal.", prev()));
	}

	private boolean isAtEnd() {
		return tokens.get(current).type.equals(TokenType.EOF);
	}

	private Token peek() {
		return tokens.get(current);
	}

	private Token advance() {
		return tokens.get(current++);
	}

	private boolean match(TokenType type) {
		if (isAtEnd()) {
			return false;
		}
		return tokens.get(current).type.equals(type);
	}

	private Token prev() {
		if (current == 0) {
			return null;
		}
		return tokens.get(current - 1);
	}

	private String error(String message, Token prev) {
		String err = "";
		if (prev == null) {
			err = message + "\n" + "col [" + 0 + "]";
		}

		err = message + "\n"
				+ "col [" + prev.end + "]: near '" + prev.lexeme + "'";

		return err;
	}
}
