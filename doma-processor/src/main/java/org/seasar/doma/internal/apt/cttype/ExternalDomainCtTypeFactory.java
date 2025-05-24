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
package org.seasar.doma.internal.apt.cttype;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.AptIllegalOptionException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMeta;
import org.seasar.doma.jdbc.domain.DomainConverter;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.message.Message;

public class ExternalDomainCtTypeFactory {
  private final RoundContext ctx;

  public ExternalDomainCtTypeFactory(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
  }

  DomainCtType newDomainCtType(TypeMirror type) {
    var valueType = getValueType(type);
    if (valueType == null) {
      return null;
    }
    var basicCtType = ctx.getCtTypes().newBasicCtType(valueType);
    if (basicCtType == null) {
      return null;
    }
    if (type.getKind() == TypeKind.ARRAY) {
      return newDomainCtType(type, basicCtType, Collections.emptyList());
    } else {
      var typeArgCtTypes = getTypeArgCtTypes(type);
      if (typeArgCtTypes == null) {
        return null;
      }
      return newDomainCtType(type, basicCtType, typeArgCtTypes);
    }
  }

  private TypeMirror getValueType(TypeMirror domainType) {
    var valueType = getValueTypeFromOptions(domainType);
    if (valueType != null) {
      return valueType;
    }
    valueType = getValueTypeFromRoundContext(domainType);
    if (valueType != null) {
      return valueType;
    }
    return getValueTypeFromCompiledMetadata(domainType);
  }

  private TypeMirror getValueTypeFromOptions(TypeMirror domainType) {
    var csv = ctx.getOptions().getDomainConverters();
    if (csv != null) {
      for (String value : csv.split(",")) {
        var className = value.trim();
        if (className.isEmpty()) {
          continue;
        }
        var convertersProviderElement =
            ctx.getMoreElements().getTypeElementFromBinaryName(className);
        if (convertersProviderElement == null) {
          throw new AptIllegalOptionException(Message.DOMA4200.getMessage(className));
        }
        var convertersMirror =
            ctx.getAnnotations().newDomainConvertersAnnot(convertersProviderElement);
        if (convertersMirror == null) {
          throw new AptIllegalOptionException(Message.DOMA4201.getMessage(className));
        }
        for (var converterType : convertersMirror.getValueValue()) {
          // converterType does not contain adequate information in
          // eclipse incremental compile, so reload type
          converterType = reloadTypeMirror(converterType);
          if (converterType == null) {
            continue;
          }
          var argTypes = getConverterArgTypes(converterType);
          if (argTypes == null
              || !ctx.getMoreTypes().isSameTypeWithErasure(domainType, argTypes[0])) {
            continue;
          }
          return argTypes[1];
        }
      }
    }
    return null;
  }

  private TypeMirror reloadTypeMirror(TypeMirror typeMirror) {
    var typeElement = ctx.getMoreTypes().toTypeElement(typeMirror);
    if (typeElement == null) {
      return null;
    }
    typeElement = ctx.getMoreElements().getTypeElement(typeElement.getQualifiedName());
    if (typeElement == null) {
      return null;
    }
    return typeElement.asType();
  }

  private TypeMirror[] getConverterArgTypes(TypeMirror typeMirror) {
    for (var supertype : ctx.getMoreTypes().directSupertypes(typeMirror)) {
      if (!ctx.getMoreTypes().isAssignableWithErasure(supertype, DomainConverter.class)) {
        continue;
      }
      if (ctx.getMoreTypes().isSameTypeWithErasure(supertype, DomainConverter.class)) {
        var declaredType = ctx.getMoreTypes().toDeclaredType(supertype);
        assertNotNull(declaredType);
        var args = declaredType.getTypeArguments();
        assertEquals(2, args.size());
        return new TypeMirror[] {args.get(0), args.get(1)};
      }
      var argTypes = getConverterArgTypes(supertype);
      if (argTypes != null) {
        return argTypes;
      }
    }
    return null;
  }

  private TypeMirror getValueTypeFromRoundContext(TypeMirror domainType) {
    return ctx.getExternalDomainMetaList().stream()
        .filter(it -> ctx.getMoreTypes().isSameTypeWithErasure(it.asType(), domainType))
        .findFirst()
        .map(ExternalDomainMeta::getValueType)
        .orElse(null);
  }

  private TypeMirror getValueTypeFromCompiledMetadata(TypeMirror domainType) {
    var domainTypeElement = ctx.getMoreTypes().toTypeElement(domainType);
    if (domainTypeElement == null) {
      return null;
    }
    var className = ClassNames.newExternalDomainTypeClassName(domainTypeElement.getQualifiedName());
    var externalDomainMetadataElement = ctx.getMoreElements().getTypeElement(className);
    if (externalDomainMetadataElement == null) {
      return null;
    }
    var argTypes = getDomainTypeArgTypes(externalDomainMetadataElement.asType());
    if (argTypes == null) {
      return null;
    }
    return argTypes[0];
  }

  private TypeMirror[] getDomainTypeArgTypes(TypeMirror typeMirror) {
    for (var supertype : ctx.getMoreTypes().directSupertypes(typeMirror)) {
      if (!ctx.getMoreTypes().isAssignableWithErasure(supertype, DomainType.class)) {
        continue;
      }
      if (ctx.getMoreTypes().isSameTypeWithErasure(supertype, DomainType.class)) {
        var declaredType = ctx.getMoreTypes().toDeclaredType(supertype);
        assertNotNull(declaredType);
        var args = declaredType.getTypeArguments();
        assertEquals(2, args.size());
        return new TypeMirror[] {args.get(0), args.get(1)};
      }
      var argTypes = getDomainTypeArgTypes(supertype);
      if (argTypes != null) {
        return argTypes;
      }
    }
    return null;
  }

  private List<CtType> getTypeArgCtTypes(TypeMirror type) {
    var typeElement = ctx.getMoreTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    var declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    var typeArgs = declaredType.getTypeArguments().iterator();
    return typeElement.getTypeParameters().stream()
        .map(
            __ ->
                typeArgs.hasNext()
                    ? ctx.getCtTypes().newCtType(typeArgs.next())
                    : ctx.getCtTypes().newNoneCtType())
        .collect(toList());
  }

  private DomainCtType newDomainCtType(
      TypeMirror type, BasicCtType typeElement, List<CtType> typeArgCtTypes) {
    var name = ctx.getNames().createExternalDomainName(type);
    var typeClassName = ClassNames.newExternalDomainTypeClassName(name);
    return new DomainCtType(ctx, type, typeElement, typeArgCtTypes, typeClassName);
  }
}
