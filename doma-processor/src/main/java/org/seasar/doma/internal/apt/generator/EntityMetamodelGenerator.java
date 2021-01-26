package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.*;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.MetamodelAnnot;
import org.seasar.doma.internal.apt.annot.ScopeClass;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMeta;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.metamodel.DefaultPropertyMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.EntityTypeProxy;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;

public class EntityMetamodelGenerator extends AbstractGenerator {

  private final EntityMeta entityMeta;
  private final ClassName entityTypeName;

  public EntityMetamodelGenerator(
      Context ctx,
      ClassName className,
      Printer printer,
      EntityMeta entityMeta,
      ClassName entityTypeName) {
    super(ctx, className, printer);
    assertNotNull(entityMeta, entityTypeName);
    this.entityMeta = entityMeta;
    this.entityTypeName = entityTypeName;
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
        /* 1 */ simpleName, /* 2 */ EntityMetamodel.class, /* 3 */ entityMeta.getType());
    print("%n");
    indent();
    printValidateVersionStaticInitializer();
    printFields();
    printConstructors();
    printMethods();
    unindent();
    iprint("}%n");
  }

  private void printFields() {
    printQualifiedTableNameField();
    printEntityTypeField();
    printAllPropertyMetamodelsFields();
    printPropertyMetamodelFields();
    printScopeFields();
  }

  private void printEntityTypeField() {
    iprint("private final %1$s __entityType = %1$s.getSingletonInternal();%n", entityTypeName);
    print("%n");
  }

  private void printAllPropertyMetamodelsFields() {
    iprint(
        "private final java.util.List<%1$s<?>> __allPropertyMetamodels;%n",
        PropertyMetamodel.class);
    print("%n");
  }

  private void printQualifiedTableNameField() {
    iprint("private final String __qualifiedTableName;%n");
    print("%n");
  }

  private void printScopeFields() {
    MetamodelAnnot metamodelValue = entityMeta.getEntityAnnot().getMetamodelValue();
    if (metamodelValue == null) {
      return;
    }
    for (ScopeClass scope : metamodelValue.scopes()) {
      iprint("private final %1$s %2$s = new %1$s();%n", scope, scope.scopeField());
      print("%n");
    }
  }

  private void printConstructors() {
    printNoArgsConstructor();
    printOneArgConstructor();
  }

  private void printNoArgsConstructor() {
    iprint("public %1$s() {%n", simpleName);
    indent();
    iprint("this(\"\");%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printOneArgConstructor() {
    iprint("public %1$s(String qualifiedTableName) {%n", simpleName);
    indent();
    iprint("this.__qualifiedTableName = java.util.Objects.requireNonNull(qualifiedTableName);%n");
    iprint(
        "java.util.ArrayList<%1$s<?>> __list = new java.util.ArrayList<>(%2$s);%n",
        PropertyMetamodel.class, entityMeta.getAllPropertyMetas().size());
    for (EntityPropertyMeta p : entityMeta.getAllPropertyMetas()) {
      if (p.isEmbedded()) {
        iprint("__list.addAll(%1$s.allPropertyMetamodels());%n", p.getName());
      } else {
        iprint("__list.add(%1$s);%n", p.getName());
      }
    }
    iprint("__allPropertyMetamodels = java.util.Collections.unmodifiableList(__list);%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printPropertyMetamodelFields() {
    UnwrapOptionalVisitor visitor = new UnwrapOptionalVisitor();
    for (EntityPropertyMeta p : entityMeta.getAllPropertyMetas()) {
      if (p.isEmbedded()) {
        ClassName className = createEmbeddableTypeClassName(p);
        iprint(
            "public final %1$s.Metamodel %2$s = new %1$s.Metamodel(__entityType, \"%2$s\");%n",
            /* 1 */ className, /* 2 */ p.getName());
      } else {
        Pair<CtType, TypeMirror> pair = p.getCtType().accept(visitor, null);
        iprint(
            "public final %1$s<%2$s> %3$s = new %4$s<%2$s>(%5$s.class, __entityType, \"%3$s\");%n",
            /* 1 */ PropertyMetamodel.class,
            /* 2 */ pair.snd,
            /* 3 */ p.getName(),
            /* 4 */ DefaultPropertyMetamodel.class,
            /* 5 */ pair.fst.getQualifiedName());
      }
      print("%n");
    }
  }

  private ClassName createEmbeddableTypeClassName(EntityPropertyMeta p) {
    TypeElement embeddableTypeElement = ctx.getMoreTypes().toTypeElement(p.getType());
    if (embeddableTypeElement == null) {
      throw new AptIllegalStateException("embeddableTypeElement");
    }
    Name binaryName = ctx.getMoreElements().getBinaryName(embeddableTypeElement);
    return ClassNames.newEmbeddableTypeClassName(binaryName);
  }

  private void printMethods() {
    printAsTypeMethod();
    printAllPropertyMetamodelsMethod();
    printScopeMethods();
  }

  private void printAsTypeMethod() {
    iprint("@Override%n");
    iprint("public %1$s<%2$s> asType() {%n", EntityType.class, entityMeta.getType());
    indent();
    iprint(
        "return __qualifiedTableName.isEmpty() ? __entityType : new %1$s<>(__entityType, __qualifiedTableName);%n",
        EntityTypeProxy.class);
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printAllPropertyMetamodelsMethod() {
    iprint("@Override%n");
    iprint("public java.util.List<%1$s<?>> allPropertyMetamodels() {%n", PropertyMetamodel.class);
    indent();
    iprint("return __allPropertyMetamodels;%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printScopeMethods() {
    MetamodelAnnot metamodel = entityMeta.getEntityAnnot().getMetamodelValue();
    if (metamodel == null) {
      return;
    }
    for (ScopeClass scopeClass : metamodel.scopes()) {
      for (ExecutableElement method : scopeClass.scopeMethods()) {
        printScopeMethod(scopeClass, method);
      }
    }
  }

  private void printScopeMethod(ScopeClass scope, ExecutableElement method) {
    List<? extends VariableElement> parameters = new ArrayList<>(method.getParameters());
    TypeMirror returnType = method.getReturnType();
    String methodName = method.getSimpleName().toString();
    parameters.remove(0);

    iprint(
        "public %1$s%2$s %3$s(%4$s) {%n",
        buildTypeParameters(method), returnType, methodName, generateParameterList(method, parameters));
    indent();

    String params =
        parameters.stream().map(VariableElement::getSimpleName).collect(Collectors.joining(", "));
    if (!params.isEmpty()) {
      params = ", " + params;
    }
    iprint("return %1$s.%2$s(this%3$s);%n", scope.scopeField(), method, params);
    unindent();
    iprint("}%n");
    print("%n");
  }

  private String buildTypeParameters(ExecutableElement method) {
    List<? extends TypeParameterElement> typeParameters = method.getTypeParameters();
    if (typeParameters.isEmpty()) {
      return "";
    }
    return typeParameters.stream()
            .map(this::buildTypeParameter)
            .collect(Collectors.joining(", ", "<", "> "));
  }

  private String buildTypeParameter(TypeParameterElement element) {
    return element.getSimpleName().toString() + " extends " + element.getBounds().stream()
            .map(TypeMirror::toString)
            .collect(Collectors.joining(" & "));
  }

  private String generateParameterList(
      ExecutableElement method, List<? extends VariableElement> parameters) {
    List<String> params = new ArrayList<>();
    for (int i = 0; i < parameters.size(); i++) {
      VariableElement variable = parameters.get(i);
      boolean isLast = (parameters.size() - 1) == i;
      String type = variable.asType().toString();

      if (isLast && method.isVarArgs()) {
        // build varargs parameter
        ArrayType arrayType = (ArrayType) variable.asType();
        type = arrayType.getComponentType().toString() + "...";
      }

      params.add(type + " " + variable.getSimpleName());
    }

    return String.join(", ", params);
  }
}
