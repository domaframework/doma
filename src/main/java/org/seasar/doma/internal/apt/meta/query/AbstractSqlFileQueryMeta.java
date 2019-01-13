package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;

public abstract class AbstractSqlFileQueryMeta extends AbstractQueryMeta {

  protected SqlAnnot sqlAnnot;

  protected AbstractSqlFileQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  public SqlAnnot getSqlAnnot() {
    return sqlAnnot;
  }

  public void setSqlAnnot(SqlAnnot sqlAnnot) {
    this.sqlAnnot = sqlAnnot;
  }

  public String getPath() {
    if (sqlAnnot == null) {
      // sql file path
      return SqlFileUtil.buildPath(getDaoElement().getQualifiedName().toString(), getName());
    }
    return buildQualifiedMethodName();
  }

  protected String buildQualifiedMethodName() {
    return String.format("%s#%s", getDaoElement().getQualifiedName(), getName());
  }
}
