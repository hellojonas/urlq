package com.hjonas.urlq.criteria;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.hjonas.urlq.ast.Expr;
import com.hjonas.urlq.ast.Token;

public class Builder<T> {
	private CriteriaBuilder cb;
	private Root<T> root;

	public Builder(CriteriaBuilder cb, Root<T> root) {
		this.cb = cb;
		this.root = root;
	}

	public Predicate toPredicate(Expr expr) {
		return (Predicate) evaluate(expr);
	}

	@SuppressWarnings("unchecked")
	private Object evaluate(Expr expr) {
		if (expr instanceof Expr.Literal) {
			Expr.Literal e = (Expr.Literal) expr;
			return e.value;
		}

		if (expr instanceof Expr.Group) {
			Expr.Group e = (Expr.Group) expr;
			return evaluate(e.expr);
		}

		if (expr instanceof Expr.Binary) {
			Expr.Binary e = (Expr.Binary) expr;

			switch (e.operator.type) {
				case AND: {
					return cb.and(
							(Predicate) evaluate(e.left),
							(Predicate) evaluate(e.right));
				}
				case OR: {
					return cb.or(
							(Predicate) evaluate(e.left),
							(Predicate) evaluate(e.right));
				}
				case EQUAL: {
					Expression<?> left = path((Expr.Path) e.left);
					Object value = evaluate(e.right);
					return cb.equal(left, value);
				}
				case NOT_EQUAL: {
					Expression<?> left = path((Expr.Path) e.left);
					Object value = evaluate(e.right);
					return cb.notEqual(left, value);
				}
				case CONTAIN: {
					Expression<?> left = path((Expr.Path) e.left);
					Object value = evaluate(e.right);
					return cb.like((Expression<String>) left, cb.literal("%" + value + "%"));

				}
				case NOT_CONTAIN: {
					Expression<?> left = path((Expr.Path) e.left);
					Object value = evaluate(e.right);
					return cb.notLike((Expression<String>) left, cb.literal("%" + value + "%"));
				}
				case INCLUDE: {
					Expression<?> left = path((Expr.Path) e.left);
					List<Object> values = array((Expr.Array) e.right);
					return left.in(values);
				}
				case NOT_INCLUDE: {
					Expression<?> left = path((Expr.Path) e.left);
					List<Object> values = array((Expr.Array) e.right);
					return left.in(values);
				}
				case LESS:
				case LESS_EQUAL:
				case GREATER:
				case GREATER_EQUAL: {
					return comparison(e.operator, (Expr.Path) e.left, e.right);
				}
				case BETWEEN: {
					List<Object> values = array((Expr.Array) e.right);

					if (values.size() != 2) {
						// TODO: throw proper error
						// TODO: throw proper error
						// TODO: throw proper error
						throw new RuntimeException("betwee operator expects an array of 2 elements");
					}

					return between((Expr.Path) e.left, values.get(0), values.get(1));
				}
			}
		}

		if (expr instanceof Expr.Path) {
			return path((Expr.Path) expr);
		}

		if (expr instanceof Expr.Array) {
			return array((Expr.Array) expr);
		}

		throw new RuntimeException("Expected expression");
	}

	private Path<Object> path(Expr.Path expr) {
		if (expr.identifiers.isEmpty()) {
			return null;
		}

		Path<Object> p = root.get(expr.identifiers.get(0).lexeme);

		for (int i = 1; i < expr.identifiers.size(); i++) {
			p = root.get(expr.identifiers.get(i).lexeme);
		}

		return p;
	}

	private List<Object> array(Expr.Array expr) {
		return expr.elements.stream()
				.map(el -> evaluate(el))
				.collect(Collectors.toList());
	}

	private Object comparison(Token operator, Expr.Path left, Expr right) {

		String methdoName = null;

		switch (operator.type) {
			case LESS: {
				methdoName = "lessThan";
				break;
			}
			case LESS_EQUAL: {
				methdoName = "lessThanOrEqualTo";
				break;
			}
			case GREATER: {
				methdoName = "greaterThan";
				break;
			}
			case GREATER_EQUAL: {
				methdoName = "greaterThanOrEqualTo";
				break;
			}
		}

		Object value = evaluate(right);
		Class<?> valType = value.getClass();

		Method asMethod;
		try {
			asMethod = Path.class.getMethod("as", Class.class);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}

		Path<?> path = path((Expr.Path) left);
		Expression<?> pathExpression = (Expression<?>) asMethod.invoke(path, valType);

		Method compMethod;
		try {
			compMethod = CriteriaBuilder.class.getMethod(methdoName, Expression.class, Expression.class);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}

		return compMethod.invoke(cb, pathExpression, cb.literal(value).as(valType));
	}

	private Object between(Expr.Path left, Object start, Object end) {
		Class<?> valType = start.getClass();

		Method asMethod;
		try {
			asMethod = Path.class.getMethod("as", Class.class);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}

		Path<?> path = path(left);
		Expression<?> pathExpression = (Expression<?>) asMethod.invoke(path, valType);

		Method compMethod;
		try {
			compMethod = CriteriaBuilder.class.getMethod("between", Expression.class, Expression.class,
					Expression.class);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}

		return compMethod.invoke(cb, pathExpression, cb.literal(start).as(valType), cb.literal(end).as(valType));
	}
}
