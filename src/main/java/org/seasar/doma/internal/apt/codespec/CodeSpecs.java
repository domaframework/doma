package org.seasar.doma.internal.apt.codespec;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Function;
import java.util.stream.Collectors;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.apt.Context;

public class CodeSpecs {

  private final Context ctx;

  public CodeSpecs(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  public CodeSpec newEntityDescCodeSpec(TypeElement entityElement) {
    assertNotNull(entityElement);
    return newDescCodeSpec(entityElement, Conventions::createDescClassName);
  }

  public CodeSpec newEmbeddableDescCodeSpec(TypeElement embeddableElement) {
    assertNotNull(embeddableElement);
    return newDescCodeSpec(embeddableElement, Conventions::createDescClassName);
  }

  public CodeSpec newHolderDescCodeSpec(TypeElement holderElement) {
    assertNotNull(holderElement);
    return newDescCodeSpec(holderElement, Conventions::createDescClassName);
  }

  public CodeSpec newExternalHolderDescCodeSpec(TypeElement holderElement) {
    assertNotNull(holderElement);
    return newDescCodeSpec(holderElement, Conventions::createExternalDescClassName);
  }

  private CodeSpec newDescCodeSpec(TypeElement typeElement, Function<Name, String> convention) {
    var binaryName = ctx.getElements().getBinaryName(typeElement);
    var descClassName = convention.apply(binaryName);
    var typeParamsName = createTypeParamsName(typeElement);
    return new CodeSpec(descClassName, typeParamsName);
  }

  public CodeSpec newDaoImplCodeSpec(TypeElement daoElement, TypeElement parentDaoElement) {
    assertNotNull(daoElement);
    var name = buildDaoName(daoElement);
    var typeParamsName = createTypeParamsName(daoElement);
    var parent = newParentDaoImplCodeSpec(parentDaoElement);
    return new CodeSpec(name, typeParamsName, parent);
  }

  private CodeSpec newParentDaoImplCodeSpec(TypeElement parentDaoElement) {
    if (parentDaoElement == null) {
      return null;
    }
    var name = buildDaoName(parentDaoElement);
    var typeParamsName = createTypeParamsName(parentDaoElement);
    return new CodeSpec(name, typeParamsName);
  }

  private String buildDaoName(TypeElement daoElement) {
    class DaoNameBuilder {
      String prefix() {
        var daoPackage = ctx.getOptions().getDaoPackage();
        if (daoPackage != null) {
          return daoPackage + ".";
        }
        var packageName = ctx.getElements().getPackageName(daoElement);
        var base = "";
        if (packageName != null && packageName.length() > 0) {
          base = packageName + ".";
        }
        var daoSubpackage = ctx.getOptions().getDaoSubpackage();
        if (daoSubpackage != null) {
          return base + daoSubpackage + ".";
        }
        return base;
      }

      String infix() {
        return daoElement.getSimpleName().toString();
      }

      String suffix() {
        return ctx.getOptions().getDaoSuffix();
      }

      String build() {
        return prefix() + infix() + suffix();
      }
    }
    return new DaoNameBuilder().build();
  }

  private String createTypeParamsName(TypeElement typeElement) {
    return typeElement
        .getTypeParameters()
        .stream()
        .map(TypeParameterElement::asType)
        .map(TypeMirror::toString)
        .collect(Collectors.joining(", "));
  }
}
