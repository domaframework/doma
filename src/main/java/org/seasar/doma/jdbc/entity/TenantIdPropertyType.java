package org.seasar.doma.jdbc.entity;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;

/**
 * A description for an tenant id property.
 *
 * @param <ENTITY> the entity type
 * @param <BASIC> the property basic type
 * @param <CONTAINER> the property container type
 */
public class TenantIdPropertyType<ENTITY, BASIC, CONTAINER>
    extends DefaultPropertyType<ENTITY, BASIC, CONTAINER> {

  public TenantIdPropertyType(
      Class<ENTITY> entityClass,
      Supplier<Scalar<BASIC, CONTAINER>> scalarSupplier,
      String name,
      String columnName,
      NamingType namingType,
      boolean quoteRequired) {
    super(entityClass, scalarSupplier, name, columnName, namingType, true, true, quoteRequired);
  }

  @Override
  public boolean isTenantId() {
    return true;
  }
}
