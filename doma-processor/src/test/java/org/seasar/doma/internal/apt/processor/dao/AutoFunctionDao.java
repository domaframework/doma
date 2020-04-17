package org.seasar.doma.internal.apt.processor.dao;

import example.domain.PhoneNumber;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.ResultSet;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.jdbc.Reference;

@Dao(config = MyConfig.class)
public interface AutoFunctionDao {

  @Function
  String executeFunction(
      @In Integer arg1, @InOut Reference<Integer> arg2, @Out Reference<Integer> arg3);

  @Function
  PhoneNumber executeFunction2(
      @In PhoneNumber arg1, @InOut Reference<PhoneNumber> arg2, @Out Reference<PhoneNumber> arg3);

  @Function
  List<String> executeFunction3(@ResultSet List<String> arg1);

  @Function
  List<PhoneNumber> executeFunction4(@ResultSet List<PhoneNumber> arg1);

  @Function
  List<Emp> executeFunction5(@ResultSet List<Emp> arg1);

  @Function
  MyEnum executeFunction6(
      @In MyEnum arg1, @InOut Reference<MyEnum> arg2, @Out Reference<MyEnum> arg3);

  public enum MyEnum {}
}
