package org.seasar.doma.jdbc.criteria.query;

import java.util.function.Consumer;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;

public interface CriteriaBuilder {

  void concat(PreparedSqlBuilder buf, Runnable leftOperand, Runnable rightOperand);

  void offsetAndFetch(PreparedSqlBuilder buf, int offset, int limit);

  void lockWithTableHint(
      PreparedSqlBuilder buf, ForUpdateOption option, Consumer<PropertyMetamodel<?>> column);

  void forUpdate(
      PreparedSqlBuilder buf,
      ForUpdateOption option,
      Consumer<PropertyMetamodel<?>> column,
      AliasManager aliasManager);
}
