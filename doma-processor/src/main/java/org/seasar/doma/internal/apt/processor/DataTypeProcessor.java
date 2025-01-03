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

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import org.seasar.doma.DataType;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.meta.domain.DataTypeMeta;
import org.seasar.doma.internal.apt.meta.domain.DataTypeMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.DataType"})
@SupportedOptions({
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.TEST,
  Options.TRACE,
  Options.DEBUG,
  Options.CONFIG_PATH
})
public class DataTypeProcessor extends AbstractDomainProcessor<DataTypeMeta> {

  public DataTypeProcessor() {
    super(DataType.class);
  }

  @Override
  protected DataTypeMetaFactory createTypeElementMetaFactory() {
    return new DataTypeMetaFactory(ctx);
  }
}
