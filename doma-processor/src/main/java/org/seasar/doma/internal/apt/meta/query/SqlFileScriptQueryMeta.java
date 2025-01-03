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
package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.ScriptAnnot;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlFileScriptQueryMeta extends AbstractSqlFileQueryMeta {

  private ScriptAnnot scriptAnnot;

  public SqlFileScriptQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
  }

  void setScriptAnnot(ScriptAnnot scriptAnnot) {
    this.scriptAnnot = scriptAnnot;
  }

  public boolean getHaltOnError() {
    return scriptAnnot.getHaltOnErrorValue();
  }

  public String getBlockDelimiter() {
    return scriptAnnot.getBlockDelimiterValue();
  }

  public SqlLogType getSqlLogType() {
    return scriptAnnot.getSqlLogValue();
  }

  @Override
  public String getPath() {
    if (sqlAnnot == null) {
      // script file path
      return ScriptFileUtil.buildPath(getDaoElement().getQualifiedName().toString(), getName());
    }
    return buildQualifiedMethodName();
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitSqlFileScriptQueryMeta(this);
  }
}
