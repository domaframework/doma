package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.Config;

/**
 * Indicates that a generated code that implements the annotated interface is annotated with some
 * annotations.
 *
 * <p>This annotation is mainly intended to inject a {@link Config} instance to a DAO
 * implementation's constructor. Don't use {@link Dao#config()} with this annotation.
 *
 * <p>There are 2 ways to use this annotation:
 *
 * <ul>
 *   <li>annotate directly
 *   <li>annotate indirectly
 * </ul>
 *
 * <p>annotate directly:
 *
 * <pre>
 * &#64;Dao
 * &#64;AnnotateWith(annotations = {
 *   &#64;Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
 *   &#64;Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
 * public interface EmployeeDao {
 *    ...
 * }
 * </pre>
 *
 * annotate indirectly:
 *
 * <pre>
 * &#64;AnnotateWith(annotations = {
 *         &#64;Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
 *         &#64;Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
 * public @interface InjectConfig {
 * }
 * </pre>
 *
 * <pre>
 * &#64;Dao
 * &#64;InjectConfig
 * public interface EmployeeDao {
 *    ...
 * }
 * </pre>
 */
@SuppressWarnings("deprecation")
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotateWith {

  /**
   * @return the {@link Annotation} array.
   */
  Annotation[] annotations();
}
