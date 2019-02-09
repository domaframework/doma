package org.seasar.doma.internal;

import org.seasar.doma.internal.util.ClassUtil;

public class ClassName implements CharSequence {

  private final String qualifiedName;

  public ClassName(String qualifiedName) {
    this.qualifiedName = qualifiedName;
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

  @Override
  public String toString() {
    return qualifiedName;
  }
}
