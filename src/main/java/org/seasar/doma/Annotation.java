package org.seasar.doma;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * アノテーションを示します。
 *
 * @author taedium
 * @see AnnotateWith
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation {

  /**
   * 注釈する対象を返します。
   *
   * @return 注釈する対象
   */
  AnnotationTarget target();

  /**
   * アノテーションの型を返します。
   *
   * @return アノテーションの型
   */
  Class<? extends java.lang.annotation.Annotation> type();

  /**
   * アノテーションの要素を返します。
   *
   * <p>「要素名 = 値」 形式で文字列を記述します。 複数存在する場合はカンマで区切ります。
   *
   * @return アノテーションの要素
   */
  String elements() default "";
}
