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
import java.util.stream.Stream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class StreamCtType extends AbstractCtType {

    protected TypeMirror elementTypeMirror;

    protected CtType elementCtType;

    public StreamCtType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public CtType getElementCtType() {
        return elementCtType;
    }

    public boolean isRawType() {
        return elementTypeMirror == null;
    }

    public boolean isWildcardType() {
        return elementTypeMirror != null
                && elementTypeMirror.getKind() == TypeKind.WILDCARD;
    }

    public static StreamCtType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        if (!TypeMirrorUtil.isSameType(type, Stream.class, env)) {
            return null;
        }
        StreamCtType streamCtType = new StreamCtType(type, env);
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(type, env);
        if (declaredType == null) {
            return null;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() > 0) {
            streamCtType.elementTypeMirror = typeArgs.get(0);
            streamCtType.elementCtType = EntityCtType.newInstance(
                    streamCtType.elementTypeMirror, env);
            if (streamCtType.elementCtType == null) {
                streamCtType.elementCtType = OptionalCtType.newInstance(
                        streamCtType.elementTypeMirror, env);
                if (streamCtType.elementCtType == null) {
                    streamCtType.elementCtType = DomainCtType.newInstance(
                            streamCtType.elementTypeMirror, env);
                    if (streamCtType.elementCtType == null) {
                        streamCtType.elementCtType = BasicCtType.newInstance(
                                streamCtType.elementTypeMirror, env);
                        if (streamCtType.elementCtType == null) {
                            streamCtType.elementCtType = MapCtType.newInstance(
                                    streamCtType.elementTypeMirror, env);
                            if (streamCtType.elementCtType == null) {
                                streamCtType.elementCtType = AnyCtType
                                        .newInstance(
                                                streamCtType.elementTypeMirror,
                                                env);
                            }
                        }
                    }
                }
            }
        }
        return streamCtType;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitStreamCtType(this, p);
    }
}
