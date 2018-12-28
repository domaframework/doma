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

import java.util.Optional;
import java.util.function.Function;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;

/** @author nakamura-to */
public class OptionalSingleResultCallback<TARGET>
    implements IterationCallback<TARGET, Optional<TARGET>> {

  protected final Function<TARGET, Optional<TARGET>> mapper;

  public OptionalSingleResultCallback() {
    this(Optional::ofNullable);
  }

  public OptionalSingleResultCallback(Function<TARGET, Optional<TARGET>> mapper) {
    assertNotNull(mapper);
    this.mapper = mapper;
  }

  @Override
  public Optional<TARGET> defaultResult() {
    return mapper.apply(null);
  }

  @Override
  public Optional<TARGET> iterate(TARGET target, IterationContext context) {
    context.exit();
    return mapper.apply(target);
  }
}
