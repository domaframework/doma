package org.seasar.doma.internal.jdbc.command;

/**
 * SQLスクリプトのトークンタイプです。
 *
 * @author taedium
 */
public enum ScriptTokenType {

  /** 引用符に囲まれたトークン */
  QUOTE,

  /** 1行コメントのトークン */
  LINE_COMMENT,

  /** ブロックコメントの始まりを表すトークン */
  START_OF_BLOCK_COMMENT,

  /** ブロックコメントのトークン */
  BLOCK_COMMENT,

  /** ブロックコメントの終わりを表すトークン */
  END_OF_BLOCK_COMMENT,

  /** SQLステートメントの区切りを表すトークン */
  STATEMENT_DELIMITER,

  /** SQLブロックの区切りを表すトークン */
  BLOCK_DELIMITER,

  /** SQL内の単語を表すトークン */
  WORD,

  /** 空白など単語以外を表すトークン */
  OTHER,

  /** 一行の終わりを表すトークン */
  END_OF_LINE,

  /** ファイルの終わりを表すトークン */
  END_OF_FILE
}
