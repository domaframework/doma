package org.seasar.doma.it;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.dialect.SqliteDialect;
import org.seasar.doma.wrapper.LocalDateWrapper;

public class CustomSqliteLogFormattingVisitor extends SqliteDialect.SqliteSqlLogFormattingVisitor {

  @Override
  public String visitLocalDateWrapper(LocalDateWrapper wrapper, SqlLogFormattingFunction p, Void q)
      throws RuntimeException {
    LocalDate date = wrapper.get();
    if (date == null) {
      return "null";
    }
    return "'" + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00:00.000'";
  }
}
