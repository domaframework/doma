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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.def.TypeParametersDef;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyNameCollector;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.message.Message;

abstract class AbstractQueryMetaFactory<M extends AbstractQueryMeta> implements QueryMetaFactory {

  final RoundContext ctx;

  final TypeElement daoElement;

  final ExecutableElement methodElement;

  AbstractQueryMetaFactory(
      RoundContext ctx, TypeElement daoElement, ExecutableElement methodElement) {
    assertNotNull(ctx, daoElement, methodElement);
    this.ctx = ctx;
    this.daoElement = daoElement;
    this.methodElement = methodElement;
  }

  void doTypeParameters(M queryMeta) {
    TypeParametersDef typeParametersDef = ctx.getMoreElements().getTypeParametersDef(methodElement);
    queryMeta.setTypeParametersDef(typeParametersDef);
  }

  protected abstract void doReturnType(M queryMeta);

  protected abstract void doParameters(M queryMeta);

  void doThrowTypes(M queryMeta) {
    methodElement.getThrownTypes().forEach(queryMeta::addThrownType);
  }

  void validateEntityPropertyNames(
      TypeMirror entityType,
      AnnotationMirror annotationMirror,
      AnnotationValue includeValue,
      AnnotationValue excludeValue,
      AnnotationValue duplicateKeysValue) {
    List<String> includedPropertyNames = AnnotationValueUtil.toStringList(includeValue);
    if (includedPropertyNames == null) {
      includedPropertyNames = Collections.emptyList();
    }
    List<String> excludedPropertyNames = AnnotationValueUtil.toStringList(excludeValue);
    if (excludedPropertyNames == null) {
      excludedPropertyNames = Collections.emptyList();
    }
    List<String> duplicateKeys = AnnotationValueUtil.toStringList(duplicateKeysValue);
    if (duplicateKeys == null) {
      duplicateKeys = Collections.emptyList();
    }
    if (!includedPropertyNames.isEmpty()
        || !excludedPropertyNames.isEmpty()
        || !duplicateKeys.isEmpty()) {
      EntityPropertyNameCollector collector = new EntityPropertyNameCollector(ctx);
      Set<String> names = collector.collect(entityType);
      for (String included : includedPropertyNames) {
        if (!names.contains(included)) {
          throw new AptException(
              Message.DOMA4084,
              methodElement,
              annotationMirror,
              includeValue,
              new Object[] {included, entityType});
        }
      }
      for (String excluded : excludedPropertyNames) {
        if (!names.contains(excluded)) {
          throw new AptException(
              Message.DOMA4085,
              methodElement,
              annotationMirror,
              excludeValue,
              new Object[] {excluded, entityType});
        }
      }
      for (String duplicateKey : duplicateKeys) {
        if (!names.contains(duplicateKey)) {
          throw new AptException(
              Message.DOMA4462,
              methodElement,
              annotationMirror,
              duplicateKeysValue,
              new Object[] {duplicateKey, entityType});
        }
      }
    }
  }

  QueryReturnMeta createReturnMeta(QueryMeta queryMeta) {
    QueryReturnMetaFactory factory = new QueryReturnMetaFactory(ctx, queryMeta);
    return factory.createQueryReturnMeta();
  }

  QueryParameterMeta createParameterMeta(VariableElement parameter) {
    QueryParameterMetaFactory factory = new QueryParameterMetaFactory(ctx, parameter);
    return factory.createQueryParameterMeta();
  }
}
