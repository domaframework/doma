package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.Embeddable;
import org.seasar.doma.EntityField;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.ParameterName;
import org.seasar.doma.TenantId;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AllArgsConstructorAnnot;
import org.seasar.doma.internal.apt.annot.EmbeddableAnnot;
import org.seasar.doma.internal.apt.annot.ValueAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.util.ElementKindUtil;
import org.seasar.doma.message.Message;

public class EmbeddableMetaFactory implements TypeElementMetaFactory<EmbeddableMeta> {

  private final Context ctx;

  public EmbeddableMetaFactory(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  @Override
  public EmbeddableMeta createTypeElementMeta(TypeElement embeddableElement) {
    EmbeddableAnnot mirror = ctx.getAnnotations().newEmbeddableAnnot(embeddableElement);
    if (mirror == null) {
      throw new AptIllegalStateException("annot must not be null");
    }
    EmbeddableMeta embeddableMeta = new EmbeddableMeta(mirror, embeddableElement);
    Strategy strategy = createStrategy(embeddableElement, embeddableMeta);
    strategy.validateClass(embeddableElement, embeddableMeta);
    strategy.doFieldElements(embeddableElement, embeddableMeta);
    strategy.doConstructor(embeddableElement, embeddableMeta);
    return embeddableMeta;
  }

  private Strategy createStrategy(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
    ValueAnnot valueAnnot = ctx.getAnnotations().newValueAnnot(embeddableElement);
    if (valueAnnot != null) {
      return new ValueStrategy(ctx, valueAnnot);
    }
    AllArgsConstructorAnnot allArgsConstructorAnnot =
        ctx.getAnnotations().newAllArgsConstructorAnnot(embeddableElement);
    if (allArgsConstructorAnnot != null) {
      return new AllArgsConstructorStrategy(ctx, allArgsConstructorAnnot);
    }
    return new DefaultStrategy(ctx);
  }

  interface Strategy {

    void validateClass(TypeElement embeddableElement, EmbeddableMeta embeddableMeta);

    void doFieldElements(TypeElement embeddableElement, EmbeddableMeta embeddableMeta);

    void doConstructor(TypeElement embeddableElement, EmbeddableMeta embeddableMeta);
  }

  protected static class DefaultStrategy implements Strategy {

    final Context ctx;

    DefaultStrategy(Context ctx) {
      assertNotNull(ctx);
      this.ctx = ctx;
    }

    @Override
    public void validateClass(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      ElementKind kind = embeddableElement.getKind();
      if (kind != ElementKind.CLASS && !ElementKindUtil.isRecord(kind)) {
        EmbeddableAnnot embeddableAnnot = embeddableMeta.getEmbeddableAnnot();
        throw new AptException(
            Message.DOMA4283,
            embeddableElement,
            embeddableAnnot.getAnnotationMirror(),
            new Object[] {});
      }
      if (!embeddableElement.getTypeParameters().isEmpty()) {
        throw new AptException(Message.DOMA4285, embeddableElement, new Object[] {});
      }
      validateEnclosingElement(embeddableElement);
    }

    void validateEnclosingElement(Element element) {
      TypeElement typeElement = ctx.getMoreElements().toTypeElement(element);
      if (typeElement == null) {
        return;
      }
      String simpleName = typeElement.getSimpleName().toString();
      if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
          || simpleName.contains(Constants.DESC_NAME_DELIMITER)) {
        throw new AptException(
            Message.DOMA4417, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
      NestingKind nestingKind = typeElement.getNestingKind();
      if (nestingKind == NestingKind.TOP_LEVEL) {
        return;
      } else if (nestingKind == NestingKind.MEMBER) {
        Set<Modifier> modifiers = typeElement.getModifiers();
        if (modifiers.containsAll(Arrays.asList(Modifier.STATIC, Modifier.PUBLIC))) {
          validateEnclosingElement(typeElement.getEnclosingElement());
        } else {
          throw new AptException(
              Message.DOMA4415, typeElement, new Object[] {typeElement.getQualifiedName()});
        }
      } else {
        throw new AptException(
            Message.DOMA4416, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
    }

    @Override
    public void doFieldElements(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      for (VariableElement fieldElement : getFieldElements(embeddableElement)) {
        try {
          if (fieldElement.getAnnotation(Transient.class) != null) {
            continue;
          } else if (fieldElement.getModifiers().contains(Modifier.STATIC)) {
            continue;
          } else if (fieldElement.getAnnotation(OriginalStates.class) != null) {
            throw new AptException(Message.DOMA4286, fieldElement, new Object[] {});
          } else if (fieldElement.getAnnotation(Id.class) != null) {
            throw new AptException(Message.DOMA4289, fieldElement, new Object[] {});
          } else if (fieldElement.getAnnotation(Version.class) != null) {
            throw new AptException(Message.DOMA4290, fieldElement, new Object[] {});
          } else if (fieldElement.getAnnotation(TenantId.class) != null) {
            throw new AptException(Message.DOMA4443, fieldElement, new Object[] {});
          } else if (fieldElement.getAnnotation(GeneratedValue.class) != null) {
            throw new AptException(Message.DOMA4291, fieldElement, new Object[] {});
          } else {
            doEmbeddablePropertyMeta(fieldElement, embeddableMeta);
          }
        } catch (AptException e) {
          ctx.getReporter().report(e);
          embeddableMeta.setError(true);
        }
      }
    }

    void doEmbeddablePropertyMeta(VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
      validateFieldAnnotation(fieldElement, embeddableMeta);
      EmbeddablePropertyMetaFactory propertyMetaFactory =
          new EmbeddablePropertyMetaFactory(ctx, fieldElement);
      EmbeddablePropertyMeta propertyMeta = propertyMetaFactory.createEmbeddablePropertyMeta();
      embeddableMeta.addEmbeddablePropertyMeta(propertyMeta);
    }

    List<VariableElement> getFieldElements(TypeElement embeddableElement) {
      List<VariableElement> results = new LinkedList<>();
      for (TypeElement t = embeddableElement;
          t != null && t.asType().getKind() != TypeKind.NONE;
          t = ctx.getMoreTypes().toTypeElement(t.getSuperclass())) {
        if (t.getAnnotation(Embeddable.class) == null) {
          continue;
        }
        List<VariableElement> fields =
            new LinkedList<>(ElementFilter.fieldsIn(t.getEnclosedElements()));
        Collections.reverse(fields);
        results.addAll(fields);
      }
      Collections.reverse(results);

      List<VariableElement> hiderFields = new LinkedList<>(results);
      for (Iterator<VariableElement> it = results.iterator(); it.hasNext(); ) {
        VariableElement hidden = it.next();
        for (VariableElement hider : hiderFields) {
          if (ctx.getMoreElements().hides(hider, hidden)) {
            it.remove();
          }
        }
      }
      return results;
    }

    void validateFieldAnnotation(VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
      TypeElement foundAnnotationTypeElement = null;
      for (AnnotationMirror annotation : fieldElement.getAnnotationMirrors()) {
        DeclaredType declaredType = annotation.getAnnotationType();
        TypeElement typeElement = ctx.getMoreTypes().toTypeElement(declaredType);
        if (typeElement.getAnnotation(EntityField.class) != null) {
          if (foundAnnotationTypeElement != null) {
            throw new AptException(
                Message.DOMA4288,
                fieldElement,
                new Object[] {
                  foundAnnotationTypeElement.getQualifiedName(), typeElement.getQualifiedName()
                });
          }
          foundAnnotationTypeElement = typeElement;
        }
      }
    }

    @Override
    public void doConstructor(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      if (embeddableMeta.isAbstract()) {
        return;
      }
      EmbeddableConstructorMeta constructorMeta =
          getConstructorMeta(embeddableElement, embeddableMeta);
      if (constructorMeta == null) {
        throw new AptException(Message.DOMA4293, embeddableElement, new Object[] {});
      }
      if (constructorMeta.getConstructorElement().getModifiers().contains(Modifier.PRIVATE)) {
        throw new AptException(Message.DOMA4294, embeddableElement, new Object[] {});
      }
      embeddableMeta.setConstructorMeta(constructorMeta);
    }

    EmbeddableConstructorMeta getConstructorMeta(
        TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      Map<String, EmbeddablePropertyMeta> propertyMetaMap = new HashMap<>();
      for (EmbeddablePropertyMeta propertyMeta : embeddableMeta.getEmbeddablePropertyMetas()) {
        propertyMetaMap.put(propertyMeta.getName(), propertyMeta);
      }
      outer:
      for (ExecutableElement constructor :
          ElementFilter.constructorsIn(embeddableElement.getEnclosedElements())) {
        List<EmbeddablePropertyMeta> propertyMetaList = new ArrayList<>();
        for (VariableElement param : constructor.getParameters()) {
          String name = param.getSimpleName().toString();
          ParameterName parameterName = param.getAnnotation(ParameterName.class);
          if (parameterName != null) {
            name = parameterName.value();
          }
          TypeMirror paramType = param.asType();
          EmbeddablePropertyMeta propertyMeta = propertyMetaMap.get(name);
          if (propertyMeta == null) {
            continue outer;
          }
          TypeMirror propertyType = propertyMeta.getType();
          if (!ctx.getMoreTypes().isSameTypeWithErasure(paramType, propertyType)) {
            continue outer;
          }
          propertyMetaList.add(propertyMeta);
        }
        if (propertyMetaMap.size() == propertyMetaList.size()) {
          return new EmbeddableConstructorMeta(constructor, propertyMetaList);
        }
      }
      return null;
    }
  }

  protected static class AllArgsConstructorStrategy extends DefaultStrategy {

    final AllArgsConstructorAnnot allArgsConstructorAnnot;

    AllArgsConstructorStrategy(Context ctx, AllArgsConstructorAnnot allArgsConstructorAnnot) {
      super(ctx);
      assertNotNull(allArgsConstructorAnnot);
      this.allArgsConstructorAnnot = allArgsConstructorAnnot;
    }

    @Override
    public void doConstructor(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      if (!allArgsConstructorAnnot.getStaticNameValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4424,
            embeddableElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getStaticName(),
            new Object[] {});
      }
      if (allArgsConstructorAnnot.isAccessPrivate()) {
        throw new AptException(
            Message.DOMA4425,
            embeddableElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getAccess(),
            new Object[] {});
      }
      if (allArgsConstructorAnnot.isAccessNone()) {
        throw new AptException(
            Message.DOMA4427,
            embeddableElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getAccess(),
            new Object[] {});
      }
    }
  }

  protected static class ValueStrategy extends DefaultStrategy {

    final ValueAnnot valueAnnot;

    ValueStrategy(Context ctx, ValueAnnot valueAnnot) {
      super(ctx);
      assertNotNull(valueAnnot);
      this.valueAnnot = valueAnnot;
    }

    @Override
    public void doConstructor(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      if (!valueAnnot.getStaticConstructorValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4423,
            embeddableElement,
            valueAnnot.getAnnotationMirror(),
            valueAnnot.getStaticConstructor(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
    }
  }
}
