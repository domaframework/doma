package org.seasar.doma.internal.util;

public final class IntegerUtil {

  /**
   * Computes the next power of two greater than or equal to the given integer. If the input is
   * already a power of two, the method returns the same value. If the computation results in an
   * overflow, the input value is returned instead.
   *
   * @param n the integer input, must be greater than or equal to 0
   * @return the next power of two greater than or equal to the input value
   * @throws IllegalArgumentException if the input value is less than 0
   */
  public static int nextPowerOfTwo(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("n must be greater than or equal to 0");
    }
    if (n == 0) {
      return 1;
    }
    // The input is already a power of two
    if ((n & (n - 1)) == 0) {
      return n;
    }
    int result = Integer.highestOneBit(n) << 1;
    // The calculation overflowed
    if (result < 0) {
      return n;
    }
    return result;
  }
}
