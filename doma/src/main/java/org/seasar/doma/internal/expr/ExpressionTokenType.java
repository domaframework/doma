package org.seasar.doma.internal.expr;

/**
 * @author taedium
 * 
 */
public enum ExpressionTokenType {

    WHITESPACE,

    OPENED_PARENS,

    CLOSED_PARENS,

    VARIABLE,

    CHAR_LITERAL,

    STRING_LITERAL,

    INT_LITERAL,

    FLOAT_LITERAL,

    DOUBLE_LITERAL,

    LONG_LITERAL,

    BIGDECIMAL_LITERAL,

    ILLEGAL_NUMBER_LITERAL,

    NULL_LITERAL,

    TRUE_LITERAL,

    FALSE_LITERAL,

    NOT_OPERATOR,

    AND_OPERATOR,

    OR_OPERATOR,

    ADD_OPERATOR,

    SUBTRACT_OPERATOR,

    MULTIPLY_OPERATOR,

    DIVIDE_OPERATOR,

    METHOD_OPERATOR,

    NO_PARAM_METHOD_OPERATOR,

    COMMA_OPERATOR,

    NEW_OPERATOR,

    EQ_OPERATOR,

    NE_OPERATOR,

    GT_OPERATOR,

    LT_OPERATOR,

    GE_OPERATOR,

    LE_OPERATOR,

    OTHER,

    EOE;

}
