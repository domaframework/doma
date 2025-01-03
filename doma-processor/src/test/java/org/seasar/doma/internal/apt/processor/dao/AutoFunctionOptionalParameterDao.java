/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.ResultSet;
import org.seasar.doma.jdbc.Reference;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
@Dao
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

  enum MyEnum {}
}
