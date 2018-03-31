package org.seasar.doma.internal.apt.processor.dao;

import example.holder.PhoneNumber;
import java.util.List;
import java.util.Optional;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.Reference;

@Dao(config = MyConfig.class)
public interface AutoFunctionOptionalParameterDao {

  @Function
  Optional<String> executeFunction(
      @In Optional<Integer> arg1,
      @InOut Reference<Optional<Integer>> arg2,
      @Out Reference<Optional<Integer>> arg3);

  @Function
  PhoneNumber executeFunction2(
      @In Optional<PhoneNumber> arg1,
      @InOut Reference<Optional<PhoneNumber>> arg2,
      @Out Reference<Optional<PhoneNumber>> arg3);

  @Function
  List<Optional<String>> executeFunction3(@ResultSet List<Optional<String>> arg1);

  @Function
  List<Optional<PhoneNumber>> executeFunction4(@ResultSet List<Optional<PhoneNumber>> arg1);

  @Function
  Optional<MyEnum> executeFunction5(
      @In Optional<MyEnum> arg1,
      @InOut Reference<Optional<MyEnum>> arg2,
      @Out Reference<Optional<MyEnum>> arg3);

  @Function
  List<Optional<MyEnum>> executeFunction6(@ResultSet List<Optional<MyEnum>> arg1);

  public enum MyEnum {}
}
