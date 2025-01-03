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
package org.seasar.doma.internal.expr;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.util.CharSequenceUtil;

public class NullExpressionFunctions implements ExpressionFunctions {

  @Override
  public String escape(CharSequence text) {
    return null;
  }

  @Override
  public String escape(CharSequence text, char escapeChar) {
    return null;
  }

  @Override
  public String prefix(CharSequence text) {
    if (text == null) {
      return null;
    }
    return text.toString();
  }

  @Override
  public String prefix(CharSequence text, char escape) {
    if (text == null) {
      return null;
    }
    return text.toString();
  }

  @Override
  public String suffix(CharSequence text) {
    if (text == null) {
      return null;
    }
    return text.toString();
  }

  @Override
  public String suffix(CharSequence text, char escape) {
    if (text == null) {
      return null;
    }
    return text.toString();
  }

  @Override
  public String infix(CharSequence text) {
    if (text == null) {
      return null;
    }
    return text.toString();
  }

  @Override
  public String infix(CharSequence text, char escapeChar) {
    if (text == null) {
      return null;
    }
    return text.toString();
  }

  @Override
  public java.util.Date roundDownTimePart(java.util.Date date) {
    return date;
  }

  @Override
  public Date roundDownTimePart(Date date) {
    return date;
  }

  @Override
  public Timestamp roundDownTimePart(Timestamp timestamp) {
    return timestamp;
  }

  @Override
  public LocalDateTime roundDownTimePart(LocalDateTime localDateTime) {
    return localDateTime;
  }

  @Override
  public java.util.Date roundUpTimePart(java.util.Date date) {
    return date;
  }

  @Override
  public Date roundUpTimePart(Date date) {
    return date;
  }

  @Override
  public Timestamp roundUpTimePart(Timestamp timestamp) {
    return timestamp;
  }

  @Override
  public LocalDate roundUpTimePart(LocalDate localDate) {
    return localDate;
  }

  @Override
  public LocalDateTime roundUpTimePart(LocalDateTime localDateTime) {
    return localDateTime;
  }

  @Override
  public boolean isEmpty(CharSequence charSequence) {
    return CharSequenceUtil.isEmpty(charSequence);
  }

  @Override
  public boolean isNotEmpty(CharSequence charSequence) {
    return CharSequenceUtil.isNotEmpty(charSequence);
  }

  @Override
  public boolean isBlank(CharSequence charSequence) {
    return CharSequenceUtil.isBlank(charSequence);
  }

  @Override
  public boolean isNotBlank(CharSequence charSequence) {
    return CharSequenceUtil.isNotBlank(charSequence);
  }
}
