package org.seasar.doma.internal.apt.processor.dao;

import example.domain.JobType;
import example.domain.PhoneNumber;
import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

@Dao
public interface SqlFileSelectDomainDao {

  @Select
  PhoneNumber selectPhoneNumberById(Integer id);

  @Select
  List<PhoneNumber> selectPhoneNumberByNameAndSalary(
      String name, BigDecimal salary, SelectOptions options);

  @Select
  JobType selectJobTypeById(Integer id);

  @Select
  List<JobType> selectJobTypeByNameAndSalary(String name, BigDecimal salary, SelectOptions options);
}
