package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;

import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.wrapper.ArrayWrapper;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.BigIntegerWrapper;
import org.seasar.doma.wrapper.BlobWrapper;
import org.seasar.doma.wrapper.BooleanWrapper;
import org.seasar.doma.wrapper.ByteWrapper;
import org.seasar.doma.wrapper.BytesWrapper;
import org.seasar.doma.wrapper.ClobWrapper;
import org.seasar.doma.wrapper.DateWrapper;
import org.seasar.doma.wrapper.DoubleWrapper;
import org.seasar.doma.wrapper.FloatWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.NClobWrapper;
import org.seasar.doma.wrapper.ShortWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;

public final class DomaTypes {

    public static boolean isSupportedType(TypeMirror typeMirror,
            ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
        // if (TypeUtil.isAssignable(typeMirror, Collection.class, env)) {
        // DeclaredType declaredType = TypeUtil
        // .toDeclaredType(typeMirror, env);
        // if (declaredType.getTypeArguments().isEmpty()) {
        // return false;
        // }
        // TypeMirror elementType = declaredType.getTypeArguments().get(0);
        // TypeMirror wrapperType = getWrapperType(elementType, env);
        // return wrapperType != null;
        // }
        TypeMirror wrapperType = getWrapperType(typeMirror, env);
        return wrapperType != null;
    }

    public static TypeMirror getWrapperType(TypeMirror typeMirror,
            ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
        Class<?> wrapperClass = typeMirror.accept(
                new WrapperTypeMappingVisitor(env), null);
        if (wrapperClass == null) {
            return null;
        }
        TypeElement wrapperTypeElement = env.getElementUtils().getTypeElement(
                wrapperClass.getName());
        if (wrapperTypeElement == null) {
            return null;
        }
        return wrapperTypeElement.asType();
    }

    protected static class WrapperTypeMappingVisitor extends
            SimpleTypeVisitor6<Class<?>, Void> {

        protected final ProcessingEnvironment env;

        protected WrapperTypeMappingVisitor(ProcessingEnvironment env) {
            this.env = env;
        }

        @Override
        public Class<?> visitArray(ArrayType t, Void p) {
            if (t.getComponentType().getKind() == TypeKind.BYTE) {
                return BytesWrapper.class;
            }
            return null;
        }

        @Override
        public Class<?> visitDeclared(DeclaredType t, Void p) {
            TypeElement typeElement = TypeUtil.toTypeElement(t, env);
            if (typeElement == null) {
                return null;
            }
            String name = typeElement.getQualifiedName().toString();
            if (String.class.getName().equals(name)) {
                return StringWrapper.class;
            }
            if (Boolean.class.getName().equals(name)) {
                return BooleanWrapper.class;
            }
            if (Byte.class.getName().equals(name)) {
                return ByteWrapper.class;
            }
            if (Short.class.getName().equals(name)) {
                return ShortWrapper.class;
            }
            if (Integer.class.getName().equals(name)) {
                return IntegerWrapper.class;
            }
            if (Long.class.getName().equals(name)) {
                return LongWrapper.class;
            }
            if (Float.class.getName().equals(name)) {
                return FloatWrapper.class;
            }
            if (Double.class.getName().equals(name)) {
                return DoubleWrapper.class;
            }
            if (TypeUtil.isAssignable(t, BigDecimal.class, env)) {
                return BigDecimalWrapper.class;
            }
            if (TypeUtil.isAssignable(t, BigInteger.class, env)) {
                return BigIntegerWrapper.class;
            }
            if (TypeUtil.isAssignable(t, Time.class, env)) {
                return TimeWrapper.class;
            }
            if (TypeUtil.isAssignable(t, Timestamp.class, env)) {
                return TimestampWrapper.class;
            }
            if (TypeUtil.isAssignable(t, Date.class, env)) {
                return DateWrapper.class;
            }
            if (TypeUtil.isAssignable(t, Array.class, env)) {
                return ArrayWrapper.class;
            }
            if (TypeUtil.isAssignable(t, Blob.class, env)) {
                return BlobWrapper.class;
            }
            if (TypeUtil.isAssignable(t, NClob.class, env)) {
                return NClobWrapper.class;
            }
            if (TypeUtil.isAssignable(t, Clob.class, env)) {
                return ClobWrapper.class;
            }
            return null;
        }

        @Override
        public Class<?> visitPrimitive(PrimitiveType t, Void p) {
            switch (t.getKind()) {
            case BOOLEAN:
                return BooleanWrapper.class;
            case BYTE:
                return ByteWrapper.class;
            case SHORT:
                return ShortWrapper.class;
            case INT:
                return IntegerWrapper.class;
            case LONG:
                return LongWrapper.class;
            case FLOAT:
                return FloatWrapper.class;
            case DOUBLE:
                return DoubleWrapper.class;
            case CHAR:
                return null;
            }
            assertUnreachable();
            return null;
        }

    }
}
