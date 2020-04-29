package org.seasar.doma.jdbc.criteria.declaration;

import java.util.function.Function;
import java.util.function.Supplier;
import org.seasar.doma.def.PropertyDef;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.scalar.Scalars;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.criteria.context.Operand;

public class DeclarationSupport {

  public <PROPERTY> Operand.Prop toProp(final PropertyDef<PROPERTY> propertyDef) {
    return new Operand.Prop(propertyDef);
  }

  public <PROPERTY> Operand.Param toParam(
      final PropertyDef<PROPERTY> propertyDef, final PROPERTY value) {
    Function<ClassHelper, InParameter<?>> parameterProvider =
        classHelper -> {
          Class<?> clazz = propertyDef.asClass();
          Supplier<Scalar<?, ?>> supplier = Scalars.wrap(value, clazz, false, classHelper);
          return new ScalarInParameter<>(supplier.get());
        };
    return new Operand.Param(parameterProvider);
  }
}
