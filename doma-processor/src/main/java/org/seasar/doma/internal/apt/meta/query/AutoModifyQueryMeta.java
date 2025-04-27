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
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.annot.ReturningAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.DeleteReturningCommand;
import org.seasar.doma.jdbc.command.InsertReturningCommand;
import org.seasar.doma.jdbc.command.UpdateReturningCommand;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class AutoModifyQueryMeta extends AbstractQueryMeta {

  private EntityCtType entityCtType;

  private String entityParameterName;

  private ModifyAnnot modifyAnnot;

  public AutoModifyQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
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

  public ModifyAnnot getModifyAnnot() {
    return modifyAnnot;
  }

  void setModifyAnnot(ModifyAnnot modifyAnnot) {
    this.modifyAnnot = modifyAnnot;
  }

  public ReturningAnnot getReturningAnnot() {
    return modifyAnnot.getReturningAnnot();
  }

  public boolean getSqlFile() {
    return modifyAnnot.getSqlFileValue();
  }

  public int getQueryTimeout() {
    return modifyAnnot.getQueryTimeoutValue();
  }

  public Boolean getIgnoreVersion() {
    return modifyAnnot.getIgnoreVersionValue();
  }

  public Boolean getExcludeNull() {
    return modifyAnnot.getExcludeNullValue();
  }

  public Boolean getSuppressOptimisticLockException() {
    return modifyAnnot.getSuppressOptimisticLockExceptionValue();
  }

  public Boolean getIncludeUnchanged() {
    return modifyAnnot.getIncludeUnchangedValue();
  }

  public List<String> getInclude() {
    return modifyAnnot.getIncludeValue();
  }

  public List<String> getExclude() {
    return modifyAnnot.getExcludeValue();
  }

  public List<String> getDuplicateKeys() {
    return modifyAnnot.getDuplicateKeysValue();
  }

  public SqlLogType getSqlLogType() {
    return modifyAnnot.getSqlLogValue();
  }

  public DuplicateKeyType getDuplicateKeyType() {
    return modifyAnnot.getDuplicateKeyTypeValue();
  }

  @Override
  public Class<?> getCommandClass() {
    var returningAnnot = modifyAnnot.getReturningAnnot();
    if (returningAnnot == null) {
      return super.getCommandClass();
    }
    return switch (queryKind) {
      case AUTO_DELETE -> DeleteReturningCommand.class;
      case AUTO_INSERT -> InsertReturningCommand.class;
      case AUTO_UPDATE -> UpdateReturningCommand.class;
      default -> throw new IllegalStateException(queryKind.name());
    };
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitAutoModifyQueryMeta(this);
  }
}
