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
package org.seasar.doma.internal.apt.validator;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Suppress;
import org.seasar.doma.message.Message;

@SuppressWarnings("deprecation")
@Dao
public interface BatchSqlValidationDao {

  void testEmbeddedVariable(String orderBy);

  @Suppress(messages = Message.DOMA4181)
  void testEmbeddedVariableSuppressed(String orderBy);

  void testIf();

  @Suppress(messages = Message.DOMA4182)
  void testIfSuppressed();

  void testFor(List<String> names);

  @Suppress(messages = Message.DOMA4183)
  void testForSuppressed(List<String> names);

  void testIfAndEmbeddedVariable(String orderBy);

  @Suppress(messages = {Message.DOMA4181, Message.DOMA4182})
  void testIfAndEmbeddedVariableSuppressed(String orderBy);

  void testPopulate(String name);
}
