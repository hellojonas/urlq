package dev.hellojonas;


public enum Operator {
    
    EQUAL(":"),
    NOT_EQUAL("[~]"),
    IN("[in]"),
    NOT_IN("[~in]"),
    CONTAINING("[#]"),
    NOT_CONTAINING("[~#]"),
    LESS_THAN("[lt]"),
    LESS_THAN_OR_EQUAL("[lte]"),
    GREATER_THAN("[gt]"),
    GREATER_THAN_OR_EQUAL("[gte]"),
    BETWEEN("[:]");

    private String op;


    Operator(String op) {
        this.op = op;
    }

    @Override
    public String toString() {
        return op;
    }
}
