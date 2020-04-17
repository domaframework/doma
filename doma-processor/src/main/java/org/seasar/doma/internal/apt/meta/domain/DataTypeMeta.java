package org.seasar.doma.internal.apt.meta.domain;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.DataTypeAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.def.TypeParametersDef;

public class DataTypeMeta implements DomainMeta {

  private final TypeElement typeElement;

  private final TypeMirror type;

  private final DataTypeAnnot dataTypeAnnot;

  private TypeParametersDef typeParametersDef;

  private BasicCtType basicCtType;

  private String accessorMethod;

  public DataTypeMeta(TypeElement typeElement, TypeMirror type, DataTypeAnnot dataTypeAnnot) {
    assertNotNull(typeElement, type);
    this.typeElement = typeElement;
    this.type = type;
    this.dataTypeAnnot = dataTypeAnnot;
  }

  @Override
  public TypeMirror getType() {
    return type;
  }

  @Override
  public TypeElement getTypeElement() {
    return typeElement;
  }

  public DataTypeAnnot getDataTypeAnnot() {
    return dataTypeAnnot;
  }

  @Override
  public String getFactoryMethod() {
    return "new";
  }

  @Override
  public String getAccessorMethod() {
    return accessorMethod;
  }

  public void setAccessorMethod(String accessorMethod) {
    this.accessorMethod = accessorMethod;
  }

  @Override
  public boolean getAcceptNull() {
    return false;
  }

  @Override
  public boolean providesConstructor() {
    return true;
  }

  @Override
  public List<String> getTypeParameters() {
    return typeParametersDef.getTypeParameters();
  }

  public void setTypeParametersDef(TypeParametersDef typeParametersDef) {
    this.typeParametersDef = typeParametersDef;
  }

  @Override
  public List<String> getTypeVariables() {
    return typeParametersDef.getTypeVariables();
  }

  @Override
  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  public void setBasicCtType(BasicCtType basicCtType) {
    this.basicCtType = basicCtType;
  }

  @Override
  public TypeMirror getValueType() {
    return basicCtType.getType();
  }

  @Override
  public boolean isParameterized() {
    return !typeElement.getTypeParameters().isEmpty();
  }

  @Override
  public boolean isError() {
    return false;
  }
}
