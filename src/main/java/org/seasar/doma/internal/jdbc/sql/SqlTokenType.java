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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import org.seasar.doma.internal.util.StringUtil;

/** @author taedium */
public enum SqlTokenType {
  QUOTE,

  OPENED_PARENS,

  CLOSED_PARENS,

  LINE_COMMENT,

  BLOCK_COMMENT,

  BIND_VARIABLE_BLOCK_COMMENT {

    @Override
    public String extract(String token) {
      assertNotNull(token);
      assertTrue(token.length() >= 5);
      String s = token.substring(2, token.length() - 2);
      return StringUtil.trimWhitespace(s);
    }
  },

  LITERAL_VARIABLE_BLOCK_COMMENT {

    @Override
    public String extract(String token) {
      assertNotNull(token);
      assertTrue(token.length() >= 5);
      String s = token.substring(3, token.length() - 2);
      return StringUtil.trimWhitespace(s);
    }
  },

  EMBEDDED_VARIABLE_BLOCK_COMMENT {

    @Override
    public String extract(String token) {
      assertNotNull(token);
      assertTrue(token.length() >= 5);
      String s = token.substring(3, token.length() - 2);
      return StringUtil.trimWhitespace(s);
    }
  },

  IF_BLOCK_COMMENT {

    @Override
    public String extract(String token) {
      assertNotNull(token);
      assertTrue(token.length() >= 7);
      return token.substring(5, token.length() - 2);
    }
  },

  ELSEIF_BLOCK_COMMENT {

    @Override
    public String extract(String token) {
      assertNotNull(token);
      assertTrue(token.length() >= 11);
      return token.substring(9, token.length() - 2);
    }
  },

  ELSE_BLOCK_COMMENT,

  FOR_BLOCK_COMMENT {

    @Override
    public String extract(String token) {
      assertNotNull(token);
      assertTrue(token.length() >= 8);
      return token.substring(6, token.length() - 2);
    }
  },

  EXPAND_BLOCK_COMMENT {

    @Override
    public String extract(String token) {
      assertNotNull(token);
      assertTrue(token.length() >= 11);
      return token.substring(9, token.length() - 2);
    }
  },

  POPULATE_BLOCK_COMMENT {

    @Override
    public String extract(String token) {
      assertNotNull(token);
      assertTrue(token.length() >= 13);
      return token.substring(11, token.length() - 2);
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

  OPTION_WORD,

  AND_WORD,

  OR_WORD,

  UNION_WORD,

  MINUS_WORD,

  EXCEPT_WORD,

  INTERSECT_WORD,

  UPDATE_WORD,

  SET_WORD,

  WORD,

  OTHER,

  WHITESPACE,

  EOL,

  EOF;

  public String extract(String token) {
    return token;
  }
}
