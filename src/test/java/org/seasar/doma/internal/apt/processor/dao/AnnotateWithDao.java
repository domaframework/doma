package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.processor.entity.Emp;

/**
 * @author taedium
 * 
 */
@Dao
@AnnotateWith(annotations = {
        @Annotation(target = AnnotationTarget.CLASS, type = ClassAnnotation.class, elements = "aaa = 1, bbb = true"),
        @Annotation(target = AnnotationTarget.CLASS, type = ClassAnnotation2.class, elements = "aaa = 1, bbb = true"),
        @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = ConstructorAnnotation.class, elements = "aaa = 1, bbb = true"),
        @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = ConstructorAnnotation2.class, elements = "aaa = 1, bbb = true"),
        @Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = ConstructorParameterAnnotation.class, elements = "aaa = 1, bbb = true"),
        @Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = ConstructorParameterAnnotation2.class, elements = "aaa = 1, bbb = true") })
public interface AnnotateWithDao {

    @Insert
    int insert(Emp emp);
}
