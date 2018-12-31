package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.jdbc.Config;

public class ConfigCtType extends AbstractCtType {

  public ConfigCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public static ConfigCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    if (!ctx.getTypes().isSameType(type, Config.class)) {
      return null;
    }
    return new ConfigCtType(type, ctx);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitConfigCtType(this, p);
  }
}
