package org.seasar.doma.jdbc.statistic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This class tests the functionality of the `Statistic` class, specifically the `calculate` method.
 * The `calculate` method updates the statistics based on the execution time of a new SQL query.
 * Each test in this class ensures that the behavior of the `calculate` method is as expected under
 * different scenarios.
 */
class StatisticTest {

  @Test
  void calculate_ShouldUpdateMaxMinAndAvgTimes() {
    // Arrange
    Statistic initialStatistic = new Statistic("SELECT * FROM users", 2, 30, 10, 50, 25.0);

    // Act
    Statistic updatedStatistic = initialStatistic.calculate(20);

    // Assert
    assertEquals(3, updatedStatistic.execCount());
    assertEquals(30, updatedStatistic.execMaxTime());
    assertEquals(10, updatedStatistic.execMinTime());
    assertEquals(70, updatedStatistic.execTotalTime());
    assertEquals(70 / 3.0, updatedStatistic.execAvgTime());
  }

  @Test
  void calculate_ShouldIncreaseExecCount() {
    // Arrange
    Statistic initialStatistic = new Statistic("SELECT * FROM orders", 1, 15, 15, 15, 15.0);

    // Act
    Statistic updatedStatistic = initialStatistic.calculate(25);

    // Assert
    assertEquals(2, updatedStatistic.execCount());
  }

  @Test
  void calculate_ShouldUpdateMaxTime() {
    // Arrange
    Statistic initialStatistic = new Statistic("SELECT * FROM products", 1, 15, 15, 15, 15.0);

    // Act
    Statistic updatedStatistic = initialStatistic.calculate(20);

    // Assert
    assertEquals(20, updatedStatistic.execMaxTime());
  }

  @Test
  void calculate_ShouldUpdateMinTime() {
    // Arrange
    Statistic initialStatistic = new Statistic("SELECT * FROM categories", 1, 20, 20, 20, 20.0);

    // Act
    Statistic updatedStatistic = initialStatistic.calculate(10);

    // Assert
    assertEquals(10, updatedStatistic.execMinTime());
  }

  @Test
  void calculate_ShouldUpdateTotalTime() {
    // Arrange
    Statistic initialStatistic = new Statistic("SELECT * FROM suppliers", 2, 50, 10, 60, 30.0);

    // Act
    Statistic updatedStatistic = initialStatistic.calculate(40);

    // Assert
    assertEquals(100, updatedStatistic.execTotalTime());
  }

  @Test
  void calculate_ShouldHandleEdgeCaseForMinAndMaxBeingEqual() {
    // Arrange
    Statistic initialStatistic = new Statistic("SELECT * FROM customers", 3, 20, 20, 60, 20.0);

    // Act
    Statistic updatedStatistic = initialStatistic.calculate(20);

    // Assert
    assertEquals(4, updatedStatistic.execCount());
    assertEquals(20, updatedStatistic.execMaxTime());
    assertEquals(20, updatedStatistic.execMinTime());
    assertEquals(80, updatedStatistic.execTotalTime());
    assertEquals(80 / 4.0, updatedStatistic.execAvgTime());
  }
}
