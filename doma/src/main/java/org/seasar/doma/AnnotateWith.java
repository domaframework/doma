/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.doma.jdbc.ConfigAdapter;

/**
 * {@link Dao} が注釈されたインタフェースの実装クラスのソースコードにアノテーションを付与することを示します。
 * <p>
 * このアノテーションは次の場合にのみ有効です。
 * <ul>
 * <li>{@code Dao} と併用されている。
 * <li>{@code Dao} の {@code config} 要素に {@link ConfigAdapter} が指定されている。
 * </ul>
 * <p>
 * 
 * <h5>例:</h5>
 * <a href="http://code.google.com/p/google-guice/">google-guice</a>
 * のアノテーションを付与するには次のように記述します。
 * 
 * <pre>
 * &#064;Dao(config = ConfigAdapter.class)
 * &#064;AnnotateWith(annotations = {
 *         &#064;Annotation(target = AnnotationTarget.CONSTRUCOTR, type = Inject.class),
 *         &#064;Annotation(target = AnnotationTarget.CONSTRUCOTR_PARAMETER, type = Named.class, elements = &quot;\&quot;sales\&quot;&quot;) })
 * public interface EmployeeDao {
 *     ...
 * }
 * </pre>
 * 
 * 実装クラスは次のようになります。
 * 
 * <pre>
 * public class EmployeeDaoImpl extends org.seasar.doma.jdbc.DomaAbstractDao implements example.EmployeeDao {
 * 
 *     &#064;com.google.inject.Inject()
 *     public EmployeeDaoImpl(@com.google.inject.name.Named(&quot;sales&quot;) org.seasar.doma.jdbc.Config config) {
 *         super(new org.seasar.doma.jdbc.ConfigAdapter(config));
 *     }
 *     ...
 * }
 * </pre>
 * 
 * @author taedium
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotateWith {

    /** アノテーション */
    Annotation[] annotations();
}
