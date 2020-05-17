package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.opentest4j.AssertionFailedError;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.ClassNames;

public class CriteriaGeneratedClassNameParameterResolver implements ParameterResolver {

  private final Class<?> clazz;
  private final String prefix;
  private final String suffix;

  public CriteriaGeneratedClassNameParameterResolver(Class<?> clazz) {
    this(clazz, "", "_");
  }

  public CriteriaGeneratedClassNameParameterResolver(Class<?> clazz, String prefix, String suffix) {
    assertNotNull(clazz);
    assertNotNull(prefix);
    assertNotNull(suffix);
    this.clazz = clazz;
    this.prefix = prefix;
    this.suffix = suffix;
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(String.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    if (clazz.isAnnotationPresent(Entity.class)) {
      return ClassNames.newEntityMetamodelClassNameBuilder(clazz.getName(), prefix, suffix)
          .toString();
    }
    throw new AssertionFailedError("annotation not found.");
  }
}
