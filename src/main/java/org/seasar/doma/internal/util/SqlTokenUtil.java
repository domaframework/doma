package org.seasar.doma.internal.util;

/** @author nakamura-to */
public final class SqlTokenUtil {

  public static boolean isWordPart(char c) {
    if (Character.isWhitespace(c)) {
      return false;
    }
    switch (c) {
      case '=':
      case '<':
      case '>':
      case '-':
      case ',':
      case '/':
      case '*':
      case '+':
      case '(':
      case ')':
      case ';':
        return false;
      default:
        return true;
    }
  }

  public static boolean isWhitespace(char c) {
    switch (c) {
      case '\u0009':
      case '\u000B':
      case '\u000C':
      case '\u001C':
      case '\u001D':
      case '\u001E':
      case '\u001F':
      case '\u0020':
        return true;
      default:
        return false;
    }
  }
}
