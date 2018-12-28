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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

/** @author taedium */
public class MapCtType extends AbstractCtType {

  public MapCtType(TypeMirror type, ProcessingEnvironment env) {
    super(type, env);
  }

  public static MapCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    if (!TypeMirrorUtil.isSameType(type, Map.class, env)) {
      return null;
    }
    DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(type, env);
    if (declaredType == null) {
      return null;
    }
    List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() != 2) {
      return null;
    }
    if (!TypeMirrorUtil.isSameType(typeArgs.get(0), String.class, env)) {
      return null;
    }
    if (!TypeMirrorUtil.isSameType(typeArgs.get(1), Object.class, env)) {
      return null;
    }
    return new MapCtType(type, env);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitMapCtType(this, p);
  }
}
