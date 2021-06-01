package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
@MultipleAnnotationConfig1
@MultipleAnnotationConfig2
@AnnotateWith(
    annotations = {
      @Annotation(
          target = AnnotationTarget.CONSTRUCTOR_PARAMETER,
          type = ConstructorParameterAnnotation.class,
          elements = "aaa = 1, bbb = true"),
      @Annotation(
          target = AnnotationTarget.CONSTRUCTOR_PARAMETER,
          type = ConstructorParameterAnnotation2.class,
          elements = "aaa = 1, bbb = true")
    })
public interface MultipleAnnotateWithDao {

  @Insert
  int insert(Emp emp);
}
