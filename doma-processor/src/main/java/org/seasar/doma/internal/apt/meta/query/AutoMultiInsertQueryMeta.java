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

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.MultiInsertAnnot;
import org.seasar.doma.internal.apt.annot.ReturningAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.InsertReturningCommand;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class AutoMultiInsertQueryMeta extends AbstractQueryMeta {

  private EntityCtType entityCtType;

  private String entityParameterName;

  private MultiInsertAnnot multiInsertAnnot;

  public AutoMultiInsertQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
  }

  public EntityCtType getEntityCtType() {
    return entityCtType;
  }

  public void setEntityCtType(EntityCtType entityCtType) {
    this.entityCtType = entityCtType;
  }

  public String getEntityParameterName() {
    return entityParameterName;
  }

  public void setEntityParameterName(String entityParameterName) {
    this.entityParameterName = entityParameterName;
  }

  public MultiInsertAnnot getMultiInsertAnnot() {
    return multiInsertAnnot;
  }

  void setMultiInsertAnnot(MultiInsertAnnot multiInsertAnnot) {
    this.multiInsertAnnot = multiInsertAnnot;
  }

  public ReturningAnnot getReturningAnnot() {
    return multiInsertAnnot.getReturningAnnot();
  }

  public int getQueryTimeout() {
    return multiInsertAnnot.getQueryTimeoutValue();
  }

  public List<String> getInclude() {
    return multiInsertAnnot.getIncludeValue();
  }

  public List<String> getExclude() {
    return multiInsertAnnot.getExcludeValue();
  }

  public List<String> getDuplicateKeys() {
    return multiInsertAnnot.getDuplicateKeysValue();
  }

  public SqlLogType getSqlLogType() {
    return multiInsertAnnot.getSqlLogValue();
  }

  public DuplicateKeyType getDuplicateKeyType() {
    return multiInsertAnnot.getDuplicateKeyTypeValue();
  }

  @Override
  public Class<?> getCommandClass() {
    var returningAnnot = multiInsertAnnot.getReturningAnnot();
    if (returningAnnot == null) {
      return super.getCommandClass();
    }
    return InsertReturningCommand.class;
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitAutoMultiInsertQueryMeta(this);
  }
}
