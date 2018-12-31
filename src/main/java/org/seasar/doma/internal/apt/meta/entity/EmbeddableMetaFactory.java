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
import org.seasar.doma.message.Message;

public class EmbeddableMetaFactory implements TypeElementMetaFactory<EmbeddableMeta> {

  protected final Context ctx;

  protected final EmbeddablePropertyMetaFactory propertyMetaFactory;

  public EmbeddableMetaFactory(Context ctx, EmbeddablePropertyMetaFactory propertyMetaFactory) {
    assertNotNull(ctx, propertyMetaFactory);
    this.ctx = ctx;
    this.propertyMetaFactory = propertyMetaFactory;
  }

  @Override
  public EmbeddableMeta createTypeElementMeta(TypeElement embeddableElement) {
    EmbeddableAnnot mirror = EmbeddableAnnot.newInstance(embeddableElement, ctx);
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

  protected Strategy createStrategy(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
    ValueAnnot valueAnnot = ValueAnnot.newInstance(embeddableElement, ctx);
    if (valueAnnot != null) {
      return new ValueStrategy(ctx, propertyMetaFactory, valueAnnot);
    }
    AllArgsConstructorAnnot allArgsConstructorAnnot =
        AllArgsConstructorAnnot.newInstance(embeddableElement, ctx);
    if (allArgsConstructorAnnot != null) {
      return new AllArgsConstructorStrategy(ctx, propertyMetaFactory, allArgsConstructorAnnot);
    }
    return new DefaultStrategy(ctx, propertyMetaFactory);
  }

  protected interface Strategy {

    void validateClass(TypeElement embeddableElement, EmbeddableMeta embeddableMeta);

    void doFieldElements(TypeElement embeddableElement, EmbeddableMeta embeddableMeta);

    void doConstructor(TypeElement embeddableElement, EmbeddableMeta embeddableMeta);
  }

  protected static class DefaultStrategy implements Strategy {

    protected final Context ctx;

    protected final EmbeddablePropertyMetaFactory propertyMetaFactory;

    public DefaultStrategy(Context ctx, EmbeddablePropertyMetaFactory propertyMetaFactory) {
      assertNotNull(ctx, propertyMetaFactory);
      this.ctx = ctx;
      this.propertyMetaFactory = propertyMetaFactory;
    }

    @Override
    public void validateClass(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      if (embeddableElement.getKind() != ElementKind.CLASS) {
        EmbeddableAnnot embeddableAnnot = embeddableMeta.getEmbeddableAnnot();
        throw new AptException(
            Message.DOMA4283,
            ctx.getEnv(),
            embeddableElement,
            embeddableAnnot.getAnnotationMirror(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
      if (!embeddableElement.getTypeParameters().isEmpty()) {
        throw new AptException(
            Message.DOMA4285,
            ctx.getEnv(),
            embeddableElement,
            new Object[] {embeddableElement.getQualifiedName()});
      }
      validateEnclosingElement(embeddableElement);
    }

    protected void validateEnclosingElement(Element element) {
      TypeElement typeElement = ctx.getElements().toTypeElement(element);
      if (typeElement == null) {
        return;
      }
      String simpleName = typeElement.getSimpleName().toString();
      if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
          || simpleName.contains(Constants.METATYPE_NAME_DELIMITER)) {
        throw new AptException(
            Message.DOMA4417,
            ctx.getEnv(),
            typeElement,
            new Object[] {typeElement.getQualifiedName()});
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
              Message.DOMA4415,
              ctx.getEnv(),
              typeElement,
              new Object[] {typeElement.getQualifiedName()});
        }
      } else {
        throw new AptException(
            Message.DOMA4416,
            ctx.getEnv(),
            typeElement,
            new Object[] {typeElement.getQualifiedName()});
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
            throw new AptException(
                Message.DOMA4286,
                ctx.getEnv(),
                fieldElement,
                new Object[] {embeddableElement.getQualifiedName(), fieldElement.getSimpleName()});
          } else if (fieldElement.getAnnotation(Id.class) != null) {
            throw new AptException(
                Message.DOMA4289,
                ctx.getEnv(),
                fieldElement,
                new Object[] {embeddableElement.getQualifiedName(), fieldElement.getSimpleName()});
          } else if (fieldElement.getAnnotation(Version.class) != null) {
            throw new AptException(
                Message.DOMA4290,
                ctx.getEnv(),
                fieldElement,
                new Object[] {embeddableElement.getQualifiedName(), fieldElement.getSimpleName()});
          } else if (fieldElement.getAnnotation(TenantId.class) != null) {
            throw new AptException(
                Message.DOMA4443,
                ctx.getEnv(),
                fieldElement,
                new Object[] {embeddableElement.getQualifiedName(), fieldElement.getSimpleName()});
          } else if (fieldElement.getAnnotation(GeneratedValue.class) != null) {
            throw new AptException(
                Message.DOMA4291,
                ctx.getEnv(),
                fieldElement,
                new Object[] {embeddableElement.getQualifiedName(), fieldElement.getSimpleName()});
          } else {
            doEmbeddablePropertyMeta(fieldElement, embeddableMeta);
          }
        } catch (AptException e) {
          ctx.getNotifier().notify(e);
          embeddableMeta.setError(true);
        }
      }
    }

    protected void doEmbeddablePropertyMeta(
        VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
      validateFieldAnnotation(fieldElement, embeddableMeta);
      EmbeddablePropertyMeta propertyMeta =
          propertyMetaFactory.createEmbeddablePropertyMeta(fieldElement, embeddableMeta);
      embeddableMeta.addEmbeddablePropertyMeta(propertyMeta);
    }

    protected List<VariableElement> getFieldElements(TypeElement embeddableElement) {
      List<VariableElement> results = new LinkedList<VariableElement>();
      for (TypeElement t = embeddableElement;
          t != null && t.asType().getKind() != TypeKind.NONE;
          t = ctx.getTypes().toTypeElement(t.getSuperclass())) {
        if (t.getAnnotation(Embeddable.class) == null) {
          continue;
        }
        List<VariableElement> fields = new LinkedList<VariableElement>();
        for (VariableElement field : ElementFilter.fieldsIn(t.getEnclosedElements())) {
          fields.add(field);
        }
        Collections.reverse(fields);
        results.addAll(fields);
      }
      Collections.reverse(results);

      List<VariableElement> hiderFields = new LinkedList<VariableElement>(results);
      for (Iterator<VariableElement> it = results.iterator(); it.hasNext(); ) {
        VariableElement hidden = it.next();
        for (VariableElement hider : hiderFields) {
          if (ctx.getElements().hides(hider, hidden)) {
            it.remove();
          }
        }
      }
      return results;
    }

    protected void validateFieldAnnotation(
        VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
      TypeElement foundAnnotationTypeElement = null;
      for (AnnotationMirror annotation : fieldElement.getAnnotationMirrors()) {
        DeclaredType declaredType = annotation.getAnnotationType();
        TypeElement typeElement = ctx.getTypes().toTypeElement(declaredType);
        if (typeElement.getAnnotation(EntityField.class) != null) {
          if (foundAnnotationTypeElement != null) {
            throw new AptException(
                Message.DOMA4288,
                ctx.getEnv(),
                fieldElement,
                new Object[] {
                  foundAnnotationTypeElement.getQualifiedName(),
                  typeElement.getQualifiedName(),
                  embeddableMeta.getEmbeddableElement().getQualifiedName(),
                  fieldElement.getSimpleName()
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
        throw new AptException(
            Message.DOMA4293,
            ctx.getEnv(),
            embeddableElement,
            new Object[] {embeddableElement.getQualifiedName()});
      }
      if (constructorMeta.getConstructorElement().getModifiers().contains(Modifier.PRIVATE)) {
        throw new AptException(
            Message.DOMA4294,
            ctx.getEnv(),
            embeddableElement,
            new Object[] {embeddableElement.getQualifiedName()});
      }
      embeddableMeta.setConstructorMeta(constructorMeta);
    }

    protected EmbeddableConstructorMeta getConstructorMeta(
        TypeElement embeddapleElement, EmbeddableMeta embeddableMeta) {
      Map<String, EmbeddablePropertyMeta> propertyMetaMap =
          new HashMap<String, EmbeddablePropertyMeta>();
      for (EmbeddablePropertyMeta propertyMeta : embeddableMeta.getEmbeddablePropertyMetas()) {
        propertyMetaMap.put(propertyMeta.getName(), propertyMeta);
      }
      outer:
      for (ExecutableElement constructor :
          ElementFilter.constructorsIn(embeddapleElement.getEnclosedElements())) {
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
          if (!ctx.getTypes().isSameType(paramType, propertyType)) {
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

    protected final AllArgsConstructorAnnot allArgsConstructorAnnot;

    public AllArgsConstructorStrategy(
        Context ctx,
        EmbeddablePropertyMetaFactory propertyMetaFactory,
        AllArgsConstructorAnnot allArgsConstructorAnnot) {
      super(ctx, propertyMetaFactory);
      assertNotNull(allArgsConstructorAnnot);
      this.allArgsConstructorAnnot = allArgsConstructorAnnot;
    }

    @Override
    public void doConstructor(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      if (!allArgsConstructorAnnot.getStaticNameValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4424,
            ctx.getEnv(),
            embeddableElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getStaticName(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
      if (allArgsConstructorAnnot.isAccessPrivate()) {
        throw new AptException(
            Message.DOMA4425,
            ctx.getEnv(),
            embeddableElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getAccess(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
      if (allArgsConstructorAnnot.isAccessNone()) {
        throw new AptException(
            Message.DOMA4427,
            ctx.getEnv(),
            embeddableElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getAccess(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
    }
  }

  protected static class ValueStrategy extends DefaultStrategy {

    protected final ValueAnnot valueAnnot;

    public ValueStrategy(
        Context ctx, EmbeddablePropertyMetaFactory propertyMetaFactory, ValueAnnot valueAnnot) {
      super(ctx, propertyMetaFactory);
      assertNotNull(valueAnnot);
      this.valueAnnot = valueAnnot;
    }

    @Override
    public void doConstructor(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      if (!valueAnnot.getStaticConstructorValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4423,
            ctx.getEnv(),
            embeddableElement,
            valueAnnot.getAnnotationMirror(),
            valueAnnot.getStaticConstructor(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
    }
  }
}
