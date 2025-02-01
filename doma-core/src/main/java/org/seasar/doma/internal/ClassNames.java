/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        + enclosingNames.stream().map(n -> n + Constants.TYPE_NAME_DELIMITER).collect(joining())
        + simpleName;
  }

  public static ClassName newEntityTypeClassName(CharSequence entityClassName) {
    assertNotNull(entityClassName);
    return new ClassNameBuilder(entityClassName).build();
  }

  public static ClassName newEmbeddableTypeClassName(CharSequence embeddedClassName) {
    assertNotNull(embeddedClassName);
    return new ClassNameBuilder(embeddedClassName).build();
  }

  public static ClassName newDomainTypeClassName(CharSequence domainClassName) {
    assertNotNull(domainClassName);
    return new ClassNameBuilder(domainClassName).build();
  }

  public static ClassName newExternalDomainTypeClassName(CharSequence externalDomainClassName) {
    assertNotNull(externalDomainClassName);
    return new ExternalDomainClassNameBuilder(externalDomainClassName).build();
  }

  public static ClassName newEntityMetamodelClassNameBuilder(
      CharSequence entityClassName, String metamodelPrefix, String metamodelSuffix) {
    assertNotNull(entityClassName, metamodelPrefix, metamodelSuffix);
    return new MetamodelClassNameBuilder(entityClassName, metamodelPrefix, metamodelSuffix).build();
  }

  public static ClassName newAggregateStrategyTypeClassName(
      CharSequence aggregateStrategyClassName) {
    assertNotNull(aggregateStrategyClassName);
    return new ClassNameBuilder(aggregateStrategyClassName).build();
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
      return Constants.TYPE_PREFIX;
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
      return Constants.EXTERNAL_DOMAIN_TYPE_ROOT_PACKAGE
          + "."
          + ClassUtil.getPackageName(binaryName)
          + ".";
    }
  }

  private static class MetamodelClassNameBuilder extends ClassNameBuilder {
    private final String criteriaPrefix;
    private final String criteriaSuffix;

    public MetamodelClassNameBuilder(
        CharSequence binaryName, String criteriaPrefix, String criteriaSuffix) {
      super(binaryName);
      this.criteriaPrefix = criteriaPrefix;
      this.criteriaSuffix = criteriaSuffix;
    }

    @Override
    protected String infix() {
      return criteriaPrefix;
    }

    @Override
    protected String suffix() {
      return super.suffix() + criteriaSuffix;
    }
  }
}
