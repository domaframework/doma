package org.seasar.doma.internal.util;

import java.nio.CharBuffer;
import java.util.function.Function;

public final class StringUtil {

  public static String capitalize(String text) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    char chars[] = text.toCharArray();
    chars[0] = Character.toUpperCase(chars[0]);
    return new String(chars);
  }

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
    var array = text.split("_");
    if (array.length == 0) {
      return "";
    }
    var result = new StringBuilder();
    result.append(array[0].toLowerCase());
    for (var i = 1; i < array.length; i++) {
      var s = capitalize(array[i].toLowerCase());
      result.append(s);
    }
    return result.toString();
  }

  public static String fromCamelCaseToSnakeCase(String text) {
    return fromCamelCaseToSnakeCaseInternal(text);
  }

  private static String fromCamelCaseToSnakeCaseInternal(String text) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    Function<Character, Boolean> isNotUpperCase =
        c -> Character.isLowerCase(c) || Character.isDigit(c);
    var result = new StringBuilder();
    var buf = CharBuffer.wrap(text);
    while (buf.hasRemaining()) {
      var c = buf.get();
      result.append(Character.toLowerCase(c));
      buf.mark();
      if (buf.hasRemaining()) {
        var c2 = buf.get();
        if (isNotUpperCase.apply(c) && Character.isUpperCase(c2)) {
          result.append("_");
        }
        buf.reset();
      }
    }
    return result.toString();
  }

  /**
   * 文字列が空白文字だけからなるかどうかを返します。
   *
   * @param text 文字列
   * @return 文字列が空白文字のみを含む場合 {@code true}
   */
  public static boolean isWhitespace(String text) {
    if (isNullOrEmpty(text)) {
      return false;
    }
    for (var ch : text.toCharArray()) {
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
    var chars = text.toCharArray();
    var start = 0;
    var end = chars.length;

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
