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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * @author nakamura-to
 * @param <BASIC> 基本型
 * @param <CONTAINER> 基本型のコンテナ
 * @param <RESULT> 結果
 */
public class ScalarStreamHandler<BASIC, CONTAINER, RESULT>
    extends AbstractStreamHandler<CONTAINER, RESULT> {

  protected final Supplier<Scalar<BASIC, CONTAINER>> supplier;

  public ScalarStreamHandler(
      Supplier<Scalar<BASIC, CONTAINER>> supplier, Function<Stream<CONTAINER>, RESULT> mapper) {
    super(mapper);
    assertNotNull(supplier);
    this.supplier = supplier;
  }

  @Override
  protected ScalarProvider<BASIC, CONTAINER> createObjectProvider(SelectQuery query) {
    return new ScalarProvider<>(supplier, query);
  }
}
