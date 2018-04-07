package org.seasar.doma.internal.apt.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Consumer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.query.AbstractSqlTemplateQueryMeta;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

public abstract class SqlTemplateLoader {

  protected final Context ctx;

  protected final ExecutableElement methodElement;

  protected final AnnotationMirror annotationMirror;

  protected final AnnotationValue annotationValue;

  protected SqlTemplateLoader(
      Context ctx,
      ExecutableElement methodElement,
      AnnotationMirror annotationMirror,
      AnnotationValue annotationValue) {
    assertNotNull(ctx, methodElement);
    this.ctx = ctx;
    this.methodElement = methodElement;
    this.annotationMirror = annotationMirror;
    this.annotationValue = annotationValue;
  }

  public abstract void execute(
      AbstractSqlTemplateQueryMeta queryMeta, Consumer<SqlTemplate> consumer);

  protected void validateSql(String sql, String sqlFilePath) {
    if (sql.isEmpty() || StringUtil.isWhitespace(sql)) {
      throw new AptException(
          Message.DOMA4020,
          methodElement,
          annotationMirror,
          annotationValue,
          new Object[] {sqlFilePath});
    }
  }

  protected SqlNode createSqlNode(String sql, String sqlFilePath) {
    try {
      var sqlParser = new SqlParser(sql);
      return sqlParser.parse();
    } catch (JdbcException e) {
      throw new AptException(
          Message.DOMA4069,
          methodElement,
          annotationMirror,
          annotationValue,
          e,
          new Object[] {sqlFilePath, e});
    }
  }
}
