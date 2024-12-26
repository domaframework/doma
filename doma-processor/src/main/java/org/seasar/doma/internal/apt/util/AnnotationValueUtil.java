package org.seasar.doma.internal.apt.util;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor14;

public final class AnnotationValueUtil {

  public static List<String> toStringList(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    final List<String> results = new ArrayList<>();
    value.accept(Visitors.fillStringList, results);
    return results;
  }

  public static List<TypeMirror> toTypeList(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    final List<TypeMirror> results = new ArrayList<>();
    value.accept(Visitors.fillTypeList, results);
    return results;
  }

  public static List<AnnotationMirror> toAnnotationList(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    final List<AnnotationMirror> results = new ArrayList<>();
    value.accept(Visitors.fillAnnotationList, results);
    return results;
  }

  public static Boolean toBoolean(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(Visitors.toBoolean, null);
  }

  public static Integer toInteger(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(Visitors.toInteger, null);
  }

  public static Long toLong(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(Visitors.toLong, null);
  }

  public static String toString(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(Visitors.toString, null);
  }

  public static TypeMirror toType(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(Visitors.toType, null);
  }

  public static VariableElement toEnumConstant(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(Visitors.toEnumConstant, null);
  }

  public static AnnotationMirror toAnnotation(AnnotationValue value) {
    if (value == null) {
      return null;
    }
    return value.accept(Visitors.toAnnotation, null);
  }

  private static final class Visitors {

    static final AnnotationValueVisitor<Void, List<String>> fillStringList =
        new SimpleAnnotationValueVisitor14<>() {
          @Override
          public Void visitArray(List<? extends AnnotationValue> values, List<String> p) {
            for (AnnotationValue value : values) {
              value.accept(this, p);
            }
            return null;
          }

          @Override
          public Void visitString(String s, List<String> p) {
            p.add(s);
            return null;
          }
        };

    static final AnnotationValueVisitor<Void, List<TypeMirror>> fillTypeList =
        new SimpleAnnotationValueVisitor14<>() {
          @Override
          public Void visitArray(List<? extends AnnotationValue> values, List<TypeMirror> p) {
            for (AnnotationValue value : values) {
              value.accept(this, p);
            }
            return null;
          }

          @Override
          public Void visitType(TypeMirror t, List<TypeMirror> p) {
            p.add(t);
            return null;
          }
        };

    static final AnnotationValueVisitor<Void, List<AnnotationMirror>> fillAnnotationList =
        new SimpleAnnotationValueVisitor14<>() {
          @Override
          public Void visitArray(List<? extends AnnotationValue> values, List<AnnotationMirror> p) {
            for (AnnotationValue value : values) {
              value.accept(this, p);
            }
            return null;
          }

          @Override
          public Void visitAnnotation(AnnotationMirror a, List<AnnotationMirror> p) {
            p.add(a);
            return null;
          }
        };

    static final AnnotationValueVisitor<Boolean, Void> toBoolean =
        new SimpleAnnotationValueVisitor14<>() {
          @Override
          public Boolean visitBoolean(boolean b, Void p) {
            return b;
          }
        };

    static final AnnotationValueVisitor<Integer, Void> toInteger =
        new SimpleAnnotationValueVisitor14<>() {
          @Override
          public Integer visitInt(int i, Void p) {
            return i;
          }
        };

    static final AnnotationValueVisitor<Long, Void> toLong =
        new SimpleAnnotationValueVisitor14<>() {
          @Override
          public Long visitLong(long l, Void p) {
            return l;
          }
        };

    static final AnnotationValueVisitor<String, Void> toString =
        new SimpleAnnotationValueVisitor14<>() {
          @Override
          public String visitString(String s, Void p) {
            return s;
          }
        };

    static final AnnotationValueVisitor<TypeMirror, Void> toType =
        new SimpleAnnotationValueVisitor14<>() {
          @Override
          public TypeMirror visitType(TypeMirror t, Void p) {
            return t;
          }
        };

    static final AnnotationValueVisitor<VariableElement, Void> toEnumConstant =
        new SimpleAnnotationValueVisitor14<>() {
          @Override
          public VariableElement visitEnumConstant(VariableElement c, Void p) {
            return c;
          }
        };

    static final AnnotationValueVisitor<AnnotationMirror, Void> toAnnotation =
        new SimpleAnnotationValueVisitor14<>() {
          @Override
          public AnnotationMirror visitAnnotation(AnnotationMirror a, Void p) {
            return a;
          }
        };
  }
}
