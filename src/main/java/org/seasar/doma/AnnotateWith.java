package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Daoインタフェースの実装クラスのソースコードにアノテーションを注釈することを示します。
 *
 * <p>このアノテーションには2種類の使い方があります
 *
 * <ul>
 *   <li>Daoインタフェースに直接的に注釈する方法。
 *   <li>Daoインタフェースに間接的に注釈する方法。この方法では、任意のアノテーションに{@code AnnotateWith}
 *       を注釈し、そのアノテーションをDaoインタフェースに注釈する。
 * </ul>
 *
 * <p>このアノテーションを直接的であれ間接的であれDaoインタフェースに注釈する場合、{@link Dao#config()} に値を設定してはいけません。
 *
 * @author taedium
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotateWith {

  /**
   * アノテーションの配列を返します。
   *
   * @return アノテーションの配列
   */
  Annotation[] annotations();
}
