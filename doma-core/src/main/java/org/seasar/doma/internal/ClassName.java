package org.seasar.doma.internal;

import java.util.Objects;
import org.seasar.doma.internal.util.ClassUtil;

public class ClassName implements CharSequence {

  private final String qualifiedName;

  public ClassName(String qualifiedName) {
    this.qualifiedName = Objects.requireNonNull(qualifiedName);
  }

  public String getPackageName() {
    return ClassUtil.getPackageName(qualifiedName);
  }

  public String getSimpleName() {
    return ClassUtil.getSimpleName(qualifiedName);
  }

  @Override
  public int length() {
    return qualifiedName.length();
  }

  @Override
  public char charAt(int index) {
    return qualifiedName.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return qualifiedName.subSequence(start, end);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public String toString() {
    return qualifiedName;
  }
}
