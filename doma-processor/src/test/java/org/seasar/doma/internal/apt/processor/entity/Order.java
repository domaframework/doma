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
package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Column;
import org.seasar.doma.ColumnOverride;
import org.seasar.doma.Embedded;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class Order {
  @Id Integer id;

  @Embedded(
      columnOverrides = {
        @ColumnOverride(name = "city", column = @Column(name = "billing_city")),
        @ColumnOverride(name = "street", column = @Column(name = "billing_street"))
      })
  OrderAddress address1;

  @Embedded(
      prefix = "abc_",
      columnOverrides = {
        @ColumnOverride(
            name = "city",
            column =
                @Column(name = "shipping_city", insertable = true, updatable = true, quote = true)),
        @ColumnOverride(
            name = "street",
            column =
                @Column(
                    name = "shipping_street",
                    insertable = false,
                    updatable = false,
                    quote = false))
      })
  OrderAddress address2;
}
