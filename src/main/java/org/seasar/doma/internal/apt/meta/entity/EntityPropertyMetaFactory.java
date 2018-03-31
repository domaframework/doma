package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.TableGenerator;
import org.seasar.doma.Version;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.SequenceGeneratorAnnot;
import org.seasar.doma.internal.apt.annot.TableGeneratorAnnot;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.id.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.TableIdGeneratorMeta;
import org.seasar.doma.message.Message;

public class EntityPropertyMetaFactory {

  private final Context ctx;

  private final VariableElement fieldElement;

  public EntityPropertyMetaFactory(Context ctx, VariableElement fieldElement) {
    assertNotNull(ctx);
    this.ctx = ctx;
    this.fieldElement = fieldElement;
  }

  public EntityPropertyMeta createEntityPropertyMeta() {
    var name = getName();
    var ctType = createCtType();
    var columnAnnot = ctx.getAnnots().newColumnAnnot(fieldElement);
    var filedPrefix = ctx.getOptions().getEntityFieldPrefix();
    var propertyMeta = new EntityPropertyMeta(fieldElement, name, ctType, columnAnnot, filedPrefix);
    doId(propertyMeta);
    doVersion(propertyMeta);
    doColumn(propertyMeta);
    return propertyMeta;
  }

  private String getName() {
    var name = fieldElement.getSimpleName().toString();
    if (name.startsWith(Constants.RESERVED_VARIABLE_NAME_PREFIX)) {
      throw new AptException(
          Message.DOMA4025, fieldElement, new Object[] {Constants.RESERVED_VARIABLE_NAME_PREFIX});
    }
    return name;
  }

  private CtType createCtType() {
    var type = fieldElement.asType();
    var ctTypes = ctx.getCtTypes();
    var ctType =
        ctTypes.toCtType(
            type,
            List.of(
                ctTypes::newOptionalCtType,
                ctTypes::newOptionalIntCtType,
                ctTypes::newOptionalLongCtType,
                ctTypes::newOptionalDoubleCtType,
                ctTypes::newHolderCtType,
                ctTypes::newEmbeddableCtType,
                ctTypes::newBasicCtType));
    ctType.accept(new CtTypeValidator(), null);
    return ctType;
  }

  private void doId(EntityPropertyMeta propertyMeta) {
    var id = fieldElement.getAnnotation(Id.class);
    if (id == null) {
      var generatedValue = fieldElement.getAnnotation(GeneratedValue.class);
      if (generatedValue == null) {
        validateSequenceGeneratorNotExistent(propertyMeta);
        validateTableGeneratorNotExistent(propertyMeta);
        return;
      }
      throw new AptException(Message.DOMA4033, fieldElement);
    }
    if (propertyMeta.isEmbedded()) {
      throw new AptException(Message.DOMA4302, fieldElement);
    }
    propertyMeta.setId(true);
    final var generatedValue = fieldElement.getAnnotation(GeneratedValue.class);
    if (generatedValue == null) {
      validateSequenceGeneratorNotExistent(propertyMeta);
      validateTableGeneratorNotExistent(propertyMeta);
      return;
    }
    if (propertyMeta.isEmbedded()) {
      throw new AptException(Message.DOMA4303, fieldElement);
    }
    if (!isNumber(propertyMeta.getCtType())) {
      throw new AptException(Message.DOMA4095, fieldElement);
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
    var sequenceGenerator = fieldElement.getAnnotation(SequenceGenerator.class);
    if (sequenceGenerator != null) {
      throw new AptException(Message.DOMA4030, fieldElement);
    }
  }

  private void validateTableGeneratorNotExistent(EntityPropertyMeta propertyMeta) {
    var tableGenerator = fieldElement.getAnnotation(TableGenerator.class);
    if (tableGenerator != null) {
      throw new AptException(Message.DOMA4031, fieldElement);
    }
  }

  private void doIdentityIdGeneratorMeta(EntityPropertyMeta propertyMeta) {
    propertyMeta.setIdGeneratorMeta(new IdentityIdGeneratorMeta());
  }

  private void doSequenceIdGeneratorMeta(EntityPropertyMeta propertyMeta) {
    var sequenceGeneratorAnnot = ctx.getAnnots().newSequenceGeneratorAnnot(fieldElement);
    if (sequenceGeneratorAnnot == null) {
      throw new AptException(Message.DOMA4034, fieldElement);
    }
    validateSequenceIdGenerator(propertyMeta, sequenceGeneratorAnnot);
    var idGeneratorMeta = new SequenceIdGeneratorMeta(sequenceGeneratorAnnot);
    propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
  }

  private void validateSequenceIdGenerator(
      EntityPropertyMeta propertyMeta, SequenceGeneratorAnnot sequenceGeneratorAnnot) {
    var typeElement = ctx.getTypes().toTypeElement(sequenceGeneratorAnnot.getImplementerValue());
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
    var constructor = ctx.getElements().getNoArgConstructor(typeElement);
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
    var tableGeneratorAnnot = ctx.getAnnots().newTableGeneratorAnnot(fieldElement);
    if (tableGeneratorAnnot == null) {
      throw new AptException(Message.DOMA4035, fieldElement);
    }
    validateTableIdGenerator(propertyMeta, tableGeneratorAnnot);
    var idGeneratorMeta = new TableIdGeneratorMeta(tableGeneratorAnnot);
    propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
  }

  private void validateTableIdGenerator(
      EntityPropertyMeta propertyMeta, TableGeneratorAnnot tableGeneratorAnnot) {
    var typeElement = ctx.getTypes().toTypeElement(tableGeneratorAnnot.getImplementerValue());
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
    var constructor = ctx.getElements().getNoArgConstructor(typeElement);
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
    var version = fieldElement.getAnnotation(Version.class);
    if (version != null) {
      if (propertyMeta.isEmbedded()) {
        throw new AptException(Message.DOMA4304, fieldElement);
      }
      if (!isNumber(propertyMeta.getCtType())) {
        throw new AptException(Message.DOMA4093, fieldElement);
      }
      propertyMeta.setVersion(true);
    }
  }

  private void doColumn(EntityPropertyMeta propertyMeta) {
    var columnAnnot = propertyMeta.getColumnAnnot();
    if (columnAnnot == null) {
      return;
    }
    if (propertyMeta.isEmbedded()) {
      throw new AptException(Message.DOMA4306, fieldElement, columnAnnot.getAnnotationMirror());
    }
    if (propertyMeta.isId() || propertyMeta.isVersion()) {
      if (!columnAnnot.getInsertableValue()) {
        throw new AptException(
            Message.DOMA4088,
            fieldElement,
            columnAnnot.getAnnotationMirror(),
            columnAnnot.getInsertable());
      }
      if (!columnAnnot.getUpdatableValue()) {
        throw new AptException(
            Message.DOMA4089,
            fieldElement,
            columnAnnot.getAnnotationMirror(),
            columnAnnot.getUpdatable());
      }
    }
  }

  private boolean isNumber(CtType ctType) {
    var isNumber =
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
              public Boolean visitHolderCtType(HolderCtType ctType, Void p)
                  throws RuntimeException {
                return ctType.getBasicCtType().accept(this, p);
              }

              @Override
              public Boolean visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
                var boxedType = ctx.getTypes().boxIfPrimitive(ctType.getType());
                return ctx.getTypes().isAssignable(boxedType, Number.class);
              }
            },
            null);
    return isNumber == Boolean.TRUE;
  }

  private class CtTypeValidator extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4232, fieldElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcardType() || ctType.hasTypevarType()) {
        throw new AptException(
            Message.DOMA4233, fieldElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }

    @Override
    public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4204, fieldElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcardType() || ctType.hasTypevarType()) {
        throw new AptException(
            Message.DOMA4205, fieldElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }

    @Override
    public Void visitAnyCtType(AnyCtType ctType, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4096, fieldElement, new Object[] {ctType.getType()});
    }
  }
}
