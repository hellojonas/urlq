package com.hjonas.urlq;

import java.util.HashMap;

enum TokenType {
	AND,
	OR,

	COMMA,
	L_PAREN,
	R_PAREN,

	EQUAL,
	NOT_EQUAL,
	CONTAIN,
	NOT_CONTAIN,
	LESS,
	LESS_EQUAL,
	GREATER,
	GREATER_EQUAL,
	INCLUDE,
	NOT_INCLUDE,
	BETWEEN,

	PATH,
	STRING,
	NUMBER,
	TRUE,
	FALSE,
	DATE,
	GROUP,

	EOF
}
