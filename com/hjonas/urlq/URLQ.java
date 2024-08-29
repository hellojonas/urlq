package com.hjonas.urlq;

import java.util.List;

class URLQ {
	public static void main(String[] args) {
		String equalNotEqual = "age[:]12[and]name[~:]\"luis\"";
		String containNotContain = "name[#]\"la\"[and]surname[~#]\"ma\"";
		String lowerLowerEqual = "age[lt]30[and]age[lte]29.0";
		String greaterGreaterEqual = "age[gt]10[and]age[gte]11";
		String include = "name[in]\"hugo\",\"manuel\",surname";
		String notInclude = "((name[~in]\"araujo\"[and]surname[in]\"manuel\",\"mateus\")[and]name[#]\"sa\"))";
		String inactiveVerified = "inactive[:]false[and]verified[:]true";
		String between = "profile_alias_createdAt[::]$\"2024-01-30\",$\"2024-05-20\"";

		String query = equalNotEqual
				+ "[or]" + containNotContain
				+ "[or]" + lowerLowerEqual
				+ "[or]" + greaterGreaterEqual
				+ "[or]" + include
				+ "[or]" + notInclude
				+ "[or]" + inactiveVerified
				+ "[or]" + between;

		query = "(name[:]jose[or]surname[:]pedro)[and]age[lt]18";

		Lexer lexer = new Lexer(query);
		List<Token> tokens = lexer.tokens();

		Parser parser = new Parser(tokens);
		Expr queryAst = parser.parse();

		AstPrinter printer = new AstPrinter();
		printer.print(queryAst);
	}
}
