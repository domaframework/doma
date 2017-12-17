package org.seasar.doma.internal.expr.util;

/**
 * @author taedium
 * 
 */
public final class ExpressionUtil {

    protected static final char STRING_LITERAL_QUOTE = '"';

    protected static final char CHAR_LITERAL_QUOTE = '\'';

    protected static final char FUNCTION_OPERATOR = '@';

    public static boolean isExpressionIdentifierStart(char c) {
        return Character.isJavaIdentifierStart(c) || Character.isWhitespace(c)
                || c == STRING_LITERAL_QUOTE || c == CHAR_LITERAL_QUOTE || c == FUNCTION_OPERATOR;
    }

}
