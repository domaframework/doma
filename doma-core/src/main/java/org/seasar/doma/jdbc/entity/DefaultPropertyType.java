package org.seasar.doma.jdbc.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.entity.PropertyField;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * A description for a default property.
 *
 * @param <ENTITY> the entity type
 * @param <BASIC> the property basic type
 * @param <CONTAINER> the property container type
 */
public class DefaultPropertyType<ENTITY, BASIC, CONTAINER>
    implements EntityPropertyType<ENTITY, BASIC> {

  protected final Class<ENTITY> entityClass;

  protected final Supplier<Scalar<BASIC, CONTAINER>> scalarSupplier;

  protected final String name;

  protected final String simpleName;

  protected final String columnName;

  protected final NamingType namingType;

  protected final boolean insertable;

  protected final boolean updatable;

  protected final boolean quoteRequired;

  protected final PropertyField<ENTITY> field;

  public DefaultPropertyType(
      Class<ENTITY> entityClass,
      Supplier<Scalar<BASIC, CONTAINER>> scalarSupplier,
      String name,
      String columnName,
      NamingType namingType,
      boolean insertable,
      boolean updatable,
      boolean quoteRequired) {
    if (entityClass == null) {
      throw new DomaNullPointerException("entityClass");
    }
    if (scalarSupplier == null) {
      throw new DomaNullPointerException("scalarSupplier");
    }
    if (name == null) {
      throw new DomaNullPointerException("name");
    }
    if (columnName == null) {
      throw new DomaNullPointerException("columnName");
    }
    this.entityClass = entityClass;
    this.scalarSupplier = scalarSupplier;
    this.name = name;
    int pos = name.lastIndexOf('.');
    this.simpleName = pos > -1 ? name.substring(pos + 1) : name;
    this.columnName = columnName;
    this.namingType = namingType;
    this.insertable = insertable;
    this.updatable = updatable;
    this.quoteRequired = quoteRequired;
    this.field = new PropertyField<>(name, entityClass);
  }

  @Override
  public Property<ENTITY, BASIC> createProperty() {
    return new DefaultProperty();
  }

  @Override
  public void copy(ENTITY destEntity, ENTITY srcEntity) {
    Property<ENTITY, BASIC> dest = createProperty();
    dest.load(destEntity);
    Property<ENTITY, BASIC> src = createProperty();
    src.load(srcEntity);
    dest.getWrapper().set(src.getWrapper().getCopy());
    dest.save(destEntity);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getColumnName() {
    return getColumnName(Function.identity());
  }

  @Override
  public String getColumnName(Function<String, String> quoteFunction) {
    return getColumnName(Naming.DEFAULT::apply, quoteFunction);
  }

  @Override
  public String getColumnName(BiFunction<NamingType, String, String> namingFunction) {
    return getColumnName(namingFunction, Function.identity());
  }

  public String getColumnName(
      BiFunction<NamingType, String, String> namingFunction,
      Function<String, String> quoteFunction) {
    String columnName = this.columnName;
    if (columnName.isEmpty()) {
      columnName = namingFunction.apply(namingType, simpleName);
    }
    return quoteRequired ? quoteFunction.apply(columnName) : columnName;
  }

  @Override
  public boolean isQuoteRequired() {
    return quoteRequired;
  }

  @Override
  public boolean isId() {
    return false;
  }

  @Override
  public boolean isVersion() {
    return false;
  }

  @Override
  public boolean isTenantId() {
    return false;
  }

  @Override
  public boolean isInsertable() {
    return insertable;
  }

  @Override
  public boolean isUpdatable() {
    return updatable;
  }

  protected <VALUE> ENTITY modifyIfNecessary(
      EntityType<ENTITY> entityType,
      ENTITY entity,
      WrapperVisitor<Boolean, VALUE, Void, RuntimeException> visitor,
      VALUE value) {
    if (entityType.isImmutable()) {
      List<EntityPropertyType<ENTITY, ?>> propertyTypes = entityType.getEntityPropertyTypes();
      Map<String, Property<ENTITY, ?>> args = new HashMap<>(propertyTypes.size());
      for (EntityPropertyType<ENTITY, ?> propertyType : propertyTypes) {
        Property<ENTITY, ?> property = propertyType.createProperty();
        property.load(entity);
        if (propertyType == this) {
          Boolean modified = property.getWrapper().accept(visitor, value, null);
          if (modified == Boolean.FALSE) {
            return entity;
          }
        }
        args.put(propertyType.getName(), property);
      }
      return entityType.newEntity(args);
    } else {
      Property<ENTITY, ?> property = createProperty();
      property.load(entity);
      Boolean modified = property.getWrapper().accept(visitor, value, null);
      if (modified == Boolean.FALSE) {
        return entity;
      }
      property.save(entity);
      return entity;
    }
  }

  protected class DefaultProperty implements Property<ENTITY, BASIC> {

    protected final Scalar<BASIC, CONTAINER> scalar;

    protected DefaultProperty() {
      this.scalar = scalarSupplier.get();
    }

    @Override
    public Object get() {
      return scalar.get();
    }

    @Override
    public Property<ENTITY, BASIC> load(ENTITY entity) {
      Object value = field.getValue(entity);
      scalar.set(scalar.cast(value));
      return this;
    }

    @Override
    public Property<ENTITY, BASIC> save(ENTITY entity) {
      field.setValue(entity, scalar.get());
      return this;
    }

    @Override
    public InParameter<BASIC> asInParameter() {
      return new ScalarInParameter<>(scalar);
    }

    @Override
    public Wrapper<BASIC> getWrapper() {
      return scalar.getWrapper();
    }

    @Override
    public Optional<Class<?>> getDomainClass() {
      return scalar.getDomainClass();
    }

    @Override
    public String toString() {
      return scalar.toString();
    }
  }
}
