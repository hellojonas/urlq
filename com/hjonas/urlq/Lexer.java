package com.hjonas.urlq;

import java.util.ArrayList;
import java.util.List;

class Lexer {
	private String source;
	private Integer start;
	private Integer current;
	private final List<Token> tokens;

	Lexer(String source) {
		this.source = source;
		this.start = 0;
		this.current = 0;
		this.tokens = new ArrayList<>();
	}

	List<Token> tokens() {
		while (!isAtEnd()) {
			char ch = advance();
			switch (ch) {
				case '[': {
					operator();
					break;
				}
				default: {
					start = current;
				}
			}
		}
		addToken(TokenType.EOF);
		return tokens;
	}

	private void operator() {
		while (!isAtEnd() && peek() != ']') {
			advance();
		}
		if (isAtEnd()) {
			// TODO: report unterminated operator
			return;
		}
		advance();
		String op = source.substring(start, current);
		addToken(Token.KEYWORDS.get(op));
	}

	void addToken(TokenType type) {
		addToken(type, null);
	}

	void addToken(TokenType type, String literal) {
		String lexeme = source.substring(start, current);
		tokens.add(new Token(type, lexeme, literal, start, current));
		start = current;
	}

	char advance() {
		return source.charAt(current++);
	}

	boolean matchAdvance(char ch) {
		if (isAtEnd()) {
			return false;
		}

		if (ch == source.charAt(current)) {
			current++;
			return true;
		}
		return false;
	}

	char peek(int n) {
		if (isAtEnd() || current + n >= source.length()) {
			return '\0';
		}

		return source.charAt(current + n);
	}

	char peek() {
		return peek(0);
	}

	boolean isAtEnd() {
		return current >= source.length();
	}
}
