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
module org.seasar.doma.processor {
  requires java.compiler;
  requires org.seasar.doma.core;

  provides javax.annotation.processing.Processor with
      org.seasar.doma.internal.apt.processor.DomainProcessor,
      org.seasar.doma.internal.apt.processor.DataTypeProcessor,
      org.seasar.doma.internal.apt.processor.ExternalDomainProcessor,
      org.seasar.doma.internal.apt.processor.DomainConvertersProcessor,
      org.seasar.doma.internal.apt.processor.EmbeddableProcessor,
      org.seasar.doma.internal.apt.processor.EntityProcessor,
      org.seasar.doma.internal.apt.processor.DaoProcessor,
      org.seasar.doma.internal.apt.processor.ScopeProcessor;
}
