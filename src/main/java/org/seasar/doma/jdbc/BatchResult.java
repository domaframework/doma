package org.seasar.doma.jdbc;

import java.util.Arrays;
import java.util.List;

/**
 * イミュータブルなエンティティに対するバッチ更新やバッチ挿入の結果を表します。
 *
 * @author taedium
 * @param <ENTITY> エンティティ
 * @since 1.34.0
 */
public class BatchResult<ENTITY> {

  private final int[] counts;

  private final List<ENTITY> entities;

  /**
   * @param counts 更新件数の配列
   * @param entities エンティティのリスト
   */
  public BatchResult(int[] counts, List<ENTITY> entities) {
    this.counts = counts;
    this.entities = entities;
  }

  /**
   * 更新件数の配列を返します。
   *
   * @return 更新件数の配列
   */
  public int[] getCounts() {
    return counts;
  }

  /**
   * エンティティのリストを返します。
   *
   * @return エンティティのリスト
   */
  public List<ENTITY> getEntities() {
    return entities;
  }

  /**
   * エンティティのリストを返します。
   *
   * @return エンティティのリスト
   * @see <a href="https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
   *     Declarations</a>
   */
  public List<ENTITY> component1() {
    return entities;
  }

  /**
   * 更新件数の配列を返します。
   *
   * @return 更新件数の配列
   * @see <a href="https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
   *     Declarations</a>
   */
  public int[] component2() {
    return counts;
  }

  @Override
  public String toString() {
    return "BatchResult(entities=" + entities + ", counts=" + Arrays.toString(counts) + ")";
  }
}
