package org.seasar.doma.jdbc;

/**
 * A processing result for an immutable entity.
 *
 * @param <ENTITY> the entity type
 */
public class Result<ENTITY> {

  private final int count;

  private final ENTITY entity;

  /**
   * Creates an instance.
   *
   * @param count the affected row count
   * @param entity the entity
   */
  public Result(int count, ENTITY entity) {
    this.count = count;
    this.entity = entity;
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
   * Returns the entity.
   *
   * @return the entity
   */
  public ENTITY getEntity() {
    return entity;
  }

  /**
   * Returns the entity.
   *
   * @return the entity
   * @see <a href= "https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
   *     Declarations</a>
   */
  public ENTITY component1() {
    return entity;
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
    return "Result(entity=" + entity + ", count=" + count + ")";
  }
}
