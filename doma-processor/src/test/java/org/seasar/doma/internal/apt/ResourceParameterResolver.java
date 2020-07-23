package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.seasar.doma.internal.util.ResourceUtil;

public class ResourceParameterResolver implements ParameterResolver {

  private final Class<?> clazz;

  public ResourceParameterResolver(Class<?> clazz) {
    assertNotNull(clazz);
    this.clazz = clazz;
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(URL.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    Class<?> testClass = extensionContext.getTestClass().orElseThrow(AssertionError::new);
    String prefix = testClass.getName().replace(".", "/");
    String path = String.format("%s_%s.txt", prefix, clazz.getSimpleName());
    return ResourceUtil.getResource(path);
  }
}
