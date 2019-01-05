package org.seasar.doma.internal.apt.meta.query;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.meta.parameter.CallableSqlParameterMeta;

public abstract class AutoModuleQueryMeta extends AbstractQueryMeta {

  private final List<CallableSqlParameterMeta> sqlParameterMetas = new ArrayList<>();

  protected AutoModuleQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  public void addCallableSqlParameterMeta(CallableSqlParameterMeta sqlParameterMeta) {
    sqlParameterMetas.add(sqlParameterMeta);
  }

  public List<CallableSqlParameterMeta> getCallableSqlParameterMetas() {
    return sqlParameterMetas;
  }

  public abstract MapKeyNamingType getMapKeyNamingType();
}
