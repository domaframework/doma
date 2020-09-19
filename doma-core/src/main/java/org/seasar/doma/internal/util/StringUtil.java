package org.seasar.doma.internal.util;

import java.nio.CharBuffer;
import java.util.function.Function;

public final class StringUtil {

  @SuppressWarnings("CStyleArrayDeclaration")
  public static String capitalize(String text) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    char chars[] = text.toCharArray();
    chars[0] = Character.toUpperCase(chars[0]);
    return new String(chars);
  }

  @SuppressWarnings("CStyleArrayDeclaration")
  public static String decapitalize(String text) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    char chars[] = text.toCharArray();
    chars[0] = Character.toLowerCase(chars[0]);
    return new String(chars);
  }

  public static String fromSnakeCaseToCamelCase(String text) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    String[] array = text.split("_");
    if (array.length == 0) {
      return "";
    }
    StringBuilder result = new StringBuilder();
    result.append(array[0].toLowerCase());
    for (int i = 1; i < array.length; i++) {
      String s = capitalize(array[i].toLowerCase());
      result.append(s);
    }
    return result.toString();
  }

  public static String fromCamelCaseToSnakeCase(String text) {
    return fromCamelCaseToSnakeCaseInternal(text, false);
  }

  public static String fromCamelCaseToSnakeCaseWithLenient(String text) {
    return fromCamelCaseToSnakeCaseInternal(text, true);
  }

  private static String fromCamelCaseToSnakeCaseInternal(String text, boolean lenient) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    Function<Character, Boolean> isNotUpperCase;
    if (lenient) {
      isNotUpperCase = Character::isLowerCase;
    } else {
      isNotUpperCase = c -> Character.isLowerCase(c) || Character.isDigit(c);
    }
    StringBuilder result = new StringBuilder();
    CharBuffer buf = CharBuffer.wrap(text);
    while (buf.hasRemaining()) {
      char c = buf.get();
      result.append(Character.toLowerCase(c));
      buf.mark();
      if (buf.hasRemaining()) {
        char c2 = buf.get();
        if (isNotUpperCase.apply(c) && Character.isUpperCase(c2)) {
          result.append("_");
        }
        buf.reset();
      }
    }
    return result.toString();
  }

  public static boolean isWhitespace(String text) {
    if (isNullOrEmpty(text)) {
      return false;
    }
    for (char ch : text.toCharArray()) {
      if (!Character.isWhitespace(ch)) {
        return false;
      }
    }
    return true;
  }

  public static String trimWhitespace(String text) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    char[] chars = text.toCharArray();
    int start = 0;
    int end = chars.length;

    while ((start < end) && (Character.isWhitespace(chars[start]))) {
      start++;
    }
    while ((start < end) && (Character.isWhitespace(chars[end - 1]))) {
      end--;
    }
    if (start < end) {
      return ((start > 0) || (end < chars.length)) ? new String(chars, start, end - start) : text;
    }
    return "";
  }

  private static boolean isNullOrEmpty(String text) {
    return text == null || text.isEmpty();
  }
}
