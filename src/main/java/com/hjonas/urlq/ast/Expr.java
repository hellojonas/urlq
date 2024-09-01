package com.hjonas.urlq.ast;

import java.util.List;

public abstract class Expr {

	public abstract <R> R accept(Visitor<R> visitor);

	public static interface Visitor<R> {
		R visitLiteral(Literal expr);

		R visitBinary(Binary expr);

		R visitGroup(Group expr);

		R visitPath(Path expr);

		R visitArray(Array expr);
	}

	public static class Literal extends Expr {
		public final Object value;

		Literal(Object value) {
			this.value = value;
		}

		@Override
		public <R> R accept(Expr.Visitor<R> visitor) {
			return visitor.visitLiteral(this);
		}
	}

	public static class Binary extends Expr {
		public final Token operator;
		public final Expr left;
		public final Expr right;

		Binary(Token operator, Expr left, Expr right) {
			this.operator = operator;
			this.left = left;
			this.right = right;
		}

		@Override
		public <R> R accept(Expr.Visitor<R> visitor) {
			return visitor.visitBinary(this);
		}
	}

	public static class Group extends Expr {
		public final Expr expr;

		Group(Expr expr) {
			this.expr = expr;
		}

		@Override
		public <R> R accept(Expr.Visitor<R> visitor) {
			return visitor.visitGroup(this);
		}
	}

	public static class Path extends Expr {
		public final List<Token> identifiers;

		Path(List<Token> identifiers) {
			this.identifiers = identifiers;
		}

		@Override
		public <R> R accept(Expr.Visitor<R> visitor) {
			return visitor.visitPath(this);
		}
	}

	public static class Array extends Expr {
		public final List<Expr> elements;

		Array(List<Expr> elements) {
			this.elements = elements;
		}

		@Override
		public <R> R accept(Expr.Visitor<R> visitor) {
			return visitor.visitArray(this);
		}
	}
}
