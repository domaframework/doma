package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.mirror.ClobFactoryMirror;

/** @author taedium */
public class ClobCreateQueryMeta extends AbstractCreateQueryMeta {

  protected ClobFactoryMirror clobFactoryMirror;

  public ClobCreateQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  ClobFactoryMirror getClobFactoryMirror() {
    return clobFactoryMirror;
  }

  void setClobFactoryMirror(ClobFactoryMirror clobFactoryMirror) {
    this.clobFactoryMirror = clobFactoryMirror;
  }
}
