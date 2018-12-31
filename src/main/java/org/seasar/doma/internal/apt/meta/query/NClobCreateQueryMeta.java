package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.mirror.NClobFactoryMirror;

public class NClobCreateQueryMeta extends AbstractCreateQueryMeta {

  protected NClobFactoryMirror nClobFactoryMirror;

  public NClobCreateQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  NClobFactoryMirror getNClobFactoryMirror() {
    return nClobFactoryMirror;
  }

  void setNClobFactoryMirror(NClobFactoryMirror nClobFactoryMirror) {
    this.nClobFactoryMirror = nClobFactoryMirror;
  }
}
