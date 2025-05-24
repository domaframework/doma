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

import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor14;
import org.seasar.doma.internal.apt.RoundContext;
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
import org.seasar.doma.wrapper.EnumWrapper;
import org.seasar.doma.wrapper.FloatWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.LocalDateTimeWrapper;
import org.seasar.doma.wrapper.LocalDateWrapper;
import org.seasar.doma.wrapper.LocalTimeWrapper;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.NClobWrapper;
import org.seasar.doma.wrapper.ObjectWrapper;
import org.seasar.doma.wrapper.PrimitiveBooleanWrapper;
import org.seasar.doma.wrapper.PrimitiveByteWrapper;
import org.seasar.doma.wrapper.PrimitiveDoubleWrapper;
import org.seasar.doma.wrapper.PrimitiveFloatWrapper;
import org.seasar.doma.wrapper.PrimitiveIntWrapper;
import org.seasar.doma.wrapper.PrimitiveLongWrapper;
import org.seasar.doma.wrapper.PrimitiveShortWrapper;
import org.seasar.doma.wrapper.SQLXMLWrapper;
import org.seasar.doma.wrapper.ShortWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.UtilDateWrapper;

class BasicCtTypeFactory {

  private final RoundContext ctx;

  BasicCtTypeFactory(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
  }

  BasicCtType newPrimitiveBooleanCtType(TypeMirror type) {
    assertTrue(type.getKind() == TypeKind.BOOLEAN);
    return newBasicCtType(type, PrimitiveBooleanWrapper.class);
  }

  BasicCtType newPrimitiveByteCtType(TypeMirror type) {
    assertTrue(type.getKind() == TypeKind.BYTE);
    return newBasicCtType(type, PrimitiveByteWrapper.class);
  }

  BasicCtType newPrimitiveDoubleCtType(TypeMirror type) {
    assertTrue(type.getKind() == TypeKind.DOUBLE);
    return newBasicCtType(type, PrimitiveDoubleWrapper.class);
  }

  BasicCtType newPrimitiveFloatCtType(TypeMirror type) {
    assertTrue(type.getKind() == TypeKind.FLOAT);
    return newBasicCtType(type, PrimitiveFloatWrapper.class);
  }

  BasicCtType newPrimitiveIntCtType(TypeMirror type) {
    assertTrue(type.getKind() == TypeKind.INT);
    return newBasicCtType(type, PrimitiveIntWrapper.class);
  }

  BasicCtType newPrimitiveLongCtType(TypeMirror type) {
    assertTrue(type.getKind() == TypeKind.LONG);
    return newBasicCtType(type, PrimitiveLongWrapper.class);
  }

  BasicCtType newPrimitiveShortCtType(TypeMirror type) {
    assertTrue(type.getKind() == TypeKind.SHORT);
    return newBasicCtType(type, PrimitiveShortWrapper.class);
  }

  BasicCtType newArrayCtType(TypeMirror type) {
    return newBasicCtType(type, ArrayWrapper.class);
  }

  BasicCtType newBigDecimalCtType(TypeMirror type) {
    return newBasicCtType(type, BigDecimalWrapper.class);
  }

  BasicCtType newBigIntegerCtType(TypeMirror type) {
    return newBasicCtType(type, BigIntegerWrapper.class);
  }

  BasicCtType newBlobCtType(TypeMirror type) {
    return newBasicCtType(type, BlobWrapper.class);
  }

  BasicCtType newBooleanCtType(TypeMirror type) {
    return newBasicCtType(type, BooleanWrapper.class);
  }

  BasicCtType newByteCtType(TypeMirror type) {
    return newBasicCtType(type, ByteWrapper.class);
  }

  BasicCtType newBytesCtType(TypeMirror type) {
    return newBasicCtType(type, BytesWrapper.class);
  }

  BasicCtType newClobCtType(TypeMirror type) {
    return newBasicCtType(type, ClobWrapper.class);
  }

  BasicCtType newDateCtType(TypeMirror type) {
    return newBasicCtType(type, DateWrapper.class);
  }

  BasicCtType newDoubleCtType(TypeMirror type) {
    return newBasicCtType(type, DoubleWrapper.class);
  }

  BasicCtType newEnumCtType(TypeMirror type) {
    return newBasicCtType(type, EnumWrapper.class);
  }

  BasicCtType newFloatCtType(TypeMirror type) {
    return newBasicCtType(type, FloatWrapper.class);
  }

  BasicCtType newIntegerCtType(TypeMirror type) {
    return newBasicCtType(type, IntegerWrapper.class);
  }

  BasicCtType newLocalDateCtType(TypeMirror type) {
    return newBasicCtType(type, LocalDateWrapper.class);
  }

  BasicCtType newLocalDateTimeCtType(TypeMirror type) {
    return newBasicCtType(type, LocalDateTimeWrapper.class);
  }

  BasicCtType newLocalTimeCtType(TypeMirror type) {
    return newBasicCtType(type, LocalTimeWrapper.class);
  }

  BasicCtType newLongCtType(TypeMirror type) {
    return newBasicCtType(type, LongWrapper.class);
  }

  BasicCtType newNClobCtType(TypeMirror type) {
    return newBasicCtType(type, NClobWrapper.class);
  }

  BasicCtType newObjectCtType(TypeMirror type) {
    return newBasicCtType(type, ObjectWrapper.class);
  }

  BasicCtType newShortCtType(TypeMirror type) {
    return newBasicCtType(type, ShortWrapper.class);
  }

  BasicCtType newStringCtType(TypeMirror type) {
    return newBasicCtType(type, StringWrapper.class);
  }

  BasicCtType newSQLXMLCtType(TypeMirror type) {
    return newBasicCtType(type, SQLXMLWrapper.class);
  }

  BasicCtType newTimeCtType(TypeMirror type) {
    return newBasicCtType(type, TimeWrapper.class);
  }

  BasicCtType newTimestampCtType(TypeMirror type) {
    return newBasicCtType(type, TimestampWrapper.class);
  }

  BasicCtType newUtilDateCtType(TypeMirror type) {
    return newBasicCtType(type, UtilDateWrapper.class);
  }

  private BasicCtType newBasicCtType(TypeMirror type, Class<?> wrapperType) {
    var wrapperTypeElement = ctx.getMoreElements().getTypeElement(wrapperType);
    if (wrapperTypeElement == null) {
      return null;
    }
    return new BasicCtType(ctx, type, wrapperTypeElement);
  }

  BasicCtType newCtType(TypeMirror type) {
    return type.accept(new BasicCtTypeResolver(), null);
  }

  private class BasicCtTypeResolver extends SimpleTypeVisitor14<BasicCtType, Void> {

    @Override
    public BasicCtType visitArray(ArrayType t, Void p) {
      if (t.getComponentType().getKind() == TypeKind.BYTE) {
        return newBytesCtType(t);
      }
      return null;
    }

    @Override
    public BasicCtType visitDeclared(DeclaredType t, Void p) {
      TypeElement typeElement = ctx.getMoreTypes().toTypeElement(t);
      if (typeElement == null) {
        return null;
      }
      if (typeElement.getKind() == ElementKind.ENUM) {
        return newEnumCtType(t);
      }
      String name = typeElement.getQualifiedName().toString();
      if (String.class.getName().equals(name)) {
        return newStringCtType(t);
      }
      if (Boolean.class.getName().equals(name)) {
        return newBooleanCtType(t);
      }
      if (Byte.class.getName().equals(name)) {
        return newByteCtType(t);
      }
      if (Short.class.getName().equals(name)) {
        return newShortCtType(t);
      }
      if (Integer.class.getName().equals(name)) {
        return newIntegerCtType(t);
      }
      if (Long.class.getName().equals(name)) {
        return newLongCtType(t);
      }
      if (Float.class.getName().equals(name)) {
        return newFloatCtType(t);
      }
      if (Double.class.getName().equals(name)) {
        return newDoubleCtType(t);
      }
      if (Object.class.getName().equals(name)) {
        return newObjectCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, BigDecimal.class)) {
        return newBigDecimalCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, BigInteger.class)) {
        return newBigIntegerCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Time.class)) {
        return newTimeCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Timestamp.class)) {
        return newTimestampCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Date.class)) {
        return newDateCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, java.util.Date.class)) {
        return newUtilDateCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, LocalTime.class)) {
        return newLocalTimeCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, LocalDateTime.class)) {
        return newLocalDateTimeCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, LocalDate.class)) {
        return newLocalDateCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Array.class)) {
        return newArrayCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Blob.class)) {
        return newBlobCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, NClob.class)) {
        return newNClobCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Clob.class)) {
        return newClobCtType(t);
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, SQLXML.class)) {
        return newSQLXMLCtType(t);
      }
      return null;
    }

    @Override
    public BasicCtType visitPrimitive(PrimitiveType t, Void p) {
      return switch (t.getKind()) {
        case BOOLEAN -> newPrimitiveBooleanCtType(t);
        case BYTE -> newPrimitiveByteCtType(t);
        case SHORT -> newPrimitiveShortCtType(t);
        case INT -> newPrimitiveIntCtType(t);
        case LONG -> newPrimitiveLongCtType(t);
        case FLOAT -> newPrimitiveFloatCtType(t);
        case DOUBLE -> newPrimitiveDoubleCtType(t);
        case CHAR -> null;
        default -> assertUnreachable();
      };
    }
  }
}
