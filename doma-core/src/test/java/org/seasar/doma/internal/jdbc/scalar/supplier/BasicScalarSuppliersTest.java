package org.seasar.doma.internal.jdbc.scalar.supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.scalar.BasicScalarSuppliers;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;

class BasicScalarSuppliersTest {

  @Test
  void test() {
    DefaultPropertyType<MyEntity, String, String> propertyType =
        new DefaultPropertyType<>(
            MyEntity.class,
            BasicScalarSuppliers.ofString(),
            "name",
            "name",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertNotNull(propertyType.createProperty());
  }
}
