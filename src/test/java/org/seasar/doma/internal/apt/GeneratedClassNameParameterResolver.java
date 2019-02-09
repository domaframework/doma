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
import org.seasar.doma.internal.Conventions;

public class GeneratedClassNameParameterResolver implements ParameterResolver {

  private final Class<?> clazz;

  private final boolean isExternalDomain;

  public GeneratedClassNameParameterResolver(Class<?> clazz) {
    assertNotNull(clazz);
    this.clazz = clazz;
    this.isExternalDomain = false;
  }

  public GeneratedClassNameParameterResolver(Class<?> clazz, boolean isExternalDomain) {
    assertNotNull(clazz);
    this.clazz = clazz;
    this.isExternalDomain = true;
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
    if (isExternalDomain) {
      return Conventions.newExternalDomainTypClassName(clazz.getName()).toString();
    }
    if (clazz.isAnnotationPresent(Dao.class)) {
      return clazz.getName() + Options.Constants.DEFAULT_DAO_SUFFIX;
    }
    if (clazz.isAnnotationPresent(Entity.class)) {
      return Conventions.newEntityTypeClassName(clazz.getName()).toString();
    }
    if (clazz.isAnnotationPresent(Embeddable.class)) {
      return Conventions.newEmbeddableTypeClassName(clazz.getName()).toString();
    }
    if (clazz.isAnnotationPresent(Domain.class)) {
      return Conventions.newDomainTypeClassName(clazz.getName()).toString();
    }
    throw new AssertionFailedError("annotation not found.");
  }
}
