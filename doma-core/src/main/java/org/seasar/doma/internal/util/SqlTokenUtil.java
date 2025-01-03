/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.util;

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
