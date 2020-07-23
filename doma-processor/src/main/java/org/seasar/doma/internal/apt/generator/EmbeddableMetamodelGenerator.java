package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddablePropertyMeta;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.metamodel.DefaultPropertyMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;

public class EmbeddableMetamodelGenerator extends AbstractGenerator {

  private final EmbeddableMeta embeddableMeta;

  public EmbeddableMetamodelGenerator(
      Context ctx, ClassName className, Printer printer, EmbeddableMeta embeddableMeta) {
    super(ctx, className, printer);
    assertNotNull(embeddableMeta);
    this.embeddableMeta = embeddableMeta;
  }

  @Override
  public void generate() {
    printClass();
  }

  private void printClass() {
    iprint("/** */%n");
    iprint("public static final class Metamodel {%n");
    print("%n");
    indent();
    printFields();
    printConstructor();
    printMethods();
    unindent();
    iprint("}%n");
  }

  private void printFields() {
    printAllPropertyModelFields();
    printPropertyMetamodelFields();
  }

  private void printAllPropertyModelFields() {
    iprint(
        "private final java.util.List<%1$s<?>> __allPropertyMetamodels;%n",
        PropertyMetamodel.class);
    print("%n");
  }

  private void printPropertyMetamodelFields() {
    UnwrapOptionalVisitor visitor = new UnwrapOptionalVisitor();
    for (EmbeddablePropertyMeta p : embeddableMeta.getEmbeddablePropertyMetas()) {
      Pair<CtType, TypeMirror> pair = p.getCtType().accept(visitor, null);
      iprint(
          "public final %1$s<%2$s> %3$s;%n",
          /* 1 */ PropertyMetamodel.class, /* 2 */ pair.snd, /* 3 */ p.getName());
      print("%n");
    }
  }

  private void printConstructor() {
    iprint("public Metamodel(%1$s<?> entityType, String name) {%n", EntityType.class);
    indent();
    UnwrapOptionalVisitor visitor = new UnwrapOptionalVisitor();
    for (EmbeddablePropertyMeta p : embeddableMeta.getEmbeddablePropertyMetas()) {
      Pair<CtType, TypeMirror> pair = p.getCtType().accept(visitor, null);
      iprint(
          "this.%1$s = new %2$s<%3$s>(%4$s.class, entityType, name + \".%1$s\");%n",
          /* 1 */ p.getName(),
          /* 2 */ DefaultPropertyMetamodel.class,
          /* 3 */ pair.snd,
          /* 4 */ pair.fst.getQualifiedName());
    }
    iprint(
        "java.util.List<%1$s<?>> __list = new java.util.ArrayList<>(%2$s);%n",
        PropertyMetamodel.class, embeddableMeta.getEmbeddablePropertyMetas().size());
    for (EmbeddablePropertyMeta p : embeddableMeta.getEmbeddablePropertyMetas()) {
      iprint("__list.add(this.%1$s);%n", p.getName());
    }
    iprint("__allPropertyMetamodels = java.util.Collections.unmodifiableList(__list);%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printMethods() {
    printAllPropertyMetamodelsMethod();
  }

  private void printAllPropertyMetamodelsMethod() {
    iprint("public java.util.List<%1$s<?>> allPropertyMetamodels() {%n", PropertyMetamodel.class);
    indent();
    iprint("return __allPropertyMetamodels;%n");
    unindent();
    iprint("}%n");
    print("%n");
  }
}
