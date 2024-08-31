package com.hjonas.urlq;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class LexerError extends RuntimeException {

	LexerError(String msg) {
		super(msg, null, false, false);
	}
}
