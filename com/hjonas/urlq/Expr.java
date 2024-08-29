package com.hjonas.urlq;

import java.util.List;

abstract class Expr {

	abstract <R> R accept(Visitor<R> visitor);

	static interface Visitor<R> {
		R visitLiteral(Literal expr);

		R visitBinary(Binary expr);

		R visitGroup(Group expr);

		R visitPath(Path expr);

		R visitArray(Array expr);
	}

	static class Literal extends Expr {
		final Object value;

		Literal(Object value) {
			this.value = value;
		}

		@Override
		<R> R accept(Expr.Visitor<R> visitor) {
			return visitor.visitLiteral(this);
		}
	}

	static class Binary extends Expr {
		final Token operator;
		final Expr left;
		final Expr right;

		Binary(Token operator, Expr left, Expr right) {
			this.operator = operator;
			this.left = left;
			this.right = right;
		}

		@Override
		<R> R accept(Expr.Visitor<R> visitor) {
			return visitor.visitBinary(this);
		}
	}

	static class Group extends Expr {
		final Expr expr;

		Group(Expr expr) {
			this.expr = expr;
		}

		@Override
		<R> R accept(Expr.Visitor<R> visitor) {
			return visitor.visitGroup(this);
		}
	}

	static class Path extends Expr {
		final List<Token> identifiers;

		Path(List<Token> identifiers) {
			this.identifiers = identifiers;
		}

		@Override
		<R> R accept(Expr.Visitor<R> visitor) {
			return visitor.visitPath(this);
		}
	}

	static class Array extends Expr {
		final List<Expr> elements;

		Array(List<Expr> elements) {
			this.elements = elements;
		}

		@Override
		<R> R accept(Expr.Visitor<R> visitor) {
			return visitor.visitArray(this);
		}
	}
}
