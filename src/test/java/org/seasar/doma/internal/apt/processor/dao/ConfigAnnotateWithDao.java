package org.seasar.doma.internal.apt.processor.dao;

import example.dao.ExampleConfig;
import org.seasar.doma.*;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao(config = ExampleConfig.class)
@AnnotateWith(
  annotations = {
    @Annotation(
      target = AnnotationTarget.CLASS,
      type = ClassAnnotation.class,
      elements = "aaa = 1, bbb = true"
    )
  }
)
public interface ConfigAnnotateWithDao {

  @Insert
  int insert(Emp emp);
}
