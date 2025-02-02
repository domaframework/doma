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
package org.seasar.doma.jdbc;

import java.io.Serial;
import java.util.Objects;
import org.seasar.doma.message.Message;

public class UnknownColumnAdditionalInfoException extends UnknownColumnException {

  @Serial private static final long serialVersionUID = 1L;

  private final String additionalInfo;

  public UnknownColumnAdditionalInfoException(
      SqlLogType logType, UnknownColumnException exception, String additionalInfo) {
    super(
        Message.DOMA2238,
        new Object[] {
          exception.getColumnName(),
          exception.getEntityClassName(),
          exception.getSqlFilePath(),
          choiceSql(logType, exception.getRawSql(), exception.getFormattedSql()),
          additionalInfo
        },
        exception.getColumnName(),
        exception.getExpectedPropertyName(),
        exception.getEntityClassName(),
        exception.getKind(),
        exception.getRawSql(),
        exception.getFormattedSql(),
        exception.getSqlFilePath());
    this.additionalInfo = Objects.requireNonNull(additionalInfo);
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }
}
