package com.hjonas.urlq.ast;

class LexerError extends RuntimeException {

	LexerError(String msg) {
		super(msg, null, false, false);
	}
}
