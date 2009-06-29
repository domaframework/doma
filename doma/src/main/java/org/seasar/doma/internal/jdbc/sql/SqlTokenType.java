package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public enum SqlTokenType {

    QUOTE,

    OPENED_PARENS,

    CLOSED_PARENS,

    LINE_COMMENT,

    ELSEIF_LINE_COMMENT {

        @Override
        public String extractExpression(String token) {
            assertNotNull(token);
            assertTrue(token.length() >= 10);
            return token.substring(8, token.length() - 2);
        }

    },

    ELSE_LINE_COMMENT,

    BLOCK_COMMENT,

    BIND_BLOCK_COMMENT {

        @Override
        public String extractExpression(String token) {
            assertNotNull(token);
            assertTrue(token.length() >= 5);
            return token.substring(2, token.length() - 2);
        }
    },

    IF_BLOCK_COMMENT {

        @Override
        public String extractExpression(String token) {
            assertNotNull(token);
            assertTrue(token.length() >= 7);
            return token.substring(5, token.length() - 2);
        }

    },

    END_BLOCK_COMMENT,

    DELIMITER,

    SELECT_WORD,

    WHERE_WORD,

    FROM_WORD,

    GROUP_BY_WORD,

    HAVING_WORD,

    ORDER_BY_WORD,

    FOR_UPDATE_WORD,

    AND_WORD,

    OR_WORD,

    WORD,

    OTHER,

    EOL,

    EOF;

    public String extractExpression(String token) {
        return token;
    }

}
