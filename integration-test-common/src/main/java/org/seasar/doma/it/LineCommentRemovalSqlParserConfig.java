package org.seasar.doma.it;

import org.seasar.doma.jdbc.SqlParserConfig;

public class LineCommentRemovalSqlParserConfig implements SqlParserConfig {

  @Override
  public boolean shouldRemoveBlockComments() {
    return false;
  }

  @Override
  public boolean shouldRemoveLineComments() {
    return true;
  }
}
