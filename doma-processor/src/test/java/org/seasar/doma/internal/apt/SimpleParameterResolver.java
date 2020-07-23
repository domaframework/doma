package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class SimpleParameterResolver implements ParameterResolver {

  private final Object parameter;

  public SimpleParameterResolver(Object parameter) {
    assertNotNull(parameter);
    this.parameter = parameter;
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    Class<?> type = parameterContext.getParameter().getType();
    if (type.isArray() && parameter.getClass().isArray()) {
      return type.getComponentType().equals(parameter.getClass().getComponentType());
    }
    return type.equals(parameter.getClass());
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameter;
  }
}
