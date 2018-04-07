package org.seasar.doma.internal.apt.sql;

import java.util.function.Consumer;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.apt.meta.query.AbstractSqlTemplateQueryMeta;

public class SqlStringLoader extends SqlTemplateLoader {

  private final SqlAnnot sqlAnnot;

  private final String sqlFilePath = null;

  public SqlStringLoader(Context ctx, ExecutableElement methodElement, SqlAnnot sqlAnnot) {
    super(ctx, methodElement, sqlAnnot.getAnnotationMirror(), sqlAnnot.getValue());
    this.sqlAnnot = sqlAnnot;
  }

  @Override
  public void execute(AbstractSqlTemplateQueryMeta queryMeta, Consumer<SqlTemplate> consumer) {
    var sql = sqlAnnot.getValueValue();
    validateSql(sql, sqlFilePath);
    var sqlNode = createSqlNode(sql, sqlFilePath);
    var sqlTemplate =
        new SqlTemplate(
            ctx, methodElement, annotationMirror, annotationValue, sqlFilePath, sqlNode);
    consumer.accept(sqlTemplate);
  }
}
