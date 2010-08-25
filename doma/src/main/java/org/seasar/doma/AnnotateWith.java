/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

/**
 * Daoインタフェースの実装クラスのソースコードにアノテーションを注釈することを示します。
 * <p>
 * このアノテーションには2種類の使い方があります
 * <ul>
 * <li>Daoインタフェースに直接的に注釈する方法。
 * <li>Daoインタフェースに間接的に注釈する方法。この方法では、任意のアノテーションに{@code AnnotateWith}
 * を注釈し、そのアノテーションをDaoインタフェースに注釈する。
 * </ul>
 * <p>
 * このアノテーションを直接的であれ間接的であれDaoインタフェースに注釈する場合、{@link Dao#config()} に値を設定してはいけません。
 * <p>
 * 
 * <h5>例:直接的に注釈する方法</h5>
 * <a href="http://code.google.com/p/google-guice/">Guice</a>
 * のアノテーションを注釈するには次のように記述します。
 * 
 * <pre>
 * &#064;Dao
 * &#064;AnnotateWith(annotations = {
 *         &#064;Annotation(target = AnnotationTarget.CONSTRUCTOR, type = Inject.class),
 *         &#064;Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = Named.class, elements = &quot;\&quot;sales\&quot;&quot;) })
 * public interface EmployeeDao {
 *     ...
 * }
 * </pre>
 * 
 * 実装クラスは次のようになります。
 * 
 * <pre>
 * public class EmployeeDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements example.EmployeeDao {
 * 
 *     &#064;com.google.inject.Inject()
 *     public EmployeeDaoImpl(@com.google.inject.name.Named(&quot;sales&quot;) org.seasar.doma.jdbc.Config config) {
 *         super(new org.seasar.doma.jdbc.ConfigProxy(config));
 *     }
 *     ...
 * }
 * </pre>
 * 
 * <h5>例:間接的に注釈する方法</h5>
 * {@code AnnotateWith} を任意のアノテーションに注釈し、そのアノテーションをDaoに注釈することも可能です。 たとえば、ここでは、
 * {@code GuiceConfig} というアノテーションに {@code AnnotateWith} を注釈する例を示します。
 * 
 * <pre>
 * &#064;AnnotateWith(annotations = {
 *         &#064;Annotation(target = AnnotationTarget.CONSTRUCTOR, type = Inject.class),
 *         &#064;Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = Named.class, elements = &quot;\&quot;sales\&quot;&quot;) })
 * public &#064;interface GuiceConfig {
 *     ...
 * }
 * </pre>
 * 
 * {@code GuiceConfig} をDaoに注釈すれば、{@code AnnotateWith}を直接注釈した場合と同様の実装クラスが生成されます。
 * 
 * <pre>
 * &#064;Dao
 * &#064;GuiceConfig
 * public interface EmployeeDao {
 *     ...
 * }
 * </pre>
 * 
 * @author taedium
 * 
 */
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotateWith {

    /** アノテーション */
    Annotation[] annotations();
}
