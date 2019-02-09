package org.seasar.doma.internal;

import static java.util.stream.Collectors.joining;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import org.seasar.doma.internal.util.ClassUtil;

public class Conventions {

  public static String normalizeBinaryName(String binaryName) {
    assertNotNull(binaryName);
    String packageName = ClassUtil.getPackageName(binaryName);
    List<String> enclosingNames = ClassUtil.getEnclosingNames(binaryName);
    String simpleName = ClassUtil.getSimpleName(binaryName);
    String base = "";
    if (packageName.length() > 0) {
      base = packageName + ".";
    }
    return base
        + enclosingNames.stream().map(n -> n + Constants.METATYPE_NAME_DELIMITER).collect(joining())
        + simpleName;
  }

  public static ClassName newDomainTypeClassName(String binaryName) {
    assertNotNull(binaryName);
    return new MetaTypeNameBuilder(binaryName).build();
  }

  public static ClassName newEmbeddableTypeClassName(String binaryName) {
    assertNotNull(binaryName);
    return new MetaTypeNameBuilder(binaryName).build();
  }

  public static ClassName newEntityTypeClassName(String binaryName) {
    assertNotNull(binaryName);
    return new MetaTypeNameBuilder(binaryName).build();
  }

  public static ClassName newExternalDomainTypClassName(String binaryName) {
    assertNotNull(binaryName);
    return new ExternalDomainMetaTypeNameBuilder(binaryName).build();
  }

  private static class MetaTypeNameBuilder {

    final String binaryName;

    private MetaTypeNameBuilder(String binaryName) {
      this.binaryName = binaryName;
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
      return Constants.METATYPE_PREFIX;
    }

    protected String suffix() {
      String normalizeBinaryName = normalizeBinaryName(binaryName);
      return ClassUtil.getSimpleName(normalizeBinaryName);
    }

    public ClassName build() {
      return new ClassName(prefix() + infix() + suffix());
    }
  }

  private static class ExternalDomainMetaTypeNameBuilder extends MetaTypeNameBuilder {

    private ExternalDomainMetaTypeNameBuilder(String binaryName) {
      super(binaryName);
    }

    @Override
    protected String prefix() {
      return Constants.EXTERNAL_DOMAIN_METATYPE_ROOT_PACKAGE
          + "."
          + ClassUtil.getPackageName(binaryName)
          + ".";
    }
  }
}
