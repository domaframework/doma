package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.def.DefaultPropertyDef;
import org.seasar.doma.def.PropertyDef;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableDescMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddablePropertyMeta;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.entity.EntityType;

public class EmbeddableDefGenerator extends AbstractGenerator {

  private final EmbeddableMeta embeddableMeta;

  private final ClassName embeddableTypeClassName;

  public EmbeddableDefGenerator(
      Context ctx, ClassName className, Printer printer, EmbeddableDescMeta embeddableDescMeta) {
    super(ctx, className, printer);
    assertNotNull(embeddableDescMeta);
    this.embeddableMeta = embeddableDescMeta.getEmbeddableMeta();
    TypeElement embeddableTypeElement = embeddableMeta.getTypeElement();
    Name binaryName = ctx.getMoreElements().getBinaryName(embeddableTypeElement);
    embeddableTypeClassName = ClassNames.newEmbeddableTypeClassName(binaryName);
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
    iprint("public final class %1$s {%n", simpleName);
    print("%n");
    indent();
    printValidateVersionStaticInitializer();
    printFields();
    printConstructor();
    unindent();
    iprint("}%n");
  }

  private void printFields() {
    printPropertyDefFields();
  }

  private void printPropertyDefFields() {
    UnwrapOptionalVisitor visitor = new UnwrapOptionalVisitor();
    for (EmbeddablePropertyMeta p : embeddableMeta.getEmbeddablePropertyMetas()) {
      Pair<CtType, TypeMirror> pair = p.getCtType().accept(visitor, null);
      iprint(
          "public final %1$s<%2$s> %3$s;%n",
          /* 1 */ PropertyDef.class, /* 2 */ pair.snd, /* 3 */ p.getName());
      print("%n");
    }
  }

  private void printConstructor() {
    iprint("public %1$s(%2$s<?> entityType, String name) {%n", simpleName, EntityType.class);
    indent();
    UnwrapOptionalVisitor visitor = new UnwrapOptionalVisitor();
    for (EmbeddablePropertyMeta p : embeddableMeta.getEmbeddablePropertyMetas()) {
      Pair<CtType, TypeMirror> pair = p.getCtType().accept(visitor, null);
      iprint(
          "this.%1$s = new %2$s<>(%3$s.class, entityType, name + \".%1$s\");%n",
          /* 1 */ p.getName(), /* 2 */
          DefaultPropertyDef.class, /* 3 */
          pair.fst.getQualifiedName());
    }
    unindent();
    iprint("}%n");
  }
}
