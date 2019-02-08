package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.opentest4j.AssertionFailedError;
import org.seasar.doma.Dao;
import org.seasar.doma.Domain;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.apt.util.MetaUtil;

public class GeneratedClassNameParameterResolver implements ParameterResolver {

  private final Class<?> clazz;

  public GeneratedClassNameParameterResolver(Class<?> clazz) {
    assertNotNull(clazz);
    this.clazz = clazz;
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
    if (clazz.isAnnotationPresent(Dao.class)) {
      return clazz.getName() + Options.Constants.DEFAULT_DAO_SUFFIX;
    }
    if (clazz.isAnnotationPresent(Entity.class)
        || clazz.isAnnotationPresent(Embeddable.class)
        || clazz.isAnnotationPresent(Domain.class)
        || clazz.isAnnotationPresent(ExternalDomain.class)) {
      return MetaUtil.toFullMetaName(clazz.getName());
    }
    throw new AssertionFailedError("annotation not found.");
  }
}
