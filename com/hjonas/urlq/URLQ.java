package com.hjonas.urlq;

import java.util.List;

class URLQ {
	public static void main(String[] args) {
		String equalNotEqual = "age[:]12[and]name[~:]'luis'";
		String containNotContain = "name[#]'la'[and]surname[~#]'ma'";
		String lowerLowerEqual = "age[lt]30[and]age[lte]29";
		String greaterGreaterEqual = "age[gt]10[and]age[gte]11";
		String include = "name[in]'hugo','manuel',surname";
		String notInclude = "((name[~in]'araujo'[and]surname[in]'manuel','mateus')[and](name[#]'sa'))";
		String between = "profile_alias_createdAt[::]$'2024-01-30',$'2024-05-20'";

		String query = equalNotEqual
				+ "[or]" + containNotContain
				+ "[or]" + lowerLowerEqual
				+ "[or]" + greaterGreaterEqual
				+ "[or]" + include
				+ "[or]" + notInclude
				+ "[or]" + between;

		Lexer lexer = new Lexer(query);
		List<Token> tokens  = lexer.tokens();

		for (Token token : tokens) {
			System.out.println(token);
		}
	}
}
