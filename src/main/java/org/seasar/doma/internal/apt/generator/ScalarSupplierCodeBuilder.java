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
package org.seasar.doma.internal.apt.generator;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

class ScalarSupplierCodeBuilder extends SimpleCtTypeVisitor<String, Boolean, RuntimeException> {

    @Override
    protected String defaultAction(CtType ctType, Boolean p) throws RuntimeException {
        throw new AptIllegalStateException("illegalState: " + ctType.getQualifiedName());
    }

    @Override
    public String visitOptionalCtType(OptionalCtType ctType, Boolean p) throws RuntimeException {
        return ctType.getElementCtType().accept(this, true);
    }

    @Override
    public String visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p)
            throws RuntimeException {
        return String.format("() -> new %1$s()", OptionalIntScalar.class.getName());
    }

    @Override
    public String visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p)
            throws RuntimeException {
        return String.format("() -> new %1$s()", OptionalLongScalar.class.getName());
    }

    @Override
    public String visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p)
            throws RuntimeException {
        return String.format("() -> new %1$s()", OptionalDoubleScalar.class.getName());
    }

    @Override
    public String visitBasicCtType(BasicCtType ctType, Boolean p) throws RuntimeException {
        if (p) {
            return String.format("() -> new %1$s<>(%2$s)", OptionalBasicScalar.class.getName(),
                    wrapper(ctType));
        }
        return String.format("() -> new %1$s<>(%2$s, %3$s)", BasicScalar.class.getName(),
                wrapper(ctType), ctType.isPrimitive());
    }

    @Override
    public String visitHolderCtType(HolderCtType ctType, Boolean p) throws RuntimeException {
        if (p) {
            return String.format("() -> %1$s.createOptionalScalar()", holderDesc(ctType));
        }
        return String.format("() -> %1$s.createScalar()", holderDesc(ctType));
    }

    private static String wrapper(BasicCtType ctType) {
        if (ctType.isEnum()) {
            return String.format("new %1$s(%2$s.class)", ctType.getWrapperTypeName(),
                    ctType.getQualifiedName());
        }
        return String.format("new %1$s()", ctType.getWrapperTypeName());
    }

    private static String holderDesc(HolderCtType ctType) {
        int pos = ctType.getTypeName().indexOf('<');
        String typeArgDecl = "";
        if (pos > -1) {
            typeArgDecl = ctType.getTypeName().substring(pos);
        }
        return ctType.getDescClassName() + "." + typeArgDecl + "getSingletonInternal()";
    }

}
