package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AbstractAnnot;
import org.seasar.doma.internal.apt.expr.ExpressionValidator;
import org.seasar.doma.internal.apt.sql.SqlFileLoader;
import org.seasar.doma.internal.apt.sql.SqlStringLoader;
import org.seasar.doma.internal.apt.sql.SqlTemplate;
import org.seasar.doma.internal.apt.sql.SqlTemplateLoader;
import org.seasar.doma.internal.apt.sql.SqlValidator;
import org.seasar.doma.message.Message;

public abstract class AbstractSqlTemplateQueryMetaFactory<M extends AbstractSqlTemplateQueryMeta>
    extends AbstractQueryMetaFactory<M> {

  protected AbstractSqlTemplateQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  protected boolean usesSqlFile() {
    return sqlAnnot != null && sqlAnnot.getUseFileValue();
  }

  @Override
  protected void doAnnotation(M queryMeta, AbstractAnnot targetAnnot) {
    if (sqlAnnot == null) {
      return;
    }
    if (!sqlAnnot.getValueValue().isEmpty() && sqlAnnot.getUseFileValue()) {
      throw new AptException(Message.DOMA4441, methodElement, sqlAnnot.getAnnotationMirror());
    }
  }

  protected void doSqlTemplate(M queryMeta) {
    if (!ctx.getOptions().getSqlValidation()) {
      return;
    }
    var loader = createSqlTemplateLoader();
    loader.execute(
        queryMeta,
        (sqlResource) -> {
          var parameterTypeMap = queryMeta.getBindableParameterTypeMap();
          var expressionValidator = new ExpressionValidator(ctx, parameterTypeMap);
          var sqlValidator =
              createSqlValidator(
                  sqlResource,
                  expressionValidator,
                  queryMeta.isExpandable(),
                  queryMeta.isPopulatable());
          sqlValidator.validate();
        });
  }

  private SqlTemplateLoader createSqlTemplateLoader() {
    if (sqlAnnot == null || sqlAnnot.getUseFileValue()) {
      return new SqlFileLoader(ctx, methodElement);
    }
    return new SqlStringLoader(ctx, methodElement, sqlAnnot);
  }

  protected SqlValidator createSqlValidator(
      SqlTemplate sqlTemplate,
      ExpressionValidator expressionValidator,
      boolean expandable,
      boolean populatable) {
    return new SqlValidator(
        ctx, methodElement, sqlTemplate, expressionValidator, expandable, populatable);
  }
}
