package org.seasar.doma.internal.apt.meta.domain;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.DataTypeAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.def.TypeParametersDef;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.util.ElementKindUtil;
import org.seasar.doma.message.Message;

public class DataTypeMetaFactory implements TypeElementMetaFactory<DataTypeMeta> {

  private final Context ctx;

  public DataTypeMetaFactory(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  @Override
  public DataTypeMeta createTypeElementMeta(TypeElement typeElement) {
    assertNotNull(typeElement);
    DataTypeAnnot dataTypeAnnot = ctx.getAnnotations().newDataTypeAnnot(typeElement);
    if (dataTypeAnnot == null) {
      throw new AptIllegalStateException("dataTypeAnnot");
    }
    DataTypeMeta dataTypeMeta = new DataTypeMeta(typeElement, typeElement.asType(), dataTypeAnnot);
    validateTypeElement(typeElement, dataTypeMeta);
    doTypeParameters(typeElement, dataTypeMeta);
    doValueType(typeElement, dataTypeMeta);
    return dataTypeMeta;
  }

  private void validateTypeElement(TypeElement typeElement, DataTypeMeta dataTypeMeta) {
    if (!ElementKindUtil.isRecord(typeElement.getKind())) {
      throw new AptException(Message.DOMA4449, typeElement, new Object[] {});
    }
    if (typeElement.getNestingKind().isNested()) {
      validateEnclosingElement(typeElement);
    }
  }

  void validateEnclosingElement(Element element) {
    TypeElement typeElement = ctx.getMoreElements().toTypeElement(element);
    if (typeElement == null) {
      return;
    }
    String simpleName = typeElement.getSimpleName().toString();
    if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
        || simpleName.contains(Constants.TYPE_NAME_DELIMITER)) {
      throw new AptException(
          Message.DOMA4450, typeElement, new Object[] {typeElement.getQualifiedName()});
    }
    NestingKind nestingKind = typeElement.getNestingKind();
    if (nestingKind == NestingKind.TOP_LEVEL) {
      return;
    } else if (nestingKind == NestingKind.MEMBER) {
      Set<Modifier> modifiers = typeElement.getModifiers();
      if (modifiers.containsAll(Arrays.asList(Modifier.STATIC, Modifier.PUBLIC))) {
        validateEnclosingElement(typeElement.getEnclosingElement());
      } else {
        throw new AptException(
            Message.DOMA4451, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
    } else {
      throw new AptException(
          Message.DOMA4452, typeElement, new Object[] {typeElement.getQualifiedName()});
    }
  }

  public void doTypeParameters(TypeElement typeElement, DataTypeMeta dataTypeMeta) {
    TypeParametersDef typeParametersDef = ctx.getMoreElements().getTypeParametersDef(typeElement);
    dataTypeMeta.setTypeParametersDef(typeParametersDef);
  }

  private void doValueType(TypeElement typeElement, DataTypeMeta dataTypeMeta) {
    VariableElement param =
        ctx.getMoreElements().getSingleParameterOfRecordConstructor(typeElement);
    if (param == null) {
      throw new AptException(Message.DOMA4453, typeElement, new Object[] {});
    }
    TypeMirror type = param.asType();
    BasicCtType basicCtType = ctx.getCtTypes().newBasicCtType(type);
    if (basicCtType == null) {
      throw new AptException(Message.DOMA4454, param, new Object[] {type});
    }
    dataTypeMeta.setBasicCtType(basicCtType);
    dataTypeMeta.setAccessorMethod(param.getSimpleName().toString());
  }
}
