package org.seasar.doma.jdbc.entity;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;

/**
 * A description for an identity property whose value is assigned by an application.
 *
 * @param <ENTITY> the entity type
 * @param <BASIC> the property basic type
 * @param <CONTAINER> the property container type
 */
public class AssignedIdPropertyType<ENTITY, BASIC, CONTAINER>
    extends DefaultPropertyType<ENTITY, BASIC, CONTAINER> {

  public AssignedIdPropertyType(
      Class<ENTITY> entityClass,
      Supplier<Scalar<BASIC, CONTAINER>> scalarSupplier,
      String name,
      String columnName,
      NamingType namingType,
      boolean quoteRequired) {
    super(entityClass, scalarSupplier, name, columnName, namingType, true, true, quoteRequired);
  }

  @Override
  public boolean isId() {
    return true;
  }
}
