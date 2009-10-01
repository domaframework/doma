package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

/**
 * 変更されたプロパティの名前を管理するフィールドを示します。
 * <p>
 * UPDATE文のSET句に変更があったプロパティだけを含めたい場合に使用します。
 * <p>
 * このアノテーションが注釈されるフィールドは、 {@link Entity} が注釈されたクラスのメンバでなければいけません。 フィールドの型は
 * {@link String} を要素とする {@link Set} のサブタイプでなければいけません。フィールドに {@code Set}
 * のサブタイプのインスタンスを設定するのはアプリケーション開発者の責任です。
 * <p>
 * プロパティの名前は、各プロパティのセッターメソッドで設定しなければいけません。
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 * 
 *     String name;
 * 
 *     &#064;ChangedProperties
 *     Set&lt;String&gt; changedProperties = new HashSet&lt;String&gt;();
 *     
 *     public String getName() {
 *         return name;
 *     }
 *     
 *     public void setName(String name) {
 *         changedProperties.add(&quot;name&quot;);
 *         this.name = name;
 *     }
 *     
 *     ...
 * }
 * </pre>
 * 
 * @author taedium
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface ChangedProperties {
}
