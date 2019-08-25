package org.seasar.doma.jdbc.domain;

import static org.seasar.doma.internal.Constants.EXTERNAL_DOMAIN_DESC_ARRAY_SUFFIX;

import java.lang.reflect.Method;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Domain;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.message.Message;

/** A factory for domain descriptions. */
public final class DomainTypeFactory {

  /**
   * Creates the domain description.
   *
   * @param <BASIC> the basic type
   * @param <DOMAIN> the domain type
   * @param domainClass the domain class
   * @return the domain description
   * @throws DomaNullPointerException if any arguments are {@code null}
   * @throws DomaIllegalArgumentException if the domain class is not annotated with {@link Domain}
   * @throws DomainTypeNotFoundException if the domain description is not found
   */
  public static <BASIC, DOMAIN> DomainType<BASIC, DOMAIN> getDomainType(Class<DOMAIN> domainClass) {
    return getDomainType(domainClass, new ClassHelper() {});
  }

  /**
   * Creates the domain description with {@link ClassHelper}.
   *
   * @param <BASIC> the basic type
   * @param <DOMAIN> the domain type
   * @param domainClass the domain class
   * @param classHelper the class helper
   * @return the domain description
   * @throws DomaNullPointerException if any arguments are {@code null}
   * @throws DomaIllegalArgumentException if the domain class is not annotated with {@link Domain}
   * @throws DomainTypeNotFoundException if the domain description is not found
   */
  public static <BASIC, DOMAIN> DomainType<BASIC, DOMAIN> getDomainType(
      Class<DOMAIN> domainClass, ClassHelper classHelper) {
    if (domainClass == null) {
      throw new DomaNullPointerException("domainClass");
    }
    if (classHelper == null) {
      throw new DomaNullPointerException("classHelper");
    }
    if (!domainClass.isAnnotationPresent(Domain.class)) {
      throw new DomaIllegalArgumentException(
          "domainClass", Message.DOMA2205.getMessage(domainClass.getName()));
    }
    String domainTypeClassName =
        ClassNames.newDomainDescClassName(domainClass.getName()).toString();
    try {
      Class<DOMAIN> clazz = classHelper.forName(domainTypeClassName);
      Method method = ClassUtil.getMethod(clazz, "getSingletonInternal");
      return MethodUtil.invoke(method, null);
    } catch (WrapException e) {
      throw new DomainTypeNotFoundException(
          e.getCause(), domainClass.getName(), domainTypeClassName);
    } catch (Exception e) {
      throw new DomainTypeNotFoundException(e, domainClass.getName(), domainTypeClassName);
    }
  }

  /**
   * Creates the external domain description.
   *
   * @param <BASIC> the basic type
   * @param <DOMAIN> the domain type
   * @param domainClass the domain class
   * @return the external domain description
   * @throws DomaNullPointerException if {@code domainClass} is {@code null}
   */
  public static <BASIC, DOMAIN> DomainType<BASIC, DOMAIN> getExternalDomainType(
      Class<DOMAIN> domainClass) {
    return getExternalDomainType(domainClass, new ClassHelper() {});
  }

  /**
   * Creates the external domain description with {@link ClassHelper}.
   *
   * @param <BASIC> the basic type
   * @param <DOMAIN> the domain type
   * @param domainClass the domain class
   * @param classHelper the class helper
   * @return the external domain description
   * @throws DomaNullPointerException if any arguments are {@code null}
   */
  public static <BASIC, DOMAIN> DomainType<BASIC, DOMAIN> getExternalDomainType(
      Class<DOMAIN> domainClass, ClassHelper classHelper) {
    if (domainClass == null) {
      throw new DomaNullPointerException("domainClass");
    }
    if (classHelper == null) {
      throw new DomaNullPointerException("classHelper");
    }
    Class<?> clazz;
    if (domainClass.isArray()) {
      String domainName =
          domainClass.getComponentType().getName() + EXTERNAL_DOMAIN_DESC_ARRAY_SUFFIX;
      clazz = loadExternalDomainDescClass(domainName, classHelper);
    } else {
      clazz = loadExternalDomainDescClassWithTraversal(domainClass, classHelper);
    }
    if (clazz == null) {
      return null;
    }
    try {
      Method method = ClassUtil.getMethod(clazz, "getSingletonInternal");
      return MethodUtil.invoke(method, null);
    } catch (WrapException e) {
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  private static Class<?> loadExternalDomainDescClass(String domainName, ClassHelper classHelper) {
    String domainTypeClassName = ClassNames.newExternalDomainDescClassName(domainName).toString();
    try {
      return classHelper.forName(domainTypeClassName);
    } catch (WrapException e) {
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  private static Class<?> loadExternalDomainDescClassWithTraversal(
      Class<?> domainClass, ClassHelper classHelper) {
    return ClassUtil.traverse(
        domainClass,
        c -> {
          String domainName = c.getName();
          return loadExternalDomainDescClass(domainName, classHelper);
        });
  }
}
