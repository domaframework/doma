/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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

    MOD_OPERATOR,

    METHOD_OPERATOR,

    FIELD_OPERATOR,

    COMMA_OPERATOR,

    NEW_OPERATOR,

    EQ_OPERATOR,

    NE_OPERATOR,

    GT_OPERATOR,

    LT_OPERATOR,

    GE_OPERATOR,

    LE_OPERATOR,

    FUNCTION_OPERATOR,

    STATIC_METHOD_OPERATOR,

    STATIC_FIELD_OPERATOR,

    OTHER,

    EOE;

}
