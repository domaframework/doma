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
package org.seasar.doma.jdbc.criteria.statement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaException;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.message.Message;

class ReturningPropertyMetamodelsTest {

  @Test
  void of() {
    var e = new Emp_();
    assertNotNull(ReturningPropertyMetamodels.of(e, e.id));
  }

  @Test
  void of_error() {
    var e = new Emp_();
    var d = new Dept_();
    var ex = assertThrows(DomaException.class, () -> ReturningPropertyMetamodels.of(e, d.name));
    assertEquals(Message.DOMA6012, ex.getMessageResource());
    System.out.println(ex.getMessage());
  }

  @Test
  void resolve() {
    var e = new Emp_();
    var returning = ReturningPropertyMetamodels.of(e, e.id);
    var list = returning.resolve(e.asType());
    assertEquals(1, list.size());
    assertEquals(e.id.asType(), list.get(0));
  }

  @Test
  void resolve_error() {
    var e = new Emp_();
    var d = new Dept_();
    var returning = ReturningPropertyMetamodels.of(e, e.id);
    var ex = assertThrows(DomaException.class, () -> returning.resolve(d.asType()));
    assertEquals(Message.DOMA6013, ex.getMessageResource());
    System.out.println(ex.getMessage());
  }
}
