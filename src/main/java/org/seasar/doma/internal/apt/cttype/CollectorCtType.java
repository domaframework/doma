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
package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collector;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class CollectorCtType extends AbstractCtType {

  protected CtType targetCtType;

  protected AnyCtType returnCtType;

  public CollectorCtType(TypeMirror type, ProcessingEnvironment env) {
    super(type, env);
  }

  public CtType getTargetCtType() {
    return targetCtType;
  }

  public AnyCtType getReturnCtType() {
    return returnCtType;
  }

  public boolean isRawType() {
    return returnCtType.getTypeMirror() == null || targetCtType.getTypeMirror() == null;
  }

  public boolean isWildcardType() {
    return returnCtType.getTypeMirror() != null
            && returnCtType.getTypeMirror().getKind() == TypeKind.WILDCARD
        || targetCtType.getTypeMirror() != null
            && targetCtType.getTypeMirror().getKind() == TypeKind.WILDCARD;
  }

  public static CollectorCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    DeclaredType collectorDeclaredType = getCollectorDeclaredType(type, env);
    if (collectorDeclaredType == null) {
      return null;
    }

    CollectorCtType collectorCtType = new CollectorCtType(type, env);
    List<? extends TypeMirror> typeArguments = collectorDeclaredType.getTypeArguments();
    if (typeArguments.size() == 3) {
      TypeMirror targetTypeMirror = typeArguments.get(0);
      TypeMirror returnTypeMirror = typeArguments.get(2);
      collectorCtType.targetCtType =
          buildCtTypeSuppliers(targetTypeMirror, env)
              .stream()
              .map(Supplier::get)
              .filter(Objects::nonNull)
              .findFirst()
              .get();
      collectorCtType.returnCtType = AnyCtType.newInstance(returnTypeMirror, env);
    }
    return collectorCtType;
  }

  protected static DeclaredType getCollectorDeclaredType(
      TypeMirror type, ProcessingEnvironment env) {
    if (TypeMirrorUtil.isSameType(type, Collector.class, env)) {
      return TypeMirrorUtil.toDeclaredType(type, env);
    }
    for (TypeMirror supertype : env.getTypeUtils().directSupertypes(type)) {
      if (TypeMirrorUtil.isSameType(supertype, Collector.class, env)) {
        return TypeMirrorUtil.toDeclaredType(supertype, env);
      }
      DeclaredType result = getCollectorDeclaredType(supertype, env);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  protected static List<Supplier<CtType>> buildCtTypeSuppliers(
      TypeMirror typeMirror, ProcessingEnvironment env) {
    return Arrays.<Supplier<CtType>>asList(
        () -> EntityCtType.newInstance(typeMirror, env),
        () -> OptionalCtType.newInstance(typeMirror, env),
        () -> OptionalIntCtType.newInstance(typeMirror, env),
        () -> OptionalLongCtType.newInstance(typeMirror, env),
        () -> OptionalDoubleCtType.newInstance(typeMirror, env),
        () -> DomainCtType.newInstance(typeMirror, env),
        () -> BasicCtType.newInstance(typeMirror, env),
        () -> MapCtType.newInstance(typeMirror, env),
        () -> AnyCtType.newInstance(typeMirror, env));
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitCollectorCtType(this, p);
  }
}
