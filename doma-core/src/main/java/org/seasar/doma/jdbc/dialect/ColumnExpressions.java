package org.seasar.doma.jdbc.dialect;

public interface ColumnExpressions {
  enum ConcatKind {
    FUNCTION,
    PIPES,
    PLUS;
  }

  ConcatKind getConcatKind();
}
