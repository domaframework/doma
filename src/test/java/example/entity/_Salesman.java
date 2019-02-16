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
public class _Salesman extends AbstractEntityType<Salesman> {

  private static _Salesman singleton = new _Salesman();

  private final NamingType __namingType = NamingType.UPPER_CASE;

  public final AssignedIdPropertyType<Salesman, Integer, Integer> id =
      new AssignedIdPropertyType<>(
          Salesman.class,
          () -> new BasicScalar<>(IntegerWrapper::new, false),
          "id",
          "ID",
          __namingType,
          false);

  public final DefaultPropertyType<Salesman, String, String> name =
      new DefaultPropertyType<>(
          Salesman.class,
          () -> new BasicScalar<>(StringWrapper::new, false),
          "name",
          "NAME",
          __namingType,
          true,
          true,
          false);

  public final DefaultPropertyType<Salesman, BigDecimal, BigDecimal> salary =
      new DefaultPropertyType<>(
          Salesman.class,
          () -> new BasicScalar<>(BigDecimalWrapper::new, false),
          "salary",
          "SALARY",
          __namingType,
          true,
          true,
          false);

  public final TenantIdPropertyType<Salesman, String, String> tenantId =
      new TenantIdPropertyType<>(
          Salesman.class,
          () -> new BasicScalar<>(StringWrapper::new, false),
          "tenantId",
          "TENANT_ID",
          __namingType,
          false);

  public final VersionPropertyType<Salesman, Integer, Integer> version =
      new VersionPropertyType<>(
          Salesman.class,
          () -> new BasicScalar<>(IntegerWrapper::new, false),
          "version",
          "VERSION",
          __namingType,
          false);

  private final String __name = "Salesman";

  private final String __catalogName = null;

  private final String __schemaName = null;

  private final String __tableName = "";

  private final List<EntityPropertyType<Salesman, ?>> __idPropertyTypes;

  private final List<EntityPropertyType<Salesman, ?>> __entityPropertyTypes;

  private final Map<String, EntityPropertyType<Salesman, ?>> __entityPropertyTypeMap;

  private _Salesman() {
    List<EntityPropertyType<Salesman, ?>> __idList = new ArrayList<>();
    __idList.add(id);
    __idPropertyTypes = Collections.unmodifiableList(__idList);
    List<EntityPropertyType<Salesman, ?>> __list = new ArrayList<>();
    __list.add(id);
    __list.add(name);
    __list.add(salary);
    __list.add(tenantId);
    __list.add(version);
    __entityPropertyTypes = Collections.unmodifiableList(__list);
    Map<String, EntityPropertyType<Salesman, ?>> __map = new HashMap<>();
    __map.put("id", id);
    __map.put("name", name);
    __map.put("salary", salary);
    __map.put("tenantId", tenantId);
    __map.put("version", version);
    __entityPropertyTypeMap = Collections.unmodifiableMap(__map);
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public Salesman newEntity(Map<String, Property<Salesman, ?>> args) {
    Salesman entity = new Salesman();
    args.values().forEach(v -> v.save(entity));
    return entity;
  }

  @Override
  public Class<Salesman> getEntityClass() {
    return Salesman.class;
  }

  @Override
  public String getName() {
    return __name;
  }

  @Override
  public List<EntityPropertyType<Salesman, ?>> getEntityPropertyTypes() {
    return __entityPropertyTypes;
  }

  @Override
  public EntityPropertyType<Salesman, ?> getEntityPropertyType(String propertyName) {
    return __entityPropertyTypeMap.get(propertyName);
  }

  @Override
  public void saveCurrentStates(Salesman __entity) {}

  @Override
  public Salesman getOriginalStates(Salesman entity) {
    return null;
  }

  @Override
  public GeneratedIdPropertyType<Salesman, ?, ?> getGeneratedIdPropertyType() {
    return null;
  }

  @Override
  public VersionPropertyType<Salesman, ?, ?> getVersionPropertyType() {
    return version;
  }

  @Override
  public TenantIdPropertyType<Salesman, ?, ?> getTenantIdPropertyType() {
    return tenantId;
  }

  @Override
  public List<EntityPropertyType<Salesman, ?>> getIdPropertyTypes() {
    return __idPropertyTypes;
  }

  @Override
  public void preInsert(Salesman entity, PreInsertContext<Salesman> context) {}

  @Override
  public void preUpdate(Salesman entity, PreUpdateContext<Salesman> context) {}

  @Override
  public void preDelete(Salesman entity, PreDeleteContext<Salesman> context) {}

  @Override
  public void postInsert(Salesman entity, PostInsertContext<Salesman> context) {}

  @Override
  public void postUpdate(Salesman entity, PostUpdateContext<Salesman> context) {}

  @Override
  public void postDelete(Salesman entity, PostDeleteContext<Salesman> context) {}

  @Override
  public String getCatalogName() {
    return __catalogName;
  }

  @Override
  public String getSchemaName() {
    return __schemaName;
  }

  @Override
  public String getTableName() {
    return getTableName((namingType, text) -> namingType.apply(text));
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

  public static _Salesman getSingletonInternal() {
    return singleton;
  }
}
