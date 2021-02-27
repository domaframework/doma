package org.seasar.doma.internal.apt;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class CompilationUnitsParameterResolver implements ParameterResolver {

  private final List<Class<?>> compilationUnits;

  public CompilationUnitsParameterResolver(Class<?>... compilationUnits) {
    this.compilationUnits = Arrays.asList(Objects.requireNonNull(compilationUnits));
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    Class<?> type = parameterContext.getParameter().getType();
    return type.equals(List.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return compilationUnits;
  }
}
