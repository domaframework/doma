package org.seasar.doma.internal.apt.meta.entity;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleTypeVisitor8;
import org.seasar.doma.Scope;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;
import org.seasar.doma.internal.apt.decl.TypeParameterDeclaration;
import org.seasar.doma.message.Message;

public class ScopeClassMetaFactory {

  private final Context ctx;
  private final TypeElement typeElement;
  private final List<TypeParameterDeclaration> allTypeParameterDeclarations;

  public ScopeClassMetaFactory(Context ctx, TypeElement typeElement) {
    this.ctx = Objects.requireNonNull(ctx);
    this.typeElement = Objects.requireNonNull(typeElement);
    TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(typeElement);
    allTypeParameterDeclarations = typeDeclaration.getAllTypeParameterDeclarations();
  }

  public ScopeClassMeta createScopeClassMeta() {
    List<ExecutableElement> methods =
        ElementFilter.methodsIn(ctx.getMoreElements().getAllMembers(typeElement)).stream()
            .filter(m -> m.getAnnotation(Scope.class) != null)
            .sorted(Comparator.comparing(m -> m.getSimpleName().toString()))
            .collect(toList());
    List<ScopeMethodMeta> scopeMethodMetas = new ArrayList<>(methods.size());
    for (ExecutableElement method : methods) {
      if (method.getParameters().size() < 1) {
        throw new AptException(Message.DOMA4457, method, new Object[] {});
      }
      Set<Modifier> modifiers = method.getModifiers();
      if (modifiers.contains(Modifier.STATIC)) {
        throw new AptException(Message.DOMA4458, method, new Object[] {});
      }
      if (!modifiers.contains(Modifier.PUBLIC)) {
        throw new AptException(Message.DOMA4459, method, new Object[] {});
      }
      ScopeMethodMeta m = createScopeMethodMeta(method);
      scopeMethodMetas.add(m);
    }
    return new ScopeClassMeta(typeElement, scopeMethodMetas);
  }

  private ScopeMethodMeta createScopeMethodMeta(ExecutableElement method) {
    List<ScopeParameterMeta> scopeParameterMetas = new ArrayList<>(method.getParameters().size());
    Iterator<? extends VariableElement> it = method.getParameters().stream().skip(1L).iterator();
    while (it.hasNext()) {
      VariableElement parameter = it.next();
      boolean isVarArgs = !it.hasNext() && method.isVarArgs();
      ScopeParameterMeta p = createScopeParameterMeta(parameter, isVarArgs);
      scopeParameterMetas.add(p);
    }
    return new ScopeMethodMeta(method, scopeParameterMetas);
  }

  private ScopeParameterMeta createScopeParameterMeta(
      VariableElement parameter, boolean isVarArgs) {
    TypeMirror actualType = resolveTypeParameter(parameter.asType());
    TypeMirror componentType;
    if (isVarArgs) {
      componentType =
          actualType.accept(
              new SimpleTypeVisitor8<TypeMirror, Void>() {
                @Override
                public TypeMirror visitArray(ArrayType t, Void unused) {
                  return t.getComponentType();
                }
              },
              null);
    } else {
      componentType = null;
    }
    return new ScopeParameterMeta(parameter, actualType, componentType);
  }

  private TypeMirror resolveTypeParameter(TypeMirror formalType) {
    TypeParameterDeclaration typeParameterDeclaration =
        ctx.getDeclarations()
            .newTypeParameterDeclarationUsingTypeParams(formalType, allTypeParameterDeclarations);
    return typeParameterDeclaration.getActualType();
  }
}
