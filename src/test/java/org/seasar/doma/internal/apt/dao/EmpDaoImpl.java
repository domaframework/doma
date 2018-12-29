package org.seasar.doma.internal.apt.dao;

import example.entity.Emp;
import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.internal.jdbc.dao.AbstractDao;
import org.seasar.doma.jdbc.SelectOptions;

public class EmpDaoImpl extends AbstractDao implements EmpDao {

  /** */
  public EmpDaoImpl() {
    super(new org.seasar.doma.internal.apt.dao.MyConfig());
  }

  /** @param connection the connection */
  public EmpDaoImpl(java.sql.Connection connection) {
    super(new org.seasar.doma.internal.apt.dao.MyConfig(), connection);
  }

  /** @param dataSource the dataSource */
  public EmpDaoImpl(javax.sql.DataSource dataSource) {
    super(new org.seasar.doma.internal.apt.dao.MyConfig(), dataSource);
  }

  /** @param config the configuration */
  protected EmpDaoImpl(org.seasar.doma.jdbc.Config config) {
    super(config);
  }

  /**
   * @param config the configuration
   * @param connection the connection
   */
  protected EmpDaoImpl(org.seasar.doma.jdbc.Config config, java.sql.Connection connection) {
    super(config, connection);
  }

  /**
   * @param config the configuration
   * @param dataSource the dataSource
   */
  protected EmpDaoImpl(org.seasar.doma.jdbc.Config config, javax.sql.DataSource dataSource) {
    super(config, dataSource);
  }

  @Override
  public Emp selectById(Integer id, SelectOptions options) {
    return null;
  }

  @Override
  public List<Emp> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions options) {
    return null;
  }

  @Override
  public int insert(Emp entity) {
    return 0;
  }

  @Override
  public int update(Emp entity) {
    return 0;
  }

  @Override
  public int delete(Emp entity) {
    return 0;
  }
}
