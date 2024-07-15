package org.seasar.doma.it;

import org.seasar.doma.jdbc.SqlParserConfig;

public class LineCommentRemovalSqlParserConfig implements SqlParserConfig {

  @Override
  public boolean shouldRemoveBlockComment(String comment) {
    return false;
  }

  @Override
  public boolean shouldRemoveLineComment(String comment) {
    return true;
  }
}
