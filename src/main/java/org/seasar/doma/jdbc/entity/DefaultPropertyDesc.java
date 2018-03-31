package org.seasar.doma.jdbc.entity;

import java.util.HashMap;
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
import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * A description for a default property.
 *
 * @param <ENTITY> the entity type
 * @param <BASIC> the basic type
 * @param <CONTAINER> the container type
 */
public class DefaultPropertyDesc<ENTITY, BASIC, CONTAINER>
    implements EntityPropertyDesc<ENTITY, BASIC> {

  /** the entity class */
  protected final Class<ENTITY> entityClass;

  /** the supplier of the scalar value */
  protected final Supplier<Scalar<BASIC, CONTAINER>> scalarSupplier;

  /** the qualified name of the property */
  protected final String name;

  /** the simple name of the property */
  protected final String simpleName;

  /** the column name */
  protected final String columnName;

  /** the naming convention */
  protected final NamingType namingType;

  /** whether the property is insertable */
  protected final boolean insertable;

  /** whether the property is updatable */
  protected final boolean updatable;

  /** whether the column name requires quotation marks */
  protected final boolean quoteRequired;

  /** the field of the property */
  protected final PropertyField<ENTITY> field;

  /**
   * Creates an instance.
   *
   * @param entityClass the entity class
   * @param scalarSupplier the supplier of the scalar value
   * @param name the qualified name of the property
   * @param columnName the column name
   * @param namingType the naming convention
   * @param insertable whether the property is insertable
   * @param updatable whether the property is updatable
   * @param quoteRequired whether the column name requires quotation marks
   */
  public DefaultPropertyDesc(
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
    var pos = name.lastIndexOf('.');
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
    var dest = createProperty();
    dest.load(destEntity);
    var src = createProperty();
    src.load(srcEntity);
    dest.getWrapper().set(src.getWrapper().getCopy());
    dest.save(destEntity);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getColumnName(BiFunction<NamingType, String, String> namingFunction) {
    return getColumnName(namingFunction, Function.identity());
  }

  public String getColumnName(
      BiFunction<NamingType, String, String> namingFunction,
      Function<String, String> quoteFunction) {
    var columnName = this.columnName;
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
  public boolean isInsertable() {
    return insertable;
  }

  @Override
  public boolean isUpdatable() {
    return updatable;
  }

  /**
   * Modifies the property if necessary.
   *
   * @param <VALUE> the value type
   * @param entityDesc the entity description
   * @param entity the entity
   * @param visitor the visitor
   * @param value the value
   * @return the entity whose property may be changed
   */
  protected <VALUE> ENTITY modifyIfNecessary(
      EntityDesc<ENTITY> entityDesc,
      ENTITY entity,
      WrapperVisitor<Boolean, VALUE, Void, RuntimeException> visitor,
      VALUE value) {
    if (entityDesc.isImmutable()) {
      var propertyDescs = entityDesc.getEntityPropertyDescs();
      Map<String, Property<ENTITY, ?>> args = new HashMap<>(propertyDescs.size());
      for (var propertyDesc : propertyDescs) {
        var property = propertyDesc.createProperty();
        property.load(entity);
        if (propertyDesc == this) {
          var modified = property.getWrapper().accept(visitor, value, null);
          if (modified == Boolean.FALSE) {
            return entity;
          }
        }
        args.put(propertyDesc.getName(), property);
      }
      return entityDesc.newEntity(args);
    } else {
      var property = createProperty();
      property.load(entity);
      var modified = property.getWrapper().accept(visitor, value, null);
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
      var value = field.getValue(entity);
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
    public Optional<Class<?>> getHolderClass() {
      return scalar.getHolderClass();
    }
  }
}
