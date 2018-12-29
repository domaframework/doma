package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.Config;

public class ConfigCtType extends AbstractCtType {

  public ConfigCtType(TypeMirror type, ProcessingEnvironment env) {
    super(type, env);
  }

  public static ConfigCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    if (!TypeMirrorUtil.isSameType(type, Config.class, env)) {
      return null;
    }
    return new ConfigCtType(type, env);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitConfigCtType(this, p);
  }
}
