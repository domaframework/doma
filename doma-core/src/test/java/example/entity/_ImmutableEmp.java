package example.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Generated;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.AbstractEntityType;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

@Generated("")
public class _ImmutableEmp extends AbstractEntityType<ImmutableEmp> {

  private static final _ImmutableEmp singleton = new _ImmutableEmp();

  private final NamingType __namingType = NamingType.UPPER_CASE;

  public final AssignedIdPropertyType<ImmutableEmp, Integer, Integer> id =
      new AssignedIdPropertyType<>(
          ImmutableEmp.class,
          () -> new BasicScalar<>(IntegerWrapper::new),
          "id",
          "ID",
          __namingType,
          false);

  public final DefaultPropertyType<ImmutableEmp, String, String> name =
      new DefaultPropertyType<>(
          ImmutableEmp.class,
          () -> new BasicScalar<>(StringWrapper::new),
          "name",
          "NAME",
          __namingType,
          true,
          true,
          false);

  public final DefaultPropertyType<ImmutableEmp, BigDecimal, BigDecimal> salary =
      new DefaultPropertyType<>(
          ImmutableEmp.class,
          () -> new BasicScalar<>(BigDecimalWrapper::new),
          "salary",
          "SALARY",
          __namingType,
          true,
          true,
          false);

  public final VersionPropertyType<ImmutableEmp, Integer, Integer> version =
      new VersionPropertyType<>(
          ImmutableEmp.class,
          () -> new BasicScalar<>(IntegerWrapper::new),
          "version",
          "VERSION",
          __namingType,
          false);

  private final String __name = "Emp";

  private final String __catalogName = null;

  private final String __schemaName = null;

  private final String __tableName = "EMP";

  private final List<EntityPropertyType<ImmutableEmp, ?>> __idPropertyTypes;

  private final List<EntityPropertyType<ImmutableEmp, ?>> __entityPropertyTypes;

  private final Map<String, EntityPropertyType<ImmutableEmp, ?>> __entityPropertyTypeMap;

  private _ImmutableEmp() {
    List<EntityPropertyType<ImmutableEmp, ?>> __idList = new ArrayList<>();
    __idList.add(id);
    __idPropertyTypes = Collections.unmodifiableList(__idList);
    List<EntityPropertyType<ImmutableEmp, ?>> __list = new ArrayList<>();
    __list.add(id);
    __list.add(name);
    __list.add(salary);
    __list.add(version);
    __entityPropertyTypes = Collections.unmodifiableList(__list);
    Map<String, EntityPropertyType<ImmutableEmp, ?>> __map = new HashMap<>();
    __map.put("id", id);
    __map.put("name", name);
    __map.put("salary", salary);
    __map.put("version", version);
    __entityPropertyTypeMap = Collections.unmodifiableMap(__map);
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public ImmutableEmp newEntity(Map<String, Property<ImmutableEmp, ?>> args) {
    return new ImmutableEmp(
        (Integer) (args.containsKey("id") ? args.get("id").get() : null),
        (String) (args.containsKey("name") ? args.get("name").get() : null),
        (BigDecimal) (args.containsKey("salary") ? args.get("salary").get() : null),
        (Integer) (args.containsKey("version") ? args.get("version").get() : null));
  }

  @Override
  public Class<ImmutableEmp> getEntityClass() {
    return ImmutableEmp.class;
  }

  @Override
  public String getName() {
    return __name;
  }

  @Override
  public List<EntityPropertyType<ImmutableEmp, ?>> getEntityPropertyTypes() {
    return __entityPropertyTypes;
  }

  @Override
  public EntityPropertyType<ImmutableEmp, ?> getEntityPropertyType(String propertyName) {
    return __entityPropertyTypeMap.get(propertyName);
  }

  @Override
  public void saveCurrentStates(ImmutableEmp __entity) {}

  @Override
  public ImmutableEmp getOriginalStates(ImmutableEmp entity) {
    return null;
  }

  @Override
  public GeneratedIdPropertyType<ImmutableEmp, ?, ?> getGeneratedIdPropertyType() {
    return null;
  }

  @Override
  public VersionPropertyType<ImmutableEmp, ?, ?> getVersionPropertyType() {
    return version;
  }

  @Override
  public TenantIdPropertyType<ImmutableEmp, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public List<EntityPropertyType<ImmutableEmp, ?>> getIdPropertyTypes() {
    return __idPropertyTypes;
  }

  @Override
  public void preInsert(ImmutableEmp entity, PreInsertContext<ImmutableEmp> context) {}

  @Override
  public void preUpdate(ImmutableEmp entity, PreUpdateContext<ImmutableEmp> context) {}

  @Override
  public void preDelete(ImmutableEmp entity, PreDeleteContext<ImmutableEmp> context) {}

  @Override
  public void postInsert(ImmutableEmp entity, PostInsertContext<ImmutableEmp> context) {}

  @Override
  public void postUpdate(ImmutableEmp entity, PostUpdateContext<ImmutableEmp> context) {}

  @Override
  public void postDelete(ImmutableEmp entity, PostDeleteContext<ImmutableEmp> context) {}

  @Override
  public String getCatalogName() {
    return __catalogName;
  }

  @Override
  public String getSchemaName() {
    return __schemaName;
  }

  @Override
  @Deprecated
  public String getTableName() {
    return __tableName;
  }

  @Override
  public String getTableName(BiFunction<NamingType, String, String> namingFunction) {
    if (__tableName.isEmpty()) {
      return namingFunction.apply(getNamingType(), getName());
    }
    return __tableName;
  }

  @Override
  public NamingType getNamingType() {
    return __namingType;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }

  public static _ImmutableEmp getSingletonInternal() {
    return singleton;
  }
}
