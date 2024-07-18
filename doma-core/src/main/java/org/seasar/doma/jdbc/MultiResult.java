package org.seasar.doma.jdbc;

import java.util.List;

/**
 * A processing result for an immutable entities.
 *
 * @param <ENTITY> the entity type
 */
public class MultiResult<ENTITY> {

  private final int count;

  private final List<ENTITY> entities;

  /**
   * Creates an instance.
   *
   * @param count the affected row count
   * @param entities the entities
   */
  public MultiResult(int count, List<ENTITY> entities) {
    this.count = count;
    this.entities = entities;
  }

  /**
   * Returns the affected row count.
   *
   * @return the affected row count
   */
  public int getCount() {
    return count;
  }

  /**
   * Returns the entities.
   *
   * @return the entities
   */
  public List<ENTITY> getEntities() {
    return entities;
  }

  /**
   * Returns the entities.
   *
   * @return the entities
   * @see <a href= "https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
   *     Declarations</a>
   */
  public List<ENTITY> component1() {
    return entities;
  }

  /**
   * Returns the affected row count.
   *
   * @return the affected row count
   * @see <a href= "https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
   *     Declarations</a>
   */
  public int component2() {
    return count;
  }

  @Override
  public String toString() {
    return "MultiResult(entities=" + entities + ", count=" + count + ")";
  }
}
