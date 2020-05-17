package org.seasar.doma.internal.apt.util;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;

public final class AnnotationValueUtil {

  public static List<String> toStringList(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    final List<String> results = new ArrayList<>();
    value.accept(
        new SimpleAnnotationValueVisitor8<Void, Void>() {

          @Override
          public Void visitArray(List<? extends AnnotationValue> values, Void p) {
            for (AnnotationValue value : values) {
              value.accept(this, p);
            }
            return null;
          }

          @Override
          public Void visitString(String s, Void p) {
            results.add(s);
            return null;
          }
        },
        null);
    return results;
  }

  public static List<TypeMirror> toTypeList(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    final List<TypeMirror> results = new ArrayList<>();
    value.accept(
        new SimpleAnnotationValueVisitor8<Void, Void>() {

          @Override
          public Void visitArray(List<? extends AnnotationValue> values, Void p) {
            for (AnnotationValue value : values) {
              value.accept(this, p);
            }
            return null;
          }

          @Override
          public Void visitType(TypeMirror t, Void p) {
            results.add(t);
            return null;
          }
        },
        null);
    return results;
  }

  public static List<AnnotationMirror> toAnnotationList(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    final List<AnnotationMirror> results = new ArrayList<>();
    value.accept(
        new SimpleAnnotationValueVisitor8<Void, Void>() {

          @Override
          public Void visitArray(List<? extends AnnotationValue> values, Void p) {
            for (AnnotationValue value : values) {
              value.accept(this, p);
            }
            return null;
          }

          @Override
          public Void visitAnnotation(AnnotationMirror a, Void p) {
            results.add(a);
            return null;
          }
        },
        null);
    return results;
  }

  public static Boolean toBoolean(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(
        new SimpleAnnotationValueVisitor8<Boolean, Void>() {

          @Override
          public Boolean visitBoolean(boolean b, Void p) {
            return b;
          }
        },
        null);
  }

  public static Integer toInteger(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(
        new SimpleAnnotationValueVisitor8<Integer, Void>() {

          @Override
          public Integer visitInt(int i, Void p) {
            return i;
          }
        },
        null);
  }

  public static Long toLong(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(
        new SimpleAnnotationValueVisitor8<Long, Void>() {

          @Override
          public Long visitLong(long l, Void p) {
            return l;
          }
        },
        null);
  }

  public static String toString(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(
        new SimpleAnnotationValueVisitor8<String, Void>() {

          @Override
          public String visitString(String s, Void p) {
            return s;
          }
        },
        null);
  }

  public static TypeMirror toType(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(
        new SimpleAnnotationValueVisitor8<TypeMirror, Void>() {

          @Override
          public TypeMirror visitType(TypeMirror t, Void p) {
            return t;
          }
        },
        null);
  }

  public static VariableElement toEnumConstant(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(
        new SimpleAnnotationValueVisitor8<VariableElement, Void>() {

          @Override
          public VariableElement visitEnumConstant(VariableElement c, Void p) {
            return c;
          }
        },
        null);
  }

  public static AnnotationMirror toAnnotation(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(
        new SimpleAnnotationValueVisitor8<AnnotationMirror, Void>() {
          @Override
          public AnnotationMirror visitAnnotation(AnnotationMirror a, Void p) {
            return a;
          }
        },
        null);
  }
}
