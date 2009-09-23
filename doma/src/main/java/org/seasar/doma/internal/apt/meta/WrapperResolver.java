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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

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

public final class WrapperResolver {

    private static final Map<String, String> NAME_MAP = new HashMap<String, String>();
    static {
        NAME_MAP.put(Array.class.getName(), ArrayWrapper.class.getName());
        NAME_MAP.put(BigDecimal.class.getName(), BigDecimalWrapper.class
                .getName());
        NAME_MAP.put(BigInteger.class.getName(), BigIntegerWrapper.class
                .getName());
        NAME_MAP.put(Blob.class.getName(), BlobWrapper.class.getName());
        NAME_MAP.put(Boolean.class.getName(), BooleanWrapper.class.getName());
        NAME_MAP.put(byte[].class.getName(), BytesWrapper.class.getName());
        NAME_MAP.put(Byte.class.getName(), ByteWrapper.class.getName());
        NAME_MAP.put(Clob.class.getName(), ClobWrapper.class.getName());
        NAME_MAP.put(Date.class.getName(), DateWrapper.class.getName());
        NAME_MAP.put(Double.class.getName(), DoubleWrapper.class.getName());
        NAME_MAP.put(Float.class.getName(), FloatWrapper.class.getName());
        NAME_MAP.put(Integer.class.getName(), IntegerWrapper.class.getName());
        NAME_MAP.put(Long.class.getName(), LongWrapper.class.getName());
        NAME_MAP.put(NClob.class.getName(), NClobWrapper.class.getName());
        NAME_MAP.put(Short.class.getName(), ShortWrapper.class.getName());
        NAME_MAP.put(String.class.getName(), StringWrapper.class.getName());
        NAME_MAP.put(Timestamp.class.getName(), TimestampWrapper.class
                .getName());
        NAME_MAP.put(Time.class.getName(), TimeWrapper.class.getName());
    }

    public static DeclaredType getWrapperType(DeclaredType wrappedType,
            ProcessingEnvironment env) {
        assertNotNull(wrappedType, env);
        TypeElement wrappedTypeElement = TypeUtil.toTypeElement(wrappedType,
                env);
        if (wrappedTypeElement == null) {
            return null;
        }
        String wrapperTypeName = NAME_MAP.get(wrappedTypeElement
                .getQualifiedName().toString());
        if (wrapperTypeName == null) {
            return null;
        }
        TypeElement wrapperTypeElement = env.getElementUtils().getTypeElement(
                wrapperTypeName);
        if (wrapperTypeElement == null) {
            return null;
        }
        List<? extends TypeMirror> typeArgList = wrappedType.getTypeArguments();
        TypeMirror[] typeArgArray = new TypeMirror[typeArgList.size()];
        for (int i = 0; i < typeArgList.size(); i++) {
            typeArgArray[i] = typeArgList.get(0);
        }
        DeclaredType wrapperType = env.getTypeUtils().getDeclaredType(
                wrapperTypeElement, typeArgArray);
        return wrapperType;
    }
}
