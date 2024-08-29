package com.hjonas.urlq;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
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
				case ' ':
				case '\t':
				case '\n': {
					start = current;
					continue;
				}
				case '[': {
					operator();
					break;
				}
				case '(': {
					addToken(TokenType.L_PAREN);
					break;
				}
				case ')': {
					addToken(TokenType.R_PAREN);
					break;
				}
				case ',': {
					addToken(TokenType.COMMA);
					break;
				}
				case '_': {
					addToken(TokenType.UNDERSCORE);
					break;
				}
				case '$': {
					if (peek() == '"') {
						advance();
						date();
					}
					break;
				}
				case '"': {
					string();
					break;
				}
				default: {
					if (isDigit(ch)) {
						number();
					} else if (isAlpha(ch)) {
						identifier();
					}
					start = current;
				}
			}
		}
		addToken(TokenType.EOF);
		return tokens;
	}

	private void date() {
		while (!isAtEnd() && peek() != '"') {
			advance();
		}
		if (isAtEnd()) {
			// TODO: report unterminated date
			return;
		}
		advance();
		String strDate = source.substring(start + 2, current - 1);

		try {
			addToken(TokenType.DATE,
					Date.from(LocalDateTime.parse(strDate).atZone(ZoneId.systemDefault()).toInstant()));
		} catch (DateTimeParseException e) {
			// TODO: report error
		}
	}

	private void string() {
		while (!isAtEnd() && peek() != '"') {
			advance();
		}
		if (isAtEnd()) {
			// TODO: report unterminated string
			return;
		}
		advance();
		addToken(TokenType.STRING, source.substring(start + 1, current - 1));
	}

	private void identifier() {
		while (isAlphaNum(peek())) {
			advance();
		}

		String str = source.substring(start, current);
		TokenType tType = Token.KEYWORDS.get(str);

		if (tType != null) {
			addToken(tType);
			return;
		}

		addToken(TokenType.IDENTIFIER, str);
	}

	private void number() {
		while (isDigit(peek())) {
			advance();
		}
		if (peek() == '.' && isDigit(peek(1))) {
			advance();
			while (isDigit(peek())) {
				advance();
			}
		}

		addToken(TokenType.NUMBER, Double.valueOf(source.substring(start, current)));
	}

	private boolean isAlpha(char ch) {
		return ch >= 'a' && ch <= 'z'
				|| ch >= 'A' && ch <= 'Z';
	}

	private boolean isDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

	private boolean isAlphaNum(char ch) {
		return isAlpha(ch) || isDigit(ch);
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

	void addToken(TokenType type, Object literal) {
		String lexeme = source.substring(start, current);
		tokens.add(new Token(type, lexeme, literal, start, current));
		start = current;
	}

	char advance() {
		return source.charAt(current++);
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
