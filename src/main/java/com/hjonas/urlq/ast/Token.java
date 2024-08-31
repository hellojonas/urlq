package com.hjonas.urlq.ast;

import java.util.HashMap;
import java.util.Map;

public class Token {
	final TokenType type;
	final String lexeme;
	final Object literal;
	final int start;
	final int end;

	Token(TokenType type, String lexeme, Object literal, int start, int end) {
		this.type = type;
		this.lexeme = lexeme;
		this.literal = literal;
		this.start = start;
		this.end = end;
	}

	@Override
	public String toString() {
		return String.format("{type: %s, lexeme: %s, literal: %s, start: %d, end: %d}",
				type.name(),
				lexeme,
				literal,
				start,
				end);
	}

	final static Map<String, TokenType> KEYWORDS = new HashMap<>() {
		{
			put("[:]", TokenType.EQUAL);
			put("[~:]", TokenType.NOT_EQUAL);
			put("[#]", TokenType.CONTAIN);
			put("[~#]", TokenType.NOT_CONTAIN);
			put("[lt]", TokenType.LESS);
			put("[lte]", TokenType.LESS_EQUAL);
			put("[gt]", TokenType.GREATER);
			put("[gte]", TokenType.GREATER_EQUAL);
			put("[in]", TokenType.INCLUDE);
			put("[~in]", TokenType.NOT_INCLUDE);
			put("[::]", TokenType.BETWEEN);
			put("[and]", TokenType.AND);
			put("[or]", TokenType.OR);
			put("true", TokenType.TRUE);
			put("false", TokenType.FALSE);
		}
	};
}
