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
package org.seasar.doma.jdbc.criteria.context;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

/** Represents the settings for an UPDATE criteria query. */
public class UpdateSettings extends Settings {
  private int batchSize = 0;
  private boolean allowEmptyWhere;
  private boolean ignoreVersion;
  private boolean suppressOptimisticLockException;
  private boolean excludeNull;
  private final List<PropertyMetamodel<?>> includedProperties = new ArrayList<>();
  private final List<PropertyMetamodel<?>> excludedProperties = new ArrayList<>();

  /**
   * Returns the batch size.
   *
   * @return the batch size. The default value is {@literal 0}.
   */
  public int getBatchSize() {
    return batchSize;
  }

  /**
   * Sets the batch size.
   *
   * <p>If the value is less than 1, it is regarded as 1.
   *
   * @param batchSize the batch size
   * @see PreparedStatement#executeBatch()
   * @see PreparedStatement#addBatch()
   */
  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  /**
   * Returns whether the empty WHERE clause is allowed or not.
   *
   * @return whether the empty WHERE clause is allowed or not. The default value is {@literal
   *     false}.
   */
  public boolean getAllowEmptyWhere() {
    return allowEmptyWhere;
  }

  /**
   * Sets whether the empty WHERE clause is allowed or not.
   *
   * <p>If the value is {@literal true} and the WHERE clause is empty, {@link
   * org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException} will be suppressed.
   *
   * @param allowEmptyWhere whether the empty WHERE clause is allowed or not
   */
  public void setAllowEmptyWhere(boolean allowEmptyWhere) {
    this.allowEmptyWhere = allowEmptyWhere;
  }

  /**
   * Returns whether to ignore the version property or not.
   *
   * @return whether to ignore the version property or not. The default value is {@literal false}.
   */
  public boolean getIgnoreVersion() {
    return ignoreVersion;
  }

  /**
   * Sets whether to ignore the version property or not.
   *
   * <p>If the value is {@code true}, the column that mapped to the version property is excluded
   * from the SQL UPDATE statement.
   *
   * @param ignoreVersion whether to ignore the version property or not
   */
  public void setIgnoreVersion(boolean ignoreVersion) {
    this.ignoreVersion = ignoreVersion;
  }

  /**
   * Returns whether to suppress {@link org.seasar.doma.jdbc.OptimisticLockException} or not.
   *
   * @return whether to suppress {@link org.seasar.doma.jdbc.OptimisticLockException} or not. The
   *     default value is {@literal false}.
   */
  public boolean getSuppressOptimisticLockException() {
    return suppressOptimisticLockException;
  }

  /**
   * Sets whether to suppress {@link org.seasar.doma.jdbc.OptimisticLockException} or not.
   *
   * @param suppressOptimisticLockException whether to suppress {@link
   *     org.seasar.doma.jdbc.OptimisticLockException} or not
   */
  public void setSuppressOptimisticLockException(boolean suppressOptimisticLockException) {
    this.suppressOptimisticLockException = suppressOptimisticLockException;
  }

  /**
   * Returns whether to exclude null properties or not.
   *
   * @return whether to exclude null properties or not. The default value is {@literal false}.
   */
  public boolean getExcludeNull() {
    return excludeNull;
  }

  /**
   * Sets whether to exclude null properties or not.
   *
   * <p>If the value is {@code true}, the null properties are excluded from the SQL UPDATE
   * statement.
   *
   * @param excludeNull whether to exclude null properties or not
   */
  public void setExcludeNull(boolean excludeNull) {
    this.excludeNull = excludeNull;
  }

  /**
   * Returns the included properties.
   *
   * @return the included properties
   */
  public List<PropertyMetamodel<?>> include() {
    return includedProperties;
  }

  /**
   * Sets the included properties.
   *
   * @param propertyMetamodels the included properties
   */
  public void include(PropertyMetamodel<?>... propertyMetamodels) {
    Collections.addAll(includedProperties, propertyMetamodels);
  }

  /**
   * Returns the excluded properties.
   *
   * @return the excluded properties
   */
  public List<PropertyMetamodel<?>> exclude() {
    return excludedProperties;
  }

  /**
   * Sets the excluded properties.
   *
   * @param propertyMetamodels the excluded properties
   */
  public void exclude(PropertyMetamodel<?>... propertyMetamodels) {
    Collections.addAll(excludedProperties, propertyMetamodels);
  }
}
