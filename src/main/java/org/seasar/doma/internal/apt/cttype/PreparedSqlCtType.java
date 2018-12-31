package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.jdbc.PreparedSql;

public class PreparedSqlCtType extends AbstractCtType {

  public PreparedSqlCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public static PreparedSqlCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    if (!ctx.getTypes().isSameType(type, PreparedSql.class)) {
      return null;
    }
    return new PreparedSqlCtType(type, ctx);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitPreparedSqlCtType(this, p);
  }
}
