package org.seasar.doma.jdbc.entity;

import java.util.function.Supplier;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * A description for an identity property whose value is assigned by an application.
 *
 * @param <PARENT> the parent entity type
 * @param <ENTITY> the entity type
 * @param <BASIC> the property basic type
 * @param <DOMAIN> the property domain type
 */
public class AssignedIdPropertyType<PARENT, ENTITY extends PARENT, BASIC, DOMAIN>
    extends DefaultPropertyType<PARENT, ENTITY, BASIC, DOMAIN> {

  public AssignedIdPropertyType(
      Class<ENTITY> entityClass,
      Class<?> entityPropertyClass,
      Class<BASIC> basicClass,
      Supplier<Wrapper<BASIC>> wrapperSupplier,
      EntityPropertyType<PARENT, BASIC> parentEntityPropertyType,
      DomainType<BASIC, DOMAIN> domainType,
      String name,
      String columnName,
      NamingType namingType,
      boolean quoteRequired) {
    super(
        entityClass,
        entityPropertyClass,
        basicClass,
        wrapperSupplier,
        parentEntityPropertyType,
        domainType,
        name,
        columnName,
        namingType,
        true,
        true,
        quoteRequired);
  }

  @Override
  public boolean isId() {
    return true;
  }
}
