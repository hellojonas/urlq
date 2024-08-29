package com.hjonas.urlq;

import java.util.stream.Collectors;

class AstPrinter {

	void print(Expr expr) {
		System.out.println(println(expr, 0));
	}

	private String indent(int level) {
		String indent = "";
		for (int i = 0; i < level; i++) {
			indent += " ";
		}
		return indent;
	}

	private String println(Expr expr, int level) {
		String indent = indent(level);
		int nextLevel = level + 4;

		if (expr instanceof Expr.Group) {
			Expr.Group e = (Expr.Group) expr;
			return indent + "group:\n"
					+ println(e.expr, nextLevel) + "\n";
		}

		if (expr instanceof Expr.Literal) {
			Expr.Literal e = (Expr.Literal) expr;
			return indent + e.value.toString() + "\n";
		}

		if (expr instanceof Expr.Binary) {
			Expr.Binary e = (Expr.Binary) expr;
			return indent + e.operator.lexeme + ":\n"
					+ println(e.left, nextLevel)
					+ println(e.right, nextLevel) + "\n";
		}

		if (expr instanceof Expr.Path) {
			Expr.Path e = (Expr.Path) expr;
			return indent + String.join(", ", e.identifiers.stream().map(i -> i.lexeme).collect(Collectors.toList()))
					+ "\n";
		}

		if (expr instanceof Expr.Array) {
			Expr.Array e = (Expr.Array) expr;
			return "[" + indent
					+ String.join(",",
							e.elements.stream().map(el -> println(el, nextLevel)).collect(Collectors.toList()))
					+ "\n";
		}

		return "";
	}

	private String printInline(Expr expr, int level) {
		String indent = indent(level);
		int nextLevel = level + 4;

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
			return indent + e.operator.lexeme + " "
					+ println(e.left, nextLevel)
					+ println(e.right, nextLevel);
		}

		if (expr instanceof Expr.Path) {
			Expr.Path e = (Expr.Path) expr;
			return indent + String.join(", ", e.identifiers.stream().map(i -> i.lexeme).collect(Collectors.toList()));
		}

		if (expr instanceof Expr.Array) {
			Expr.Array e = (Expr.Array) expr;
			return "[" + indent
					+ String.join(",",
							e.elements.stream().map(el -> println(el, nextLevel)).collect(Collectors.toList()));
		}

		return "";
	}
}
