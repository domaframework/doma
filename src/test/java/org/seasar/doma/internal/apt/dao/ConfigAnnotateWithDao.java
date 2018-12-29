package org.seasar.doma.internal.apt.dao;

import example.dao.ExampleConfig;
import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.entity.Emp;

@Dao(config = ExampleConfig.class)
@AnnotateWith(
    annotations = {
      @Annotation(
          target = AnnotationTarget.CLASS,
          type = ClassAnnotation.class,
          elements = "aaa = 1, bbb = true")
    })
public interface ConfigAnnotateWithDao {

  @Insert
  int insert(Emp emp);
}
