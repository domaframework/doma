package org.seasar.doma.internal.jdbc.command;

public enum ScriptTokenType {
  QUOTE,

  LINE_COMMENT,

  START_OF_BLOCK_COMMENT,

  BLOCK_COMMENT,

  END_OF_BLOCK_COMMENT,

  STATEMENT_DELIMITER,

  BLOCK_DELIMITER,

  WORD,

  OTHER,

  END_OF_LINE,

  END_OF_FILE
}
