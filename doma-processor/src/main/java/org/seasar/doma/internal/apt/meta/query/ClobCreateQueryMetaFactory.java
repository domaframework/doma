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
package org.seasar.doma.internal.apt.meta.query;

import java.sql.Clob;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ClobFactoryAnnot;

public class ClobCreateQueryMetaFactory
    extends AbstractCreateQueryMetaFactory<ClobCreateQueryMeta> {

  public ClobCreateQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement, Clob.class);
  }

  @Override
  public QueryMeta createQueryMeta() {
    ClobFactoryAnnot clobFactoryAnnot = ctx.getAnnotations().newClobFactoryAnnot(methodElement);
    if (clobFactoryAnnot == null) {
      return null;
    }
    ClobCreateQueryMeta queryMeta = new ClobCreateQueryMeta(daoElement, methodElement);
    queryMeta.setClobFactoryAnnot(clobFactoryAnnot);
    queryMeta.setQueryKind(QueryKind.CLOB_FACTORY);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }
}
