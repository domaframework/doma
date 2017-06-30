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

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author taedium
 * 
 */
public interface CtType {

    TypeMirror getType();

    TypeElement getTypeElement();

    String getTypeName();

    String getQualifiedName();

    boolean isRawType();

    boolean hasWildcardType();

    boolean hasTypevarType();

    <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p)
            throws TH;
}
