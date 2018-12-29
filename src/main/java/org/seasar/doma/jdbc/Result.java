package org.seasar.doma.jdbc;

/**
 * イミュータブルなエンティティに対する更新や挿入の結果を表します。
 *
 * @author taedium
 * @param <ENTITY> エンティティ
 * @since 1.34.0
 */
public class Result<ENTITY> {

  private final int count;

  private final ENTITY entity;

  /**
   * インスタンスを構築します。
   *
   * @param count 更新件数
   * @param entity エンティティ
   */
  public Result(int count, ENTITY entity) {
    this.count = count;
    this.entity = entity;
  }

  /**
   * 更新件数を返します。
   *
   * @return 更新件数
   */
  public int getCount() {
    return count;
  }

  /**
   * エンティティを返します。
   *
   * @return エンティティ
   */
  public ENTITY getEntity() {
    return entity;
  }

  /**
   * エンティティを返します。
   *
   * @return エンティティ
   * @see <a href="https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
   *     Declarations</a>
   */
  public ENTITY component1() {
    return entity;
  }

  /**
   * 更新件数を返します。
   *
   * @return 更新件数
   * @see <a href="https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
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
