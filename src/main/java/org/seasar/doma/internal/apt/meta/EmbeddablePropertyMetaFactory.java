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
package org.seasar.doma.internal.apt.meta;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.mirror.ColumnMirror;
import org.seasar.doma.message.Message;

/** @author nakamura-to */
public class EmbeddablePropertyMetaFactory {

  protected final ProcessingEnvironment env;

  public EmbeddablePropertyMetaFactory(ProcessingEnvironment env) {
    this.env = env;
  }

  public EmbeddablePropertyMeta createEmbeddablePropertyMeta(
      VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
    EmbeddablePropertyMeta embeddablePropertyMeta = new EmbeddablePropertyMeta(fieldElement, env);
    embeddablePropertyMeta.setName(fieldElement.getSimpleName().toString());
    CtType ctType = resolveCtType(fieldElement, fieldElement.asType(), embeddableMeta);
    embeddablePropertyMeta.setCtType(ctType);
    ColumnMirror columnMirror = ColumnMirror.newInstance(fieldElement, env);
    if (columnMirror != null) {
      embeddablePropertyMeta.setColumnMirror(columnMirror);
    }
    return embeddablePropertyMeta;
  }

  protected CtType resolveCtType(
      VariableElement fieldElement, TypeMirror type, EmbeddableMeta embeddableMeta) {

    final OptionalCtType optionalCtType = OptionalCtType.newInstance(type, env);
    if (optionalCtType != null) {
      if (optionalCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4299,
            env,
            fieldElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      if (optionalCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4301,
            env,
            fieldElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      return optionalCtType;
    }

    OptionalIntCtType optionalIntCtType = OptionalIntCtType.newInstance(type, env);
    if (optionalIntCtType != null) {
      return optionalIntCtType;
    }

    OptionalLongCtType optionalLongCtType = OptionalLongCtType.newInstance(type, env);
    if (optionalLongCtType != null) {
      return optionalLongCtType;
    }

    OptionalDoubleCtType optionalDoubleCtType = OptionalDoubleCtType.newInstance(type, env);
    if (optionalDoubleCtType != null) {
      return optionalDoubleCtType;
    }

    final DomainCtType domainCtType = DomainCtType.newInstance(type, env);
    if (domainCtType != null) {
      if (domainCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4295,
            env,
            fieldElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      if (domainCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4296,
            env,
            fieldElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      return domainCtType;
    }

    BasicCtType basicCtType = BasicCtType.newInstance(type, env);
    if (basicCtType != null) {
      return basicCtType;
    }

    final EmbeddableCtType embeddableCtType = EmbeddableCtType.newInstance(type, env);
    if (embeddableCtType != null) {
      throw new AptException(
          Message.DOMA4297,
          env,
          fieldElement,
          new Object[] {
            type,
            embeddableMeta.getEmbeddableElement().getQualifiedName(),
            fieldElement.getSimpleName()
          });
    }

    throw new AptException(
        Message.DOMA4298,
        env,
        fieldElement,
        new Object[] {
          type,
          embeddableMeta.getEmbeddableElement().getQualifiedName(),
          fieldElement.getSimpleName()
        });
  }
}
