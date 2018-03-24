package org.seasar.doma.internal.apt.generator;

import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;

final class CodeHelper {

    static String box(String name) {
        if (boolean.class.getName().equals(name)) {
            return Boolean.class.getName();
        }
        if (char.class.getName().equals(name)) {
            return Character.class.getName();
        }
        if (byte.class.getName().equals(name)) {
            return Byte.class.getName();
        }
        if (short.class.getName().equals(name)) {
            return Short.class.getName();
        }
        if (int.class.getName().equals(name)) {
            return Integer.class.getName();
        }
        if (long.class.getName().equals(name)) {
            return Long.class.getName();
        }
        if (float.class.getName().equals(name)) {
            return Float.class.getName();
        }
        if (double.class.getName().equals(name)) {
            return Double.class.getName();
        }
        return name;
    }

    static String wrapperSupplier(BasicCtType ctType) {
        if (ctType.isEnum()) {
            return String.format("() -> new %1$s(%2$s.class)",
                    /* 1 */ctType.getWrapperTypeName(),
                    /* 2 */ctType.getQualifiedName());
        }
        return String.format("%1$s::new", ctType.getWrapperTypeName());
    }

    static String entityDesc(EntityCtType ctType) {
        return ctType.getDescClassName() + ".getSingletonInternal()";
    }

    static String embeddableDesc(EmbeddableCtType ctType) {
        return ctType.getDescClassName() + ".getSingletonInternal()";
    }

}
