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
package org.seasar.doma.internal.jdbc.command;

import java.util.function.Supplier;
import java.util.stream.Collector;
import org.seasar.doma.internal.jdbc.scalar.Scalar;

/**
 * @author nakamura-to
 * @param <BASIC> 基本型
 * @param <CONTAINER> 基本型のコンテナ
 * @param <RESULT> 結果
 */
public class ScalarCollectorHandler<BASIC, CONTAINER, RESULT>
    extends AbstractCollectorHandler<CONTAINER, RESULT> {

  public ScalarCollectorHandler(
      Supplier<Scalar<BASIC, CONTAINER>> supplier, Collector<CONTAINER, ?, RESULT> collector) {
    super(new ScalarStreamHandler<>(supplier, s -> s.collect(collector)));
  }
}
