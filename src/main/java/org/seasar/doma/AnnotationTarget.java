package org.seasar.doma;

/**
 * アノテーションを注釈する対象です。
 *
 * @author taedium
 * @see Annotation
 */
public enum AnnotationTarget {

  /** クラス */
  CLASS,

  /** コンストラクタ */
  CONSTRUCTOR,

  /** コンストラクターのパラメータ */
  CONSTRUCTOR_PARAMETER,
}
