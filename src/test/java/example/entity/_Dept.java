package example.entity;

import java.util.*;
import java.util.function.BiFunction;
import javax.annotation.processing.Generated;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.*;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

@Generated("")
public class _Dept extends AbstractEntityDesc<Dept> {

  private static _Dept singleton = new _Dept();

  private final NamingType __namingType = NamingType.SNAKE_UPPER_CASE;

  public final AssignedIdPropertyDesc<Dept, Integer, Integer> id =
      new AssignedIdPropertyDesc<>(
          Dept.class,
          () -> new BasicScalar<>(new IntegerWrapper(), false),
          "id",
          "ID",
          __namingType,
          false);

  public final DefaultPropertyDesc<Dept, String, String> name =
      new DefaultPropertyDesc<>(
          Dept.class,
          () -> new BasicScalar<>(new StringWrapper(), false),
          "name",
          "NAME",
          __namingType,
          true,
          true,
          false);

  private final String __name = "Dept";

  private final String __catalogName = "CATA";

  private final String __schemaName = null;

  private final String __tableName = "";

  private final List<EntityPropertyDesc<Dept, ?>> __idPropertyDescs;

  private final List<EntityPropertyDesc<Dept, ?>> __entityPropertyDescs;

  private final Map<String, EntityPropertyDesc<Dept, ?>> __entityPropertyDescMap;

  private _Dept() {
    List<EntityPropertyDesc<Dept, ?>> __idList = new ArrayList<>();
    __idList.add(id);
    __idPropertyDescs = Collections.unmodifiableList(__idList);
    List<EntityPropertyDesc<Dept, ?>> __list = new ArrayList<>();
    __list.add(id);
    __list.add(name);
    __entityPropertyDescs = Collections.unmodifiableList(__list);
    Map<String, EntityPropertyDesc<Dept, ?>> __map = new HashMap<>();
    __map.put("id", id);
    __map.put("name", name);
    __entityPropertyDescMap = Collections.unmodifiableMap(__map);
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public Dept newEntity(Map<String, Property<Dept, ?>> args) {
    Dept entity = new Dept();
    args.values().forEach(v -> v.save(entity));
    return entity;
  }

  @Override
  public Class<Dept> getEntityClass() {
    return Dept.class;
  }

  @Override
  public String getName() {
    return __name;
  }

  @Override
  public List<EntityPropertyDesc<Dept, ?>> getEntityPropertyDescs() {
    return __entityPropertyDescs;
  }

  @Override
  public EntityPropertyDesc<Dept, ?> getEntityPropertyDesc(String propertyName) {
    return __entityPropertyDescMap.get(propertyName);
  }

  @Override
  public void saveCurrentStates(Dept __entity) {}

  @Override
  public Dept getOriginalStates(Dept entity) {
    return null;
  }

  @Override
  public GeneratedIdPropertyDesc<Dept, ?, ?> getGeneratedIdPropertyDesc() {
    return null;
  }

  @Override
  public VersionPropertyDesc<Dept, ?, ?> getVersionPropertyDesc() {
    return null;
  }

  @Override
  public List<EntityPropertyDesc<Dept, ?>> getIdPropertyDescs() {
    return __idPropertyDescs;
  }

  @Override
  public void preInsert(Dept entity, PreInsertContext<Dept> context) {}

  @Override
  public void preUpdate(Dept entity, PreUpdateContext<Dept> context) {}

  @Override
  public void preDelete(Dept entity, PreDeleteContext<Dept> context) {}

  @Override
  public void postInsert(Dept entity, PostInsertContext<Dept> context) {}

  @Override
  public void postUpdate(Dept entity, PostUpdateContext<Dept> context) {}

  @Override
  public void postDelete(Dept entity, PostDeleteContext<Dept> context) {}

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
    return true;
  }

  public static _Dept getSingletonInternal() {
    return singleton;
  }
}
