package example.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.processing.Generated;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.AbstractEntityDesc;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyDesc;
import org.seasar.doma.jdbc.entity.DefaultPropertyDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyDesc;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

@Generated("")
public class _ImmutableEmp extends AbstractEntityDesc<ImmutableEmp> {

  private static _ImmutableEmp singleton = new _ImmutableEmp();

  private final NamingType __namingType = NamingType.UPPER_CASE;

  public final AssignedIdPropertyDesc<ImmutableEmp, Integer, Integer> id =
      new AssignedIdPropertyDesc<>(
          ImmutableEmp.class,
          () -> new BasicScalar<>(new IntegerWrapper(), false),
          "id",
          "ID",
          __namingType,
          false);

  public final DefaultPropertyDesc<ImmutableEmp, String, String> name =
      new DefaultPropertyDesc<>(
          ImmutableEmp.class,
          () -> new BasicScalar<>(new StringWrapper(), false),
          "name",
          "NAME",
          __namingType,
          true,
          true,
          false);

  public final DefaultPropertyDesc<ImmutableEmp, BigDecimal, BigDecimal> salary =
      new DefaultPropertyDesc<>(
          ImmutableEmp.class,
          () -> new BasicScalar<>(new BigDecimalWrapper(), false),
          "salary",
          "SALARY",
          __namingType,
          true,
          true,
          false);

  public final VersionPropertyDesc<ImmutableEmp, Integer, Integer> version =
      new VersionPropertyDesc<>(
          ImmutableEmp.class,
          () -> new BasicScalar<>(new IntegerWrapper(), false),
          "version",
          "VERSION",
          __namingType,
          false);

  private final String __name = "Emp";

  private final String __catalogName = null;

  private final String __schemaName = null;

  private final String __tableName = "EMP";

  private final List<EntityPropertyDesc<ImmutableEmp, ?>> __idPropertyDescs;

  private final List<EntityPropertyDesc<ImmutableEmp, ?>> __entityPropertyDescs;

  private final Map<String, EntityPropertyDesc<ImmutableEmp, ?>> __entityPropertyDescMap;

  private _ImmutableEmp() {
    List<EntityPropertyDesc<ImmutableEmp, ?>> __idList = new ArrayList<>();
    __idList.add(id);
    __idPropertyDescs = Collections.unmodifiableList(__idList);
    List<EntityPropertyDesc<ImmutableEmp, ?>> __list = new ArrayList<>();
    __list.add(id);
    __list.add(name);
    __list.add(salary);
    __list.add(version);
    __entityPropertyDescs = Collections.unmodifiableList(__list);
    Map<String, EntityPropertyDesc<ImmutableEmp, ?>> __map = new HashMap<>();
    __map.put("id", id);
    __map.put("name", name);
    __map.put("salary", salary);
    __map.put("version", version);
    __entityPropertyDescMap = Collections.unmodifiableMap(__map);
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
  public List<EntityPropertyDesc<ImmutableEmp, ?>> getEntityPropertyDescs() {
    return __entityPropertyDescs;
  }

  @Override
  public EntityPropertyDesc<ImmutableEmp, ?> getEntityPropertyDesc(String propertyName) {
    return __entityPropertyDescMap.get(propertyName);
  }

  @Override
  public void saveCurrentStates(ImmutableEmp __entity) {}

  @Override
  public ImmutableEmp getOriginalStates(ImmutableEmp entity) {
    return null;
  }

  @Override
  public GeneratedIdPropertyDesc<ImmutableEmp, ?, ?> getGeneratedIdPropertyDesc() {
    return null;
  }

  @Override
  public VersionPropertyDesc<ImmutableEmp, ?, ?> getVersionPropertyDesc() {
    return version;
  }

  @Override
  public List<EntityPropertyDesc<ImmutableEmp, ?>> getIdPropertyDescs() {
    return __idPropertyDescs;
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
