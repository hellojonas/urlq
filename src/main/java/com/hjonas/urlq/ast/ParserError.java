package com.hjonas.urlq.ast;

class ParserError extends RuntimeException {

	ParserError(String msg) {
		super(msg, null, false, false);
	}
}
