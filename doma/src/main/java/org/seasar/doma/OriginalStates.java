package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * エンティティがデータベースから取得された元の状態を管理するフィールドを示します。
 * <p>
 * 元の状態とは、エンティティを {@link Select} が注釈されたDaoメソッドから取得した時点におけるエンティティの状態です。
 * 変更があったプロパティのみをUPDATE文のSET句に含めたい場合に使用します。
 * <p>
 * このアノテーションが注釈されるフィールドは、エンティティクラスのメンバでなければいけません。
 * フィールドの型はエンティティクラスと同じでなければいけません。
 * <p>
 * このアノテーションが注釈されるフィールドに対し、アプリケーションはアクセスしてはいけません。
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 * 
 *     String name;
 * 
 *     &#064;OriginalStates
 *     Employee originalStates;
 *     
 *     public String getName() {
 *         return name;
 *     }
 *     
 *     public void setName(String name) {
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
public @interface OriginalStates {
}
