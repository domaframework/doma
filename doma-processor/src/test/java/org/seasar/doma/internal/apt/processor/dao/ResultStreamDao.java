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

import java.util.Map;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Select;
import org.seasar.doma.Suppress;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.message.Message;

@Dao
public interface ResultStreamDao {

  @Select
  Stream<Emp> selectByIdAndName(Integer id, String name);

  @Select
  Stream<PhoneNumber> selectById(Integer id);

  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<String> select();

  @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  @Suppress(messages = {Message.DOMA4274})
  Stream<Map<String, Object>> selectByIdAsMap(Integer id);
}
