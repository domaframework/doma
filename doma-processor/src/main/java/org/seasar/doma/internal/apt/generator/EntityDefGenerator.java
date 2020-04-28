package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.def.DefaultPropertyDef;
import org.seasar.doma.def.EntityDef;
import org.seasar.doma.def.PropertyDef;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.meta.entity.EntityDescMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMeta;
import org.seasar.doma.internal.util.Pair;

public class EntityDefGenerator extends AbstractGenerator {

  private final EntityMeta entityMeta;

  private final ClassName entityTypeClassName;

  public EntityDefGenerator(
      Context ctx, ClassName className, Printer printer, EntityDescMeta entityDescMeta) {
    super(ctx, className, printer);
    assertNotNull(entityDescMeta);
    this.entityMeta = entityDescMeta.getEntityMeta();
    TypeElement entityTypeElement = entityMeta.getTypeElement();
    Name binaryName = ctx.getMoreElements().getBinaryName(entityTypeElement);
    entityTypeClassName = ClassNames.newEntityTypeClassName(binaryName);
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
        "public final class %1$s implements %2$s<%3$s> {%n",
        /* 1 */ simpleName, /* 2 */ EntityDef.class, /* 3 */ entityMeta.getType());
    print("%n");
    indent();
    printValidateVersionStaticInitializer();
    printFields();
    printMethods();
    unindent();
    iprint("}%n");
  }

  private void printFields() {
    printEntityTypeField();
    printPropertyDefFields();
  }

  private void printEntityTypeField() {
    iprint("public final %1$s entityType = new %1$s();%n", entityTypeClassName);
    print("%n");
  }

  private void printPropertyDefFields() {
    UnwrapOptionalVisitor visitor = new UnwrapOptionalVisitor();
    for (EntityPropertyMeta p : entityMeta.getAllPropertyMetas()) {
      if (p.isEmbedded()) {
        iprint(
            "public final %1$s_ %2$s = new %1$s_(entityType, \"%2$s\");%n",
            /* 1 */ p.getCtType().getQualifiedName(), /* 2 */ p.getName());
      } else {
        Pair<CtType, TypeMirror> pair = p.getCtType().accept(visitor, null);
        iprint(
            "public final %1$s<%2$s> %3$s = new %4$s<>(%5$s.class, entityType, \"%3$s\");%n",
            /* 1 */ PropertyDef.class,
            /* 2 */ pair.snd,
            /* 3 */ p.getName(),
            /* 4 */ DefaultPropertyDef.class,
            /* 5 */ pair.fst.getQualifiedName());
      }
      print("%n");
    }
  }

  private void printMethods() {
    printAsType();
  }

  private void printAsType() {
    iprint("@Override%n");
    iprint("public %1$s asType() {%n", entityTypeClassName);
    iprint("    return entityType;%n");
    iprint("}%n");
  }
}
