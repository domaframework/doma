package org.seasar.doma.jdbc.entity;

import java.util.function.Supplier;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.NumberWrapper;
import org.seasar.doma.wrapper.NumberWrapperVisitor;
import org.seasar.doma.wrapper.Wrapper;

/**
 * A description for a version property.
 *
 * @param <PARENT> the parent entity type
 * @param <ENTITY> the entity type
 * @param <BASIC> the property basic type
 * @param <DOMAIN> the property domain type
 */
public class VersionPropertyType<PARENT, ENTITY extends PARENT, BASIC extends Number, DOMAIN>
    extends DefaultPropertyType<PARENT, ENTITY, BASIC, DOMAIN> {

  public VersionPropertyType(
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
  public boolean isVersion() {
    return true;
  }

  public ENTITY setIfNecessary(EntityType<ENTITY> entityType, ENTITY entity, Number value) {
    return modifyIfNecessary(entityType, entity, new ValueSetter(), value);
  }

  public ENTITY increment(EntityType<ENTITY> entityType, ENTITY entity) {
    return modifyIfNecessary(entityType, entity, new Incrementer(), null);
  }

  protected static class ValueSetter
      implements NumberWrapperVisitor<Boolean, Number, Void, RuntimeException> {

    @Override
    public <V extends Number> Boolean visitNumberWrapper(
        NumberWrapper<V> wrapper, Number value, Void q) {
      Number currentValue = wrapper.get();
      if (currentValue == null || currentValue.intValue() < 0) {
        wrapper.set(value);
        return true;
      }
      return false;
    }
  }

  protected static class Incrementer
      implements NumberWrapperVisitor<Boolean, Void, Void, RuntimeException> {

    @Override
    public <V extends Number> Boolean visitNumberWrapper(NumberWrapper<V> wrapper, Void p, Void q) {
      wrapper.increment();
      return true;
    }
  }
}
