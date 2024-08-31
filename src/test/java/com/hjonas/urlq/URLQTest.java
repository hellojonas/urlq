package com.hjonas.urlq;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.hjonas.urlq.ast.AstPrinter;
import com.hjonas.urlq.ast.Expr;




public class URLQTest {

    // TODO: write better tests
    public void printAst() {
        String equalNotEqual = "age[:]12[and]name[~:]\"luis\"";
        String containNotContain = "name[#]\"la\"[and]surname[~#]\"ma\"";
        String lowerLowerEqual = "age[lt]30[and]age[lte]29.0";
        String greaterGreaterEqual = "age[gt]10[and]age[gte]11";
        String include = "name[in]\"hugo\",\"manuel\",surname";
        String notInclude = "((name[~in]\"araujo\"[and]surname[in]\"manuel\",\"mateus\")[and]name[#]\"sa\")";
        String inactiveVerified = "inactive[:]false[and]verified[:]true";
        String between = "profile_alias_createdAt [::] $\"2024-01-30T00:00:00\", $\"2024-05-20T00:00:00\"";

        String query = equalNotEqual
                + "[or]" + containNotContain
                + "[or]" + lowerLowerEqual
                + "[or]" + greaterGreaterEqual
                + "[or]" + include
                + "[or]" + notInclude
                + "[or]" + inactiveVerified
                + "[or]" + between;

        Expr ast = URLQ.parse(query);

        AstPrinter printer = new AstPrinter();
        printer.print(ast);
    }
}
