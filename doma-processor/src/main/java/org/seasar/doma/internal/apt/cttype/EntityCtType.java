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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.generator.Code;

public class EntityCtType extends AbstractCtType {

  private final boolean immutable;

  private final ClassName typeClassName;

  EntityCtType(RoundContext ctx, TypeMirror type, boolean immutable, ClassName typeClassName) {
    super(ctx, type);
    assertNotNull(typeClassName);
    this.immutable = immutable;
    this.typeClassName = typeClassName;
  }

  public boolean isImmutable() {
    return immutable;
  }

  public boolean isAbstract() {
    return typeElement.getModifiers().contains(Modifier.ABSTRACT);
  }

  public Code getTypeCode() {
    return new Code(p -> p.print("%1$s.getSingletonInternal()", typeClassName));
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEntityCtType(this, p);
  }

  /**
   * Resolves an {@code EntityCtType} from the given {@code CtType}. This method traverses the type
   * hierarchy of the provided {@code CtType} and identifies if it corresponds to an {@code
   * EntityCtType}. It also handles nested and optional types.
   *
   * @param ctType the {@code CtType} instance to resolve into an {@code EntityCtType}.
   * @return the resolved {@code EntityCtType} if found; otherwise, null.
   */
  public static EntityCtType resolveEntityCtType(CtType ctType) {
    class EntityCtTypeVisitor extends SimpleCtTypeVisitor<EntityCtType, Void, RuntimeException> {
      @Override
      protected EntityCtType defaultAction(CtType ctType, Void p) throws RuntimeException {
        return null;
      }

      @Override
      public EntityCtType visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
        return ctType;
      }
    }

    return ctType.accept(
        new EntityCtTypeVisitor() {

          @Override
          public EntityCtType visitIterableCtType(IterableCtType ctType, Void unused)
              throws RuntimeException {
            return ctType
                .getElementCtType()
                .accept(
                    new EntityCtTypeVisitor() {
                      @Override
                      public EntityCtType visitOptionalCtType(OptionalCtType ctType, Void unused)
                          throws RuntimeException {
                        return ctType.getElementCtType().accept(new EntityCtTypeVisitor(), null);
                      }
                    },
                    null);
          }

          @Override
          public EntityCtType visitOptionalCtType(OptionalCtType ctType, Void unused)
              throws RuntimeException {
            return ctType.getElementCtType().accept(new EntityCtTypeVisitor(), null);
          }
        },
        null);
  }
}
