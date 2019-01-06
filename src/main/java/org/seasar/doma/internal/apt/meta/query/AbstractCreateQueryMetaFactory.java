package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.message.Message;

public abstract class AbstractCreateQueryMetaFactory<M extends AbstractCreateQueryMeta>
    extends AbstractQueryMetaFactory<M> {

  private final Class<?> returnClass;

  protected AbstractCreateQueryMetaFactory(Context ctx, Class<?> returnClass) {
    super(ctx);
    assertNotNull(returnClass);
    this.returnClass = returnClass;
  }

  @Override
  protected void doReturnType(M queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    QueryReturnMeta resultMeta = createReturnMeta(queryMeta);
    queryMeta.setReturnMeta(resultMeta);
    if (!returnClass.getName().equals(resultMeta.getCtType().getQualifiedName())) {
      throw new AptException(Message.DOMA4097, method, new Object[] {returnClass.getName()});
    }
  }

  @Override
  protected void doParameters(M queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    List<? extends VariableElement> params = method.getParameters();
    int size = params.size();
    if (size != 0) {
      throw new AptException(Message.DOMA4078, method, new Object[] {});
    }
  }
}
