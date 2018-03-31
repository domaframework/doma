package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.internal.jdbc.dao.AbstractDao;
import org.seasar.doma.jdbc.SelectOptions;

/** @author taedium */
public class NoConfigEmpDaoImpl extends AbstractDao implements NoConfigEmpDao {

  /** @param config the config */
  public NoConfigEmpDaoImpl(org.seasar.doma.jdbc.Config config) {
    super(config);
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
