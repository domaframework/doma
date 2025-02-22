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
package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Iterator;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.meta.aggregate.AggregateStrategyMeta;
import org.seasar.doma.internal.apt.meta.aggregate.AssociationLinkerMeta;
import org.seasar.doma.jdbc.aggregate.AbstractAggregateStrategyType;
import org.seasar.doma.jdbc.aggregate.AssociationLinkerType;

public class AggregateStrategyTypeGenerator extends AbstractGenerator {

  private final AggregateStrategyMeta strategyMeta;

  public AggregateStrategyTypeGenerator(
      RoundContext ctx, ClassName className, Printer printer, AggregateStrategyMeta strategyMeta) {
    super(ctx, className, printer);
    assertNotNull(strategyMeta);
    this.strategyMeta = strategyMeta;
  }

  @Override
  public void generate() {
    printPackage();
    printClass();
  }

  private void printPackage() {
    if (!packageName.isEmpty()) {
      iprint("package %1$s;%n", packageName);
      iprint("%n");
    }
  }

  private void printClass() {
    iprint("/** */%n");
    printGenerated();
    iprint(
        "public final class %1$s extends %2$s {%n",
        /* 1 */ simpleName, /* 2 */ AbstractAggregateStrategyType.class);
    print("%n");
    indent();
    printFields();
    printValidateVersionStaticInitializer();
    printConstructor();
    printMethods();
    unindent();
    iprint("}%n");
  }

  private void printFields() {
    printSingletonField();
  }

  private void printSingletonField() {
    iprint("private static final %1$s __singleton = new %1$s();%n", simpleName);
    print("%n");
  }

  private void printConstructor() {
    iprint("private %1$s() {%n", simpleName);
    indent();
    iprint(
        "super(\"%1$s\", %2$s, \"%3$s\", java.util.List.of(%n",
        /* 1 */ strategyMeta.typeElement().getQualifiedName(),
        /* 2 */ strategyMeta.root().getTypeCode(),
        /* 3 */ strategyMeta.tableAlias());
    indent();
    Iterator<AssociationLinkerMeta> iter = strategyMeta.associationLinkerMetas().iterator();
    while (iter.hasNext()) {
      AssociationLinkerMeta linkerMeta = iter.next();
      iprint(
          "%1$s.of(\"%2$s\", \"%3$s\", %4$s, \"%5$s\", %6$s, %7$s, %8$s.%9$s)",
          /* 1 */ AssociationLinkerType.class,
          /* 2 */ linkerMeta.ancestorPath(),
          /* 3 */ linkerMeta.propertyPath(),
          /* 4 */ linkerMeta.propertyPathDepth(),
          /* 5 */ linkerMeta.tableAlias(),
          /* 6 */ linkerMeta.source().getTypeCode(),
          /* 7 */ linkerMeta.target().getTypeCode(),
          /* 8 */ linkerMeta.classElement(),
          /* 9 */ linkerMeta.filedElement());
      if (iter.hasNext()) {
        print(",");
      }
      print("%n");
    }
    unindent();
    iprint("));%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printMethods() {
    printGetSingletonInternalMethod();
  }

  private void printGetSingletonInternalMethod() {
    iprint("/**%n");
    iprint(" * @return the singleton%n");
    iprint(" */%n");
    iprint("public static %1$s getSingletonInternal() {%n", simpleName);
    iprint("    return __singleton;%n");
    iprint("}%n");
    print("%n");
  }
}
