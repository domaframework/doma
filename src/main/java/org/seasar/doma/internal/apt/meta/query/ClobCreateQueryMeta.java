package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.reflection.ClobFactoryReflection;

public class ClobCreateQueryMeta extends AbstractCreateQueryMeta {

  private ClobFactoryReflection clobFactoryReflection;

  public ClobCreateQueryMeta(ExecutableElement method) {
    super(method);
  }

  public ClobFactoryReflection getClobFactoryReflection() {
    return clobFactoryReflection;
  }

  public void setClobFactoryReflection(ClobFactoryReflection clobFactoryReflection) {
    this.clobFactoryReflection = clobFactoryReflection;
  }
}
