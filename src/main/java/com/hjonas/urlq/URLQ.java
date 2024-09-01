package com.hjonas.urlq;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.hjonas.urlq.ast.Expr;
import com.hjonas.urlq.ast.Lexer;
import com.hjonas.urlq.ast.Parser;
import com.hjonas.urlq.ast.Token;
import com.hjonas.urlq.criteria.PredicateBuilder;

public class URLQ {
	public static <T> Predicate predicate(Root<T> root, CriteriaBuilder cb, String query) {
		Lexer lexer = new Lexer(query);
		List<Token> tokens = lexer.scan();

		Parser parser = new Parser(tokens);
		Expr ast = parser.parse();

		PredicateBuilder<T> builder = new PredicateBuilder<T>(root, cb);

		return builder.toPredicate(ast);
	}
}
