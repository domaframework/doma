package org.seasar.doma.jdbc.entity;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;

/**
 * A description for an identity property whose value is assigned by an application.
 *
 * @param <ENTITY> the entity type
 * @param <BASIC> the basic type
 * @param <CONTAINER> the container type
 */
public class AssignedIdPropertyDesc<ENTITY, BASIC, CONTAINER>
    extends DefaultPropertyDesc<ENTITY, BASIC, CONTAINER> {

  /**
   * Creates an instance.
   *
   * @param entityClass the entity class
   * @param scalarSupplier the supplier of the scalar value
   * @param name the qualified name of the property
   * @param columnName the column name
   * @param namingType the naming convention
   * @param quoteRequired whether the column name requires quotation marks
   */
  public AssignedIdPropertyDesc(
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
