package org.seasar.doma.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ThrowingSupplierTest {

  @Test
  void testGetReturnsValue() throws Exception {
    // Arrange
    ThrowingSupplier<String, Exception> supplier = () -> "Success";

    // Act
    String result = supplier.get();

    // Assert
    assertEquals("Success", result);
  }

  @Test
  void testGetThrowsException() {
    // Arrange
    ThrowingSupplier<String, Exception> supplier =
        () -> {
          throw new Exception("Error occurred");
        };

    // Act & Assert
    Exception thrown = assertThrows(Exception.class, supplier::get);
    assertEquals("Error occurred", thrown.getMessage());
  }
}
