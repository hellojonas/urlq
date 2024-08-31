package com.hjonas.urlq;

import java.util.List;

import com.hjonas.urlq.ast.Expr;
import com.hjonas.urlq.ast.Lexer;
import com.hjonas.urlq.ast.Parser;
import com.hjonas.urlq.ast.Token;


public class URLQ {
	public static Expr parse(String query) {
		Lexer lexer = new Lexer(query);
		List<Token> tokens = lexer.scan();

		Parser parser = new Parser(tokens);
		Expr ast = parser.parse(); //

		return ast;
	}
}
