package com.hjonas.urlq;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ParserError extends RuntimeException {

	ParserError(String msg) {
		super(msg, null, false, false);
	}
}
