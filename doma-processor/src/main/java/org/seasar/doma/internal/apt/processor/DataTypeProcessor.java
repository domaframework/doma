/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.processor;

import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import org.seasar.doma.DataType;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.meta.domain.DataTypeMeta;
import org.seasar.doma.internal.apt.meta.domain.DataTypeMetaFactory;

public class DataTypeProcessor implements ElementProcessor<DataTypeMeta> {

  private final DomainProcessorSupport<DataTypeMeta> support;

  public DataTypeProcessor(RoundContext ctx) {
    support = new DomainProcessorSupport<>(ctx, DataType.class, new DataTypeMetaFactory(ctx));
  }

  @Override
  public List<DataTypeMeta> process(Set<? extends Element> elements) {
    return support.process(elements);
  }
}
