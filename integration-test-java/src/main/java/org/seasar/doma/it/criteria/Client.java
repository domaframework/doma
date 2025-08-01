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
package org.seasar.doma.it.criteria;

import org.seasar.doma.Column;
import org.seasar.doma.ColumnOverride;
import org.seasar.doma.Embedded;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

@Entity(metamodel = @Metamodel)
@Table(name = "CUSTOMER")
public class Client {

  @Id Integer customerId;

  @Embedded(
      columnOverrides =
          @ColumnOverride(
              name = "shippingAddress.street",
              column = @Column(name = "SHIPPING_STREET")))
  CompositeCustomerAddress address;

  public Integer getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  public CompositeCustomerAddress getAddress() {
    return address;
  }

  public void setAddress(CompositeCustomerAddress address) {
    this.address = address;
  }
}
