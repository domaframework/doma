package org.seasar.doma.internal;

import static java.util.stream.Collectors.joining;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import org.seasar.doma.internal.util.ClassUtil;

public class ClassNames {

  public static String normalizeBinaryName(CharSequence binaryName) {
    assertNotNull(binaryName);
    String name = binaryName.toString();
    String packageName = ClassUtil.getPackageName(name);
    List<String> enclosingNames = ClassUtil.getEnclosingNames(name);
    String simpleName = ClassUtil.getSimpleName(name);
    String base = "";
    if (packageName.length() > 0) {
      base = packageName + ".";
    }
    return base
        + enclosingNames.stream().map(n -> n + Constants.DESC_NAME_DELIMITER).collect(joining())
        + simpleName;
  }

  public static ClassName newEntityDescClassName(CharSequence entityClassName) {
    assertNotNull(entityClassName);
    return new ClassNameBuilder(entityClassName).build();
  }

  public static ClassName newEmbeddableDescClassName(CharSequence embeddedClassName) {
    assertNotNull(embeddedClassName);
    return new ClassNameBuilder(embeddedClassName).build();
  }

  public static ClassName newDomainDescClassName(CharSequence domainClassName) {
    assertNotNull(domainClassName);
    return new ClassNameBuilder(domainClassName).build();
  }

  public static ClassName newExternalDomainDescClassName(CharSequence externalDomainClassName) {
    assertNotNull(externalDomainClassName);
    return new ExternalDomainClassNameBuilder(externalDomainClassName).build();
  }

  private static class ClassNameBuilder {

    final String binaryName;

    private ClassNameBuilder(CharSequence binaryName) {
      this.binaryName = binaryName.toString();
    }

    protected String prefix() {
      String packageName = ClassUtil.getPackageName(binaryName);
      String prefix = "";
      if (packageName.length() > 0) {
        prefix = packageName + ".";
      }
      return prefix;
    }

    protected String infix() {
      return Constants.DESC_PREFIX;
    }

    protected String suffix() {
      String normalizeBinaryName = normalizeBinaryName(binaryName);
      return ClassUtil.getSimpleName(normalizeBinaryName);
    }

    public ClassName build() {
      return new ClassName(prefix() + infix() + suffix());
    }
  }

  private static class ExternalDomainClassNameBuilder extends ClassNameBuilder {

    private ExternalDomainClassNameBuilder(CharSequence binaryName) {
      super(binaryName);
    }

    @Override
    protected String prefix() {
      return Constants.EXTERNAL_DOMAIN_DESC_ROOT_PACKAGE
          + "."
          + ClassUtil.getPackageName(binaryName)
          + ".";
    }
  }
}
