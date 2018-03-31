package org.seasar.doma.jdbc.entity;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.wrapper.NumberWrapper;
import org.seasar.doma.wrapper.NumberWrapperVisitor;

/**
 * A description for a version property.
 *
 * @param <ENTITY> the entity type
 * @param <BASIC> the basic type
 * @param <CONTAINER> the container of the basic type
 */
public class VersionPropertyDesc<ENTITY, BASIC extends Number, CONTAINER>
    extends DefaultPropertyDesc<ENTITY, BASIC, CONTAINER> {

  /**
   * Creates an instance.
   *
   * @param entityClass the entity class
   * @param scalarSupplier the supplier of a scalar
   * @param name the name of the property
   * @param columnName the name of the column
   * @param namingType naming convention
   * @param quoteRequired whether the column name requires quotation marks
   */
  public VersionPropertyDesc(
      Class<ENTITY> entityClass,
      Supplier<Scalar<BASIC, CONTAINER>> scalarSupplier,
      String name,
      String columnName,
      NamingType namingType,
      boolean quoteRequired) {
    super(entityClass, scalarSupplier, name, columnName, namingType, true, true, quoteRequired);
  }

  @Override
  public boolean isVersion() {
    return true;
  }

  /**
   * If necessary, sets the value to the version property.
   *
   * @param entityDesc the entity description
   * @param entity the entity
   * @param value the value
   * @return the entity
   */
  public ENTITY setIfNecessary(EntityDesc<ENTITY> entityDesc, ENTITY entity, Number value) {
    return modifyIfNecessary(entityDesc, entity, new ValueSetter(), value);
  }

  /**
   * Increments the version value.
   *
   * @param entityDesc the entity description
   * @param entity the entity
   * @return the entity
   */
  public ENTITY increment(EntityDesc<ENTITY> entityDesc, ENTITY entity) {
    return modifyIfNecessary(entityDesc, entity, new Incrementer(), null);
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
