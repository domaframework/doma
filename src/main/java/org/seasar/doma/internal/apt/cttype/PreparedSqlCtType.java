package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.PreparedSql;

public class PreparedSqlCtType extends AbstractCtType {

  public PreparedSqlCtType(TypeMirror type, ProcessingEnvironment env) {
    super(type, env);
  }

  public static PreparedSqlCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    if (!TypeMirrorUtil.isSameType(type, PreparedSql.class, env)) {
      return null;
    }
    return new PreparedSqlCtType(type, env);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitPreparedSqlCtType(this, p);
  }
}
