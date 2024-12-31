package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import org.seasar.doma.internal.util.StringUtil;

public enum SqlTokenType {
  QUOTE,

  OPENED_PARENS,

  CLOSED_PARENS,

  LINE_COMMENT,

  BLOCK_COMMENT,

  PARSER_LEVEL_BLOCK_COMMENT,

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

  DISTINCT_WORD,

  IN_WORD,

  OTHER,

  WHITESPACE,

  EOL,

  EOF;

  public String extract(String token) {
    return token;
  }
}
