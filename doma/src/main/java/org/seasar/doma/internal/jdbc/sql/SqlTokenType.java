/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
