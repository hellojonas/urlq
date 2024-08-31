package com.hjonas.urlq;

import java.util.stream.Collectors;

public class AstPrinter {

	void print(Expr expr) {
		System.out.println(println(expr, 0));
	}

	private String indent(int level, int inc) {
		String indent = "";
		for (int i = 0; i < level; i++) {
			if (i % inc == 0) {
				indent += "|";
				continue;
			}
			indent += " ";
		}
		return indent;
	}

	private String println(Expr expr, int level) {
		int inc = 4;
		String indent = indent(level, inc);
		int nextLevel = level + inc;

		if (expr instanceof Expr.Group) {
			Expr.Group e = (Expr.Group) expr;
			return indent + "group:\n"
					+ println(e.expr, nextLevel);
		}

		if (expr instanceof Expr.Literal) {
			Expr.Literal e = (Expr.Literal) expr;
			return indent + e.value.toString() + "\n";
		}

		if (expr instanceof Expr.Binary) {
			Expr.Binary e = (Expr.Binary) expr;
			return indent + e.operator.lexeme + ":\n"
					+ println(e.left, nextLevel)
					+ println(e.right, nextLevel);
		}

		if (expr instanceof Expr.Path) {
			Expr.Path e = (Expr.Path) expr;
			return indent + String.join("_", e.identifiers.stream().map(i -> i.lexeme).collect(Collectors.toList()))
					+ "\n";
		}

		if (expr instanceof Expr.Array) {
			Expr.Array e = (Expr.Array) expr;
			return indent + "["
					+ String.join(",",
							e.elements.stream().map(el -> printInline(el, nextLevel)).collect(Collectors.toList()))
					+ "]\n";
		}

		return "";
	}

	private String printInline(Expr expr, int level) {
		int inc = 4;
		String indent = indent(level, 4);
		int nextLevel = level + inc;

		if (expr instanceof Expr.Literal) {
			Expr.Literal e = (Expr.Literal) expr;
			return e.value.toString();
		}

		if (expr instanceof Expr.Path) {
			Expr.Path e = (Expr.Path) expr;
			return String.join("_", e.identifiers.stream().map(i -> i.lexeme).collect(Collectors.toList()));
		}

		if (expr instanceof Expr.Array) {
			Expr.Array e = (Expr.Array) expr;
			return indent + "["
					+ String.join(",",
							e.elements.stream().map(el -> printInline(el, nextLevel)).collect(Collectors.toList()));
		}

		return "";
	}
}
