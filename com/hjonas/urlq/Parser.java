package com.hjonas.urlq;

import java.util.ArrayList;
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
	}

	Expr expression() {
		Expr left = path();

		if (left == null) {
			// TODO: handle this case
		}

		Token operator = peek();

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
				return new Expr.Binary(operator, left, primary());
			}
			case INCLUDE:
			case NOT_INCLUDE:
			case BETWEEN: {
				advance();
				return new Expr.Binary(operator, left, array());
			}
			default: {
				// TODO: report error: expected operator
			}
		}
	}

	Expr path() {
		List<Token> identifiers = new ArrayList<>();
		if (match(TokenType.IDENTIFIER)) {
			identifiers.add(advance());
		}
		while (match(TokenType.UNDERSCORE)) {
			advance();
			if (match(TokenType.IDENTIFIER)) {
				identifiers.add(advance());
				continue;
			}
			// TODO: report error expected identifier
		}

		if (identifiers.isEmpty()) {
			// TODO: report error expected identifier
		}

		return new Expr.Path(identifiers);
	}

	Expr array() {
		List<Expr> elements = new ArrayList<>();
		Expr element = primary();

		while (match(TokenType.COMMA)) {
			advance();
			elements.add(primary());
		}

		return new Expr.Array(elements);
	}

	Expr primary() {
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
