package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.message.Message;

public abstract class AbstractCreateQueryMetaFactory<M extends AbstractCreateQueryMeta>
    extends AbstractQueryMetaFactory<M> {

  private final Class<?> returnClass;

  AbstractCreateQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement, Class<?> returnClass) {
    super(ctx, daoElement, methodElement);
    assertNotNull(returnClass);
    this.returnClass = returnClass;
  }

  @Override
  protected void doReturnType(M queryMeta) {
    QueryReturnMeta resultMeta = createReturnMeta(queryMeta);
    queryMeta.setReturnMeta(resultMeta);
    if (!returnClass.getName().equals(resultMeta.getCtType().getQualifiedName())) {
      throw new AptException(Message.DOMA4097, methodElement, new Object[] {returnClass.getName()});
    }
  }

  @Override
  protected void doParameters(M queryMeta) {
    List<? extends VariableElement> params = methodElement.getParameters();
    int size = params.size();
    if (size != 0) {
      throw new AptException(Message.DOMA4078, methodElement, new Object[] {});
    }
  }
}
