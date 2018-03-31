package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;

/** @author taedium */
public class EmbeddableCtType extends AbstractCtType {

  private final String descClassName;

  EmbeddableCtType(Context ctx, TypeMirror type) {
    super(ctx, type);
    assertNotNull(typeElement);
    CodeSpec codeSpec = ctx.getCodeSpecs().newEmbeddableDescCodeSpec(typeElement);
    this.descClassName = codeSpec.getQualifiedName();
  }

  public String getDescClassName() {
    return descClassName;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEmbeddableCtType(this, p);
  }
}
