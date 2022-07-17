package org.seasar.doma.jdbc.criteria.context;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

/** Represents the settings for an INSERT criteria query. */
public class InsertSettings extends Settings {
  private int batchSize = 0;
  private boolean excludeNull;
  private final List<PropertyMetamodel<?>> includedProperties = new ArrayList<>();
  private final List<PropertyMetamodel<?>> excludedProperties = new ArrayList<>();

  private boolean ignoreGeneratedKeys;

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
   * <p>If the value is {@code true}, the null properties are excluded from the SQL INSERT
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

  /**
   * Returns whether auto-generated keys are ignored.
   *
   * @return Whether auto-generated keys are ignored.
   */
  public boolean getIgnoreGeneratedKeys() {
    return ignoreGeneratedKeys;
  }

  /**
   * If this flag is enabled, performance may be improved. However, note that entities won't have
   * auto-generated keys.
   *
   * @param ignoreGeneratedKeys whether auto-generated keys are ignored
   */
  public void setIgnoreGeneratedKeys(boolean ignoreGeneratedKeys) {
    this.ignoreGeneratedKeys = ignoreGeneratedKeys;
  }
}
