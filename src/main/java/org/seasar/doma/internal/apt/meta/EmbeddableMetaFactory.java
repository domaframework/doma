/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.meta;

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
import javax.annotation.processing.ProcessingEnvironment;
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
import org.seasar.doma.internal.apt.Notifier;
import org.seasar.doma.internal.apt.mirror.AllArgsConstructorMirror;
import org.seasar.doma.internal.apt.mirror.EmbeddableMirror;
import org.seasar.doma.internal.apt.mirror.ValueMirror;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.message.Message;

/** @author nakamura-to */
public class EmbeddableMetaFactory implements TypeElementMetaFactory<EmbeddableMeta> {

  protected final ProcessingEnvironment env;

  protected final EmbeddablePropertyMetaFactory propertyMetaFactory;

  public EmbeddableMetaFactory(
      ProcessingEnvironment env, EmbeddablePropertyMetaFactory propertyMetaFactory) {
    assertNotNull(env, propertyMetaFactory);
    this.env = env;
    this.propertyMetaFactory = propertyMetaFactory;
  }

  @Override
  public EmbeddableMeta createTypeElementMeta(TypeElement embeddableElement) {
    EmbeddableMirror mirror = EmbeddableMirror.newInstance(embeddableElement, env);
    if (mirror == null) {
      throw new AptIllegalStateException("mirror must not be null");
    }
    EmbeddableMeta embeddableMeta = new EmbeddableMeta(mirror, embeddableElement);
    Strategy strategy = createStrategy(embeddableElement, embeddableMeta);
    strategy.validateClass(embeddableElement, embeddableMeta);
    strategy.doFieldElements(embeddableElement, embeddableMeta);
    strategy.doConstructor(embeddableElement, embeddableMeta);
    return embeddableMeta;
  }

  protected Strategy createStrategy(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
    ValueMirror valueMirror = ValueMirror.newInstance(embeddableElement, env);
    if (valueMirror != null) {
      return new ValueStrategy(env, propertyMetaFactory, valueMirror);
    }
    AllArgsConstructorMirror allArgsConstructorMirror =
        AllArgsConstructorMirror.newInstance(embeddableElement, env);
    if (allArgsConstructorMirror != null) {
      return new AllArgsConstructorStrategy(env, propertyMetaFactory, allArgsConstructorMirror);
    }
    return new DefaultStrategy(env, propertyMetaFactory);
  }

  protected interface Strategy {

    void validateClass(TypeElement embeddableElement, EmbeddableMeta embeddableMeta);

    void doFieldElements(TypeElement embeddableElement, EmbeddableMeta embeddableMeta);

    void doConstructor(TypeElement embeddableElement, EmbeddableMeta embeddableMeta);
  }

  protected static class DefaultStrategy implements Strategy {

    protected final ProcessingEnvironment env;

    protected final EmbeddablePropertyMetaFactory propertyMetaFactory;

    public DefaultStrategy(
        ProcessingEnvironment env, EmbeddablePropertyMetaFactory propertyMetaFactory) {
      assertNotNull(env, propertyMetaFactory);
      this.env = env;
      this.propertyMetaFactory = propertyMetaFactory;
    }

    @Override
    public void validateClass(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      if (embeddableElement.getKind() != ElementKind.CLASS) {
        EmbeddableMirror embeddableMirror = embeddableMeta.getEmbeddableMirror();
        throw new AptException(
            Message.DOMA4283,
            env,
            embeddableElement,
            embeddableMirror.getAnnotationMirror(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
      if (!embeddableElement.getTypeParameters().isEmpty()) {
        throw new AptException(
            Message.DOMA4285,
            env,
            embeddableElement,
            new Object[] {embeddableElement.getQualifiedName()});
      }
      validateEnclosingElement(embeddableElement);
    }

    protected void validateEnclosingElement(Element element) {
      TypeElement typeElement = ElementUtil.toTypeElement(element, env);
      if (typeElement == null) {
        return;
      }
      String simpleName = typeElement.getSimpleName().toString();
      if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
          || simpleName.contains(Constants.METATYPE_NAME_DELIMITER)) {
        throw new AptException(
            Message.DOMA4417, env, typeElement, new Object[] {typeElement.getQualifiedName()});
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
              Message.DOMA4415, env, typeElement, new Object[] {typeElement.getQualifiedName()});
        }
      } else {
        throw new AptException(
            Message.DOMA4416, env, typeElement, new Object[] {typeElement.getQualifiedName()});
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
                env,
                fieldElement,
                new Object[] {embeddableElement.getQualifiedName(), fieldElement.getSimpleName()});
          } else if (fieldElement.getAnnotation(Id.class) != null) {
            throw new AptException(
                Message.DOMA4289,
                env,
                fieldElement,
                new Object[] {embeddableElement.getQualifiedName(), fieldElement.getSimpleName()});
          } else if (fieldElement.getAnnotation(Version.class) != null) {
            throw new AptException(
                Message.DOMA4290,
                env,
                fieldElement,
                new Object[] {embeddableElement.getQualifiedName(), fieldElement.getSimpleName()});
          } else if (fieldElement.getAnnotation(TenantId.class) != null) {
            throw new AptException(
                Message.DOMA4443,
                env,
                fieldElement,
                new Object[] {embeddableElement.getQualifiedName(), fieldElement.getSimpleName()});
          } else if (fieldElement.getAnnotation(GeneratedValue.class) != null) {
            throw new AptException(
                Message.DOMA4291,
                env,
                fieldElement,
                new Object[] {embeddableElement.getQualifiedName(), fieldElement.getSimpleName()});
          } else {
            doEmbeddablePropertyMeta(fieldElement, embeddableMeta);
          }
        } catch (AptException e) {
          Notifier.notify(env, e);
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
          t = TypeMirrorUtil.toTypeElement(t.getSuperclass(), env)) {
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
          if (env.getElementUtils().hides(hider, hidden)) {
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
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(declaredType, env);
        if (typeElement.getAnnotation(EntityField.class) != null) {
          if (foundAnnotationTypeElement != null) {
            throw new AptException(
                Message.DOMA4288,
                env,
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
            env,
            embeddableElement,
            new Object[] {embeddableElement.getQualifiedName()});
      }
      if (constructorMeta.getConstructorElement().getModifiers().contains(Modifier.PRIVATE)) {
        throw new AptException(
            Message.DOMA4294,
            env,
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
          if (!TypeMirrorUtil.isSameType(paramType, propertyType, env)) {
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

    protected final AllArgsConstructorMirror allArgsConstructorMirror;

    public AllArgsConstructorStrategy(
        ProcessingEnvironment env,
        EmbeddablePropertyMetaFactory propertyMetaFactory,
        AllArgsConstructorMirror allArgsConstructorMirror) {
      super(env, propertyMetaFactory);
      assertNotNull(allArgsConstructorMirror);
      this.allArgsConstructorMirror = allArgsConstructorMirror;
    }

    @Override
    public void doConstructor(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      if (!allArgsConstructorMirror.getStaticNameValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4424,
            env,
            embeddableElement,
            allArgsConstructorMirror.getAnnotationMirror(),
            allArgsConstructorMirror.getStaticName(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
      if (allArgsConstructorMirror.isAccessPrivate()) {
        throw new AptException(
            Message.DOMA4425,
            env,
            embeddableElement,
            allArgsConstructorMirror.getAnnotationMirror(),
            allArgsConstructorMirror.getAccess(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
      if (allArgsConstructorMirror.isAccessNone()) {
        throw new AptException(
            Message.DOMA4427,
            env,
            embeddableElement,
            allArgsConstructorMirror.getAnnotationMirror(),
            allArgsConstructorMirror.getAccess(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
    }
  }

  protected static class ValueStrategy extends DefaultStrategy {

    protected final ValueMirror valueMirror;

    public ValueStrategy(
        ProcessingEnvironment env,
        EmbeddablePropertyMetaFactory propertyMetaFactory,
        ValueMirror valueMirror) {
      super(env, propertyMetaFactory);
      assertNotNull(valueMirror);
      this.valueMirror = valueMirror;
    }

    @Override
    public void doConstructor(TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      if (!valueMirror.getStaticConstructorValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4423,
            env,
            embeddableElement,
            valueMirror.getAnnotationMirror(),
            valueMirror.getStaticConstructor(),
            new Object[] {embeddableElement.getQualifiedName()});
      }
    }
  }
}
