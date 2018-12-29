package org.seasar.doma.internal.apt.domain;

/**
 * @author taedium
 * @see "https://gist.github.com/4257111"
 */
public abstract class GenericDomain<T> {

  private final T value;

  public GenericDomain(final T value) {
    this.value = value;
  }

  public T getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  public boolean canEqual(final Object other) {
    return getClass().isInstance(other);
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!getClass().isInstance(obj)) {
      return false;
    }
    final GenericDomain<T> other = (GenericDomain<T>) obj;
    return other.canEqual(this) && value.equals(other.value);
  }
}
