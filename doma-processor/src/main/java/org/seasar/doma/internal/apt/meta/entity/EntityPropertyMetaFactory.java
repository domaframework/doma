/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.util.stream.Collectors;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.TableGenerator;
import org.seasar.doma.TenantId;
import org.seasar.doma.Version;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
import org.seasar.doma.internal.apt.annot.EmbeddedAnnot;
import org.seasar.doma.internal.apt.annot.SequenceGeneratorAnnot;
import org.seasar.doma.internal.apt.annot.TableGeneratorAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.id.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.TableIdGeneratorMeta;
import org.seasar.doma.message.Message;

class EntityPropertyMetaFactory {

  private final RoundContext ctx;

  private final EntityMeta entityMeta;

  private final VariableElement fieldElement;

  public EntityPropertyMetaFactory(
      RoundContext ctx, EntityMeta entityMeta, VariableElement fieldElement) {
    assertNotNull(ctx, entityMeta, fieldElement);
    this.ctx = ctx;
    this.entityMeta = entityMeta;
    this.fieldElement = fieldElement;
  }

  public EntityPropertyMeta createEntityPropertyMeta() {
    assertNotNull(fieldElement, entityMeta);
    TypeElement entityElement =
        ctx.getMoreElements().toTypeElement(fieldElement.getEnclosingElement());
    if (entityElement == null) {
      throw new AptIllegalStateException(fieldElement.toString());
    }
    CtType ctType = ctx.getCtTypes().newCtType(fieldElement.asType(), new CtTypeValidator());
    String fieldPrefix = ctx.getOptions().getEntityFieldPrefix();
    EntityPropertyMeta propertyMeta = new EntityPropertyMeta(ctType, fieldPrefix);
    doName(propertyMeta);
    doId(propertyMeta);
    doVersion(propertyMeta);
    doTenantId(propertyMeta);
    doColumn(propertyMeta);
    doEmbedded(propertyMeta);
    return propertyMeta;
  }

  private void doName(EntityPropertyMeta propertyMeta) {
    String name = fieldElement.getSimpleName().toString();
    if (name.startsWith(Constants.RESERVED_IDENTIFIER_PREFIX)) {
      throw new AptException(
          Message.DOMA4025, fieldElement, new Object[] {Constants.RESERVED_IDENTIFIER_PREFIX});
    }
    propertyMeta.setName(name);
  }

  private void doId(EntityPropertyMeta propertyMeta) {
    Id id = fieldElement.getAnnotation(Id.class);
    if (id == null) {
      GeneratedValue generatedValue = fieldElement.getAnnotation(GeneratedValue.class);
      if (generatedValue == null) {
        validateSequenceGeneratorNotExistent(propertyMeta);
        validateTableGeneratorNotExistent(propertyMeta);
        return;
      }
      throw new AptException(Message.DOMA4033, fieldElement, new Object[] {});
    }
    if (propertyMeta.isEmbedded()) {
      throw new AptException(Message.DOMA4302, fieldElement, new Object[] {});
    }
    propertyMeta.setId(true);
    final GeneratedValue generatedValue = fieldElement.getAnnotation(GeneratedValue.class);
    if (generatedValue == null) {
      validateSequenceGeneratorNotExistent(propertyMeta);
      validateTableGeneratorNotExistent(propertyMeta);
      return;
    }
    if (propertyMeta.isEmbedded()) {
      throw new AptException(Message.DOMA4303, fieldElement, new Object[] {});
    }
    if (entityMeta.hasGeneratedIdPropertyMeta()) {
      throw new AptException(Message.DOMA4037, fieldElement, new Object[] {});
    }
    if (!isNumber(propertyMeta.getCtType())) {
      throw new AptException(Message.DOMA4095, fieldElement, new Object[] {});
    }
    switch (generatedValue.strategy()) {
      case IDENTITY:
        doIdentityIdGeneratorMeta(propertyMeta);
        break;
      case SEQUENCE:
        doSequenceIdGeneratorMeta(propertyMeta);
        break;
      case TABLE:
        doTableIdGeneratorMeta(propertyMeta);
        break;
      default:
        assertUnreachable();
        break;
    }
  }

  private void validateSequenceGeneratorNotExistent(EntityPropertyMeta propertyMeta) {
    SequenceGenerator sequenceGenerator = fieldElement.getAnnotation(SequenceGenerator.class);
    if (sequenceGenerator != null) {
      throw new AptException(Message.DOMA4030, fieldElement, new Object[] {});
    }
  }

  private void validateTableGeneratorNotExistent(EntityPropertyMeta propertyMeta) {
    TableGenerator tableGenerator = fieldElement.getAnnotation(TableGenerator.class);
    if (tableGenerator != null) {
      throw new AptException(Message.DOMA4031, fieldElement, new Object[] {});
    }
  }

  private void doIdentityIdGeneratorMeta(EntityPropertyMeta propertyMeta) {
    propertyMeta.setIdGeneratorMeta(new IdentityIdGeneratorMeta());
  }

  private void doSequenceIdGeneratorMeta(EntityPropertyMeta propertyMeta) {
    SequenceGeneratorAnnot sequenceGeneratorAnnot =
        ctx.getAnnotations().newSequenceGeneratorAnnot(fieldElement);
    if (sequenceGeneratorAnnot == null) {
      throw new AptException(Message.DOMA4034, fieldElement, new Object[] {});
    }
    validateSequenceIdGenerator(propertyMeta, sequenceGeneratorAnnot);
    SequenceIdGeneratorMeta idGeneratorMeta = new SequenceIdGeneratorMeta(sequenceGeneratorAnnot);
    propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
  }

  private void validateSequenceIdGenerator(
      EntityPropertyMeta propertyMeta, SequenceGeneratorAnnot sequenceGeneratorAnnot) {
    TypeElement typeElement =
        ctx.getMoreTypes().toTypeElement(sequenceGeneratorAnnot.getImplementerValue());
    if (typeElement == null) {
      throw new AptIllegalStateException("failed to convert to TypeElement");
    }
    if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
      throw new AptException(
          Message.DOMA4170,
          fieldElement,
          sequenceGeneratorAnnot.getAnnotationMirror(),
          sequenceGeneratorAnnot.getImplementer(),
          new Object[] {typeElement.getQualifiedName()});
    }
    ExecutableElement constructor = ctx.getMoreElements().getNoArgConstructor(typeElement);
    if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
      throw new AptException(
          Message.DOMA4171,
          fieldElement,
          sequenceGeneratorAnnot.getAnnotationMirror(),
          sequenceGeneratorAnnot.getImplementer(),
          new Object[] {typeElement.getQualifiedName()});
    }
  }

  private void doTableIdGeneratorMeta(EntityPropertyMeta propertyMeta) {
    TableGeneratorAnnot tableGeneratorAnnot =
        ctx.getAnnotations().newTableGeneratorAnnot(fieldElement);
    if (tableGeneratorAnnot == null) {
      throw new AptException(Message.DOMA4035, fieldElement, new Object[] {});
    }
    validateTableIdGenerator(propertyMeta, tableGeneratorAnnot);
    TableIdGeneratorMeta idGeneratorMeta = new TableIdGeneratorMeta(tableGeneratorAnnot);
    propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
  }

  private void validateTableIdGenerator(
      EntityPropertyMeta propertyMeta, TableGeneratorAnnot tableGeneratorAnnot) {
    TypeElement typeElement =
        ctx.getMoreTypes().toTypeElement(tableGeneratorAnnot.getImplementerValue());
    if (typeElement == null) {
      throw new AptIllegalStateException("failed to convert to TypeElement");
    }
    if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
      throw new AptException(
          Message.DOMA4168,
          fieldElement,
          tableGeneratorAnnot.getAnnotationMirror(),
          tableGeneratorAnnot.getImplementer(),
          new Object[] {typeElement.getQualifiedName()});
    }
    ExecutableElement constructor = ctx.getMoreElements().getNoArgConstructor(typeElement);
    if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
      throw new AptException(
          Message.DOMA4169,
          fieldElement,
          tableGeneratorAnnot.getAnnotationMirror(),
          tableGeneratorAnnot.getImplementer(),
          new Object[] {typeElement.getQualifiedName()});
    }
  }

  private void doVersion(EntityPropertyMeta propertyMeta) {
    Version version = fieldElement.getAnnotation(Version.class);
    if (version != null) {
      if (propertyMeta.isEmbedded()) {
        throw new AptException(Message.DOMA4304, fieldElement, new Object[] {});
      }
      if (entityMeta.hasVersionPropertyMeta()) {
        throw new AptException(Message.DOMA4024, fieldElement, new Object[] {});
      }
      if (!isNumber(propertyMeta.getCtType())) {
        throw new AptException(Message.DOMA4093, fieldElement, new Object[] {});
      }
      propertyMeta.setVersion(true);
    }
  }

  private void doTenantId(EntityPropertyMeta propertyMeta) {
    TenantId tenantId = fieldElement.getAnnotation(TenantId.class);
    if (tenantId != null) {
      if (propertyMeta.isEmbedded()) {
        throw new AptException(Message.DOMA4441, fieldElement, new Object[] {});
      }
      if (entityMeta.hasTenantIdPropertyMeta()) {
        throw new AptException(Message.DOMA4442, fieldElement, new Object[] {});
      }
      propertyMeta.setTenantId(true);
    }
  }

  private void doColumn(EntityPropertyMeta propertyMeta) {
    ColumnAnnot columnAnnot = ctx.getAnnotations().newColumnAnnot(fieldElement);
    if (columnAnnot == null) {
      return;
    }
    if (propertyMeta.isEmbedded()) {
      throw new AptException(
          Message.DOMA4306, fieldElement, columnAnnot.getAnnotationMirror(), new Object[] {});
    }
    if (propertyMeta.isId() || propertyMeta.isVersion()) {
      if (!columnAnnot.getInsertableValue()) {
        throw new AptException(
            Message.DOMA4088,
            fieldElement,
            columnAnnot.getAnnotationMirror(),
            columnAnnot.getInsertable(),
            new Object[] {});
      }
      if (!columnAnnot.getUpdatableValue()) {
        throw new AptException(
            Message.DOMA4089,
            fieldElement,
            columnAnnot.getAnnotationMirror(),
            columnAnnot.getUpdatable(),
            new Object[] {});
      }
    }
    propertyMeta.setColumnAnnot(columnAnnot);
  }

  private void doEmbedded(EntityPropertyMeta propertyMeta) {
    EmbeddedAnnot embeddedAnnot = ctx.getAnnotations().newEmbeddedAnnot(fieldElement);
    if (embeddedAnnot == null) {
      return;
    }
    if (!propertyMeta.isEmbedded()) {
      throw new AptException(
          Message.DOMA4498, fieldElement, embeddedAnnot.getAnnotationMirror(), new Object[] {});
    }
    var typeElement = ctx.getMoreTypes().toTypeElement(fieldElement.asType());
    if (typeElement == null) {
      throw new AptIllegalStateException(
          "failed to convert to TypeElement: " + fieldElement.getSimpleName());
    }
    var fieldNames =
        ElementFilter.fieldsIn(typeElement.getEnclosedElements()).stream()
            .map(it -> it.getSimpleName().toString())
            .collect(Collectors.toSet());
    for (var columnOverride : embeddedAnnot.getColumnOverridesValue()) {
      var name = columnOverride.getNameValue();
      if (!fieldNames.contains(name)) {
        throw new AptException(
            Message.DOMA4499,
            fieldElement,
            embeddedAnnot.getAnnotationMirror(),
            columnOverride.getName(),
            new Object[] {name, typeElement.getQualifiedName()});
      }
    }
    propertyMeta.setEmbeddedAnnot(embeddedAnnot);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean isNumber(CtType ctType) {
    Boolean isNumber =
        ctType.accept(
            new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>() {

              @Override
              public Boolean visitOptionalCtType(OptionalCtType ctType, Void p)
                  throws RuntimeException {
                return ctType.getElementCtType().accept(this, p);
              }

              @Override
              public Boolean visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                  throws RuntimeException {
                return true;
              }

              @Override
              public Boolean visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                  throws RuntimeException {
                return true;
              }

              @Override
              public Boolean visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
                  throws RuntimeException {
                return true;
              }

              @Override
              public Boolean visitDomainCtType(DomainCtType ctType, Void p)
                  throws RuntimeException {
                return ctType.getBasicCtType().accept(this, p);
              }

              @Override
              public Boolean visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
                TypeMirror boxedType = ctx.getMoreTypes().boxIfPrimitive(ctType.getType());
                return ctx.getMoreTypes().isAssignableWithErasure(boxedType, Number.class);
              }
            },
            null);
    return isNumber == Boolean.TRUE;
  }

  private class CtTypeValidator extends SimpleCtTypeVisitor<Void, Void, AptException> {

    @Override
    protected Void defaultAction(CtType ctType, Void aVoid) throws AptException {
      throw new AptException(Message.DOMA4096, fieldElement, new Object[] {ctType.getType()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitEmbeddableCtType(EmbeddableCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void aVoid)
        throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType optionalCtType, Void aVoid) throws AptException {
      if (optionalCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4232, fieldElement, new Object[] {optionalCtType.getQualifiedName()});
      }
      if (optionalCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4233, fieldElement, new Object[] {optionalCtType.getQualifiedName()});
      }
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType domainCtType, Void aVoid) throws AptException {
      if (domainCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4204, fieldElement, new Object[] {domainCtType.getQualifiedName()});
      }
      if (domainCtType.hasWildcard() || domainCtType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4205, fieldElement, new Object[] {domainCtType.getQualifiedName()});
      }
      return null;
    }
  }
}
