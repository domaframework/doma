package org.seasar.doma.internal.util;

/** @author taedium */
public final class CharSequenceUtil {

  public static boolean isEmpty(CharSequence charSequence) {
    return charSequence == null || charSequence.length() == 0;
  }

  public static boolean isNotEmpty(CharSequence charSequence) {
    return !isEmpty(charSequence);
  }

  public static boolean isBlank(CharSequence charSequence) {
    if (isEmpty(charSequence)) {
      return true;
    }
    for (int i = 0, length = charSequence.length(); i < length; i++) {
      char ch = charSequence.charAt(i);
      if (!Character.isWhitespace(ch)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isNotBlank(CharSequence charSequence) {
    return !isBlank(charSequence);
  }
}
