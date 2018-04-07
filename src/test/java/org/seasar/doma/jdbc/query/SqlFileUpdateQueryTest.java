package org.seasar.doma.jdbc.query;

import example.entity.Emp;
import example.entity._Emp;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.Sql;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.entity.EntityDesc;
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

public class SqlFileUpdateQueryTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  @Sql("update aaa set /*%populate */ id = id where id = /* emp.id */1")
  public void testPopulate() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    var query = new SqlTemplateUpdateQuery();
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntityAndEntityDesc("emp", emp, _Emp.getSingletonInternal());
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.addParameter("emp", Emp.class, emp);
    query.prepare();

    UpdateQuery updateQuery = query;
    var sql = updateQuery.getSql();
    assertEquals(
        "update aaa set NAME = ?, SALARY = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
    assertEquals(
        "update aaa set NAME = 'aaa', SALARY = null, VERSION = 100 + 1 where id = 10",
        sql.getFormattedSql());
    List<? extends InParameter<?>> parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertEquals(null, parameters.get(1).getWrapper().get());
    assertEquals(100, parameters.get(2).getWrapper().get());
    assertEquals(10, parameters.get(3).getWrapper().get());
    assertTrue(query.isExecutable());
  }

  @Sql("update aaa set /*%populate */ id = id where id = /* emp.id */1")
  public void testPopulate_states() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);
    emp.originalStates = new Emp();

    var query = new SqlTemplateUpdateQuery();
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntityAndEntityDesc("emp", emp, _Emp.getSingletonInternal());
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.addParameter("emp", Emp.class, emp);
    query.prepare();

    var sql = query.getSql();
    assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());

    var parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertTrue(query.isExecutable());
  }

  @Sql("update aaa set /*%populate */ id = id where id = /* emp.id */1")
  public void testPopulate_excludeNull() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("hoge");
    emp.setVersion(100);

    var query = new SqlTemplateUpdateQuery();
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntityAndEntityDesc("emp", emp, _Emp.getSingletonInternal());
    query.setNullExcluded(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.addParameter("emp", Emp.class, emp);
    query.prepare();

    var sql = query.getSql();
    assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals("hoge", parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertTrue(query.isExecutable());
  }

  @Sql("update aaa set /*%populate */ id = id where id = /* emp.id */1")
  public void testPopulate_excludeNull_updateNullableInPreUpdate() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setVersion(100);

    var query = new SqlTemplateUpdateQuery();
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntityAndEntityDesc("emp", emp, new PreUpdate(_Emp.getSingletonInternal()));
    query.setNullExcluded(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.addParameter("emp", Emp.class, emp);
    query.prepare();

    var sql = query.getSql();
    assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals("hoge", parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertTrue(query.isExecutable());
  }

  @Sql("update aaa set /*%populate */ id = id where id = /* emp.id */1")
  public void testPopulate_ignoreVersion() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);
    emp.originalStates = new Emp();

    var query = new SqlTemplateUpdateQuery();
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntityAndEntityDesc("emp", emp, _Emp.getSingletonInternal());
    query.setVersionIgnored(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.addParameter("emp", Emp.class, emp);
    query.prepare();

    var sql = query.getSql();
    assertEquals("update aaa set NAME = ?, VERSION = ? where id = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertTrue(query.isExecutable());
  }

  @Sql("update aaa set /*%populate */ id = id where id = /* emp.id */1")
  public void testPopulate_include() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));
    emp.setVersion(100);

    var query = new SqlTemplateUpdateQuery();
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntityAndEntityDesc("emp", emp, _Emp.getSingletonInternal());
    query.setIncludedPropertyNames("name");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.addParameter("emp", Emp.class, emp);
    query.prepare();

    var sql = query.getSql();
    assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertTrue(query.isExecutable());
  }

  @Sql("update aaa set /*%populate */ id = id where id = /* emp.id */1")
  public void testPopulate_exclude() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));
    emp.setVersion(100);

    var query = new SqlTemplateUpdateQuery();
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntityAndEntityDesc("emp", emp, _Emp.getSingletonInternal());
    query.setExcludedPropertyNames("name");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.addParameter("emp", Emp.class, emp);
    query.prepare();

    var sql = query.getSql();
    assertEquals("update aaa set SALARY = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(new BigDecimal(200), parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertTrue(query.isExecutable());
  }

  @Sql("update aaa set /*%populate */ id = id where id = /* emp.id */1")
  public void testPopulate_IsExecutable() throws Exception {
    var emp = new Emp();
    emp.originalStates = new Emp();

    var query = new SqlTemplateUpdateQuery();
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntityAndEntityDesc("emp", emp, _Emp.getSingletonInternal());
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.addParameter("emp", Emp.class, emp);
    query.prepare();

    assertFalse(query.isExecutable());
  }

  @Sql("update aaa set NAME = /* name */'hoge', VERSION = /* version */0 + 1 where id = /* id */0")
  public void testNonEntity() throws Exception {
    var query = new SqlTemplateUpdateQuery();
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.addParameter("id", Integer.class, 10);
    query.addParameter("name", String.class, "aaa");
    query.addParameter("version", Integer.class, 100);
    query.prepare();

    UpdateQuery updateQuery = query;
    var sql = updateQuery.getSql();
    assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
    assertEquals(
        "update aaa set NAME = 'aaa', VERSION = 100 + 1 where id = 10", sql.getFormattedSql());
    List<? extends InParameter<?>> parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertTrue(query.isExecutable());
  }

  @Sql("update aaa set VERSION = /* emp.version */0 + 1 where id = /* emp.id */0\n")
  public void testOriginalStates_unchanged() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(BigDecimal.ZERO);
    emp.setVersion(100);
    emp.originalStates = new Emp();
    emp.originalStates.setId(emp.getId());
    emp.originalStates.setName(emp.getName());
    emp.originalStates.setSalary(emp.getSalary());
    emp.originalStates.setVersion(emp.getVersion());

    var query = new SqlTemplateUpdateQuery();
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntityAndEntityDesc("emp", emp, _Emp.getSingletonInternal());
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.addParameter("emp", Emp.class, emp);
    query.prepare();

    UpdateQuery updateQuery = query;
    var sql = updateQuery.getSql();
    assertEquals("update aaa set VERSION = ? + 1 where id = ?", sql.getRawSql());
    assertEquals("update aaa set VERSION = 100 + 1 where id = 10", sql.getFormattedSql());
    List<? extends InParameter<?>> parameters = sql.getParameters();
    assertEquals(2, parameters.size());
    assertEquals(100, parameters.get(0).getWrapper().get());
    assertEquals(10, parameters.get(1).getWrapper().get());
    assertFalse(query.isExecutable());
  }

  public static class PreUpdate implements EntityDesc<Emp> {

    protected final _Emp emp;

    public String getQualifiedTableName(
        BiFunction<NamingType, String, String> namingFunction,
        Function<String, String> quoteFunction) {
      return emp.getQualifiedTableName(namingFunction, quoteFunction);
    }

    public boolean isImmutable() {
      return emp.isImmutable();
    }

    public Emp newEntity(Map<String, Property<Emp, ?>> args) {
      return emp.newEntity(args);
    }

    public Class<Emp> getEntityClass() {
      return emp.getEntityClass();
    }

    public String getName() {
      return emp.getName();
    }

    public List<EntityPropertyDesc<Emp, ?>> getEntityPropertyDescs() {
      return emp.getEntityPropertyDescs();
    }

    public EntityPropertyDesc<Emp, ?> getEntityPropertyDesc(String propertyName) {
      return emp.getEntityPropertyDesc(propertyName);
    }

    public void saveCurrentStates(Emp __entity) {
      emp.saveCurrentStates(__entity);
    }

    public Emp getOriginalStates(Emp entity) {
      return emp.getOriginalStates(entity);
    }

    public GeneratedIdPropertyDesc<Emp, ?, ?> getGeneratedIdPropertyDesc() {
      return emp.getGeneratedIdPropertyDesc();
    }

    public VersionPropertyDesc<Emp, ?, ?> getVersionPropertyDesc() {
      return emp.getVersionPropertyDesc();
    }

    public List<EntityPropertyDesc<Emp, ?>> getIdPropertyDescs() {
      return emp.getIdPropertyDescs();
    }

    public void preInsert(Emp entity, PreInsertContext<Emp> context) {
      emp.preInsert(entity, context);
    }

    public void preUpdate(Emp entity, PreUpdateContext<Emp> context) {
      entity.setName("hoge");
    }

    public void preDelete(Emp entity, PreDeleteContext<Emp> context) {
      emp.preDelete(entity, context);
    }

    public void postInsert(Emp entity, PostInsertContext<Emp> context) {
      emp.postInsert(entity, context);
    }

    public void postUpdate(Emp entity, PostUpdateContext<Emp> context) {
      emp.postUpdate(entity, context);
    }

    public void postDelete(Emp entity, PostDeleteContext<Emp> context) {
      emp.postDelete(entity, context);
    }

    public String getCatalogName() {
      return emp.getCatalogName();
    }

    public String getSchemaName() {
      return emp.getSchemaName();
    }

    public String getTableName(BiFunction<NamingType, String, String> namingFunction) {
      return emp.getTableName(namingFunction);
    }

    public NamingType getNamingType() {
      return emp.getNamingType();
    }

    public boolean isQuoteRequired() {
      return emp.isQuoteRequired();
    }

    public PreUpdate(_Emp emp) {
      super();
      this.emp = emp;
    }
  }
}
