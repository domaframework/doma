package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.ClassName;

public class TypeName {

  private final ClassName className;

  private final String typeName;

  private final String typeParametersDeclaration;

  public TypeName(ClassName className, String typeName, String typeParametersDeclaration) {
    assertNotNull(className, typeName, typeParametersDeclaration);
    this.className = className;
    this.typeName = typeName;
    this.typeParametersDeclaration = typeParametersDeclaration;
  }

  public ClassName getClassName() {
    return className;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getTypeParametersDeclaration() {
    return typeParametersDeclaration;
  }
}
