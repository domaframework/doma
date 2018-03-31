package org.seasar.doma.internal.apt.meta.dao;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class ConfigMeta {

  private final TypeMirror configType;

  private final VariableElement singletonField;

  private final ExecutableElement singletonMethod;

  private ConfigMeta(
      TypeMirror configType, VariableElement singletonField, ExecutableElement singletonMethod) {
    assertNotNull(configType);
    this.configType = configType;
    this.singletonField = singletonField;
    this.singletonMethod = singletonMethod;
  }

  public TypeMirror getConfigType() {
    return configType;
  }

  public VariableElement getSingletonField() {
    return singletonField;
  }

  public ExecutableElement getSingletonMethod() {
    return singletonMethod;
  }

  public static ConfigMeta byConstructor(TypeMirror configType) {
    assertNotNull(configType);
    return new ConfigMeta(configType, null, null);
  }

  public static ConfigMeta byField(TypeMirror configType, VariableElement singletonField) {
    assertNotNull(configType, singletonField);
    return new ConfigMeta(configType, singletonField, null);
  }

  public static ConfigMeta byMethod(TypeMirror configType, ExecutableElement singletonMethod) {
    assertNotNull(configType, singletonMethod);
    return new ConfigMeta(configType, null, singletonMethod);
  }
}
