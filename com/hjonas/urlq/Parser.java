package com.hjonas.urlq;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Parser {
	final List<Token> tokens;
	private int current;

	Parser(List<Token> tokens) {
		this.tokens = tokens;
		this.current = 0;
	}

	Expr parse() {
		return logicOr();
	}

	Expr logicOr() {
		Expr expr = logicAnd();

		while (match(TokenType.OR)) {
			Token operator = advance();
			expr = new Expr.Binary(operator, expr, logicAnd());
		}

		return expr;
	}

	Expr logicAnd() {
		Expr expr = expression();

		while (match(TokenType.AND)) {
			Token operator = advance();
			expr = new Expr.Binary(operator, expr, expression());
		}

		return expr;
	}

	Expr expression() {
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

	Expr group() {
		Expr expr = logicOr();

		if (!match(TokenType.R_PAREN)) {
			throw new RuntimeException("expected ')' after group.");
		}

		advance();
		return new Expr.Group(expr);
	}

	Expr path() {
		List<Token> identifiers = new ArrayList<>();
		if (!match(TokenType.IDENTIFIER)) {
			// TODO: report proper error
			throw new RuntimeException("expected identfier");
		}

		identifiers.add(advance());

		while (match(TokenType.UNDERSCORE)) {
			advance();
			if (match(TokenType.IDENTIFIER)) {
				identifiers.add(advance());
				continue;
			}
			// TODO: report error expected identifier
		}

		return new Expr.Path(identifiers);
	}

	Expr array() {
		List<Expr> elements = new ArrayList<>();
		elements.add(primary());

		while (match(TokenType.COMMA)) {
			advance();
			elements.add(primary());
		}

		return new Expr.Array(elements);
	}

	Expr primary() {
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

		throw new RuntimeException("expected literal after operator.");
	}

	boolean isAtEnd() {
		return tokens.get(current).type.equals(TokenType.EOF);
	}

	Token peek() {
		return tokens.get(current);
	}

	Token advance() {
		return tokens.get(current++);
	}

	boolean match(TokenType type) {
		if (isAtEnd()) {
			return false;
		}
		return tokens.get(current).type.equals(type);
	}
}
