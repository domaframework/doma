package org.seasar.doma.jdbc.builder;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNull;

import java.util.ArrayList;
import java.util.List;

/** @author bakenezumi */
class BatchParam<P> {

  final String name;

  final Class<P> paramClass;

  final List<P> params = new ArrayList<>();

  final boolean literal;

  BatchParam(Class<P> paramClass, ParamIndex index, boolean literal) {
    this.paramClass = paramClass;
    this.name = "p" + index.getValue();
    this.literal = literal;
  }

  BatchParam(BatchParam<?> baseParam, Class<P> paramClass) {
    assertEquals(Object.class, baseParam.paramClass);
    this.name = baseParam.name;
    this.paramClass = paramClass;
    this.literal = baseParam.literal;
    baseParam.params.forEach(
        e -> {
          assertNull(e);
          params.add(null);
        });
  }

  void add(P param) {
    params.add(param);
  }
}
