package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.TableGenerator;
import org.seasar.doma.TenantId;
import org.seasar.doma.Version;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
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
import org.seasar.doma.internal.apt.meta.MetaConstants;
import org.seasar.doma.internal.apt.meta.id.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.TableIdGeneratorMeta;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.message.Message;

public class EntityPropertyMetaFactory {

  protected final ProcessingEnvironment env;

  public EntityPropertyMetaFactory(ProcessingEnvironment env) {
    assertNotNull(env);
    this.env = env;
  }

  public EntityPropertyMeta createEntityPropertyMeta(
      VariableElement fieldElement, EntityMeta entityMeta) {
    assertNotNull(fieldElement, entityMeta);
    TypeElement entityElement = ElementUtil.toTypeElement(fieldElement.getEnclosingElement(), env);
    if (entityElement == null) {
      throw new AptIllegalStateException(fieldElement.toString());
    }
    EntityPropertyMeta propertyMeta =
        new EntityPropertyMeta(entityElement, fieldElement, entityMeta.getNamingType(), env);
    doCtType(propertyMeta, fieldElement, entityMeta);
    doName(propertyMeta, fieldElement, entityMeta);
    doId(propertyMeta, fieldElement, entityMeta);
    doVersion(propertyMeta, fieldElement, entityMeta);
    doTenantId(propertyMeta, fieldElement, entityMeta);
    doColumn(propertyMeta, fieldElement, entityMeta);
    return propertyMeta;
  }

  protected void doCtType(
      EntityPropertyMeta propertyMeta, final VariableElement fieldElement, EntityMeta entityMeta) {
    CtType ctType = resolveCtType(fieldElement, fieldElement.asType(), entityMeta);
    propertyMeta.setCtType(ctType);
  }

  protected CtType resolveCtType(
      VariableElement fieldElement, TypeMirror type, EntityMeta entityMeta) {
    final OptionalCtType optionalCtType = OptionalCtType.newInstance(type, env);
    if (optionalCtType != null) {
      if (optionalCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4232,
            env,
            fieldElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              entityMeta.getEntityElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      if (optionalCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4233,
            env,
            fieldElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              entityMeta.getEntityElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      return optionalCtType;
    }

    OptionalIntCtType optionalIntCtType = OptionalIntCtType.newInstance(type, env);
    if (optionalIntCtType != null) {
      return optionalIntCtType;
    }

    OptionalLongCtType optionalLongCtType = OptionalLongCtType.newInstance(type, env);
    if (optionalLongCtType != null) {
      return optionalLongCtType;
    }

    OptionalDoubleCtType optionalDoubleCtType = OptionalDoubleCtType.newInstance(type, env);
    if (optionalDoubleCtType != null) {
      return optionalDoubleCtType;
    }

    final DomainCtType domainCtType = DomainCtType.newInstance(type, env);
    if (domainCtType != null) {
      if (domainCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4204,
            env,
            fieldElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              entityMeta.getEntityElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      if (domainCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4205,
            env,
            fieldElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              entityMeta.getEntityElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      return domainCtType;
    }

    final EmbeddableCtType embeddableCtType = EmbeddableCtType.newInstance(type, env);
    if (embeddableCtType != null) {
      return embeddableCtType;
    }

    BasicCtType basicCtType = BasicCtType.newInstance(type, env);
    if (basicCtType != null) {
      return basicCtType;
    }

    throw new AptException(
        Message.DOMA4096,
        env,
        fieldElement,
        new Object[] {
          type, entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
        });
  }

  protected void doName(
      EntityPropertyMeta propertyMeta, VariableElement fieldElement, EntityMeta entityMeta) {
    String name = fieldElement.getSimpleName().toString();
    if (name.startsWith(MetaConstants.RESERVED_NAME_PREFIX)) {
      throw new AptException(
          Message.DOMA4025,
          env,
          fieldElement,
          new Object[] {
            MetaConstants.RESERVED_NAME_PREFIX, entityMeta.getEntityElement().getQualifiedName()
          });
    }
    propertyMeta.setName(name);
  }

  protected void doId(
      EntityPropertyMeta propertyMeta, VariableElement fieldElement, EntityMeta entityMeta) {
    Id id = fieldElement.getAnnotation(Id.class);
    if (id == null) {
      GeneratedValue generatedValue = fieldElement.getAnnotation(GeneratedValue.class);
      if (generatedValue == null) {
        validateSequenceGeneratorNotExistent(propertyMeta, fieldElement, entityMeta);
        validateTableGeneratorNotExistent(propertyMeta, fieldElement, entityMeta);
        return;
      }
      throw new AptException(
          Message.DOMA4033,
          env,
          fieldElement,
          new Object[] {entityMeta.getEntityElement(), fieldElement.getSimpleName()});
    }
    if (propertyMeta.isEmbedded()) {
      throw new AptException(
          Message.DOMA4302,
          env,
          fieldElement,
          new Object[] {
            entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
          });
    }
    propertyMeta.setId(true);
    final GeneratedValue generatedValue = fieldElement.getAnnotation(GeneratedValue.class);
    if (generatedValue == null) {
      validateSequenceGeneratorNotExistent(propertyMeta, fieldElement, entityMeta);
      validateTableGeneratorNotExistent(propertyMeta, fieldElement, entityMeta);
      return;
    }
    if (propertyMeta.isEmbedded()) {
      throw new AptException(
          Message.DOMA4303,
          env,
          fieldElement,
          new Object[] {
            entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
          });
    }
    if (entityMeta.hasGeneratedIdPropertyMeta()) {
      throw new AptException(
          Message.DOMA4037,
          env,
          fieldElement,
          new Object[] {
            entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
          });
    }
    if (!isNumber(propertyMeta.getCtType())) {
      throw new AptException(
          Message.DOMA4095,
          env,
          fieldElement,
          new Object[] {
            entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
          });
    }
    switch (generatedValue.strategy()) {
      case IDENTITY:
        doIdentityIdGeneratorMeta(propertyMeta, fieldElement, entityMeta);
        break;
      case SEQUENCE:
        doSequenceIdGeneratorMeta(propertyMeta, fieldElement, entityMeta);
        break;
      case TABLE:
        doTableIdGeneratorMeta(propertyMeta, fieldElement, entityMeta);
        break;
      default:
        assertUnreachable();
        break;
    }
  }

  protected void validateSequenceGeneratorNotExistent(
      EntityPropertyMeta propertyMeta, VariableElement fieldElement, EntityMeta entityMeta) {
    SequenceGenerator sequenceGenerator = fieldElement.getAnnotation(SequenceGenerator.class);
    if (sequenceGenerator != null) {
      throw new AptException(
          Message.DOMA4030,
          env,
          fieldElement,
          new Object[] {entityMeta.getEntityElement(), fieldElement.getSimpleName()});
    }
  }

  protected void validateTableGeneratorNotExistent(
      EntityPropertyMeta propertyMeta, VariableElement fieldElement, EntityMeta entityMeta) {
    TableGenerator tableGenerator = fieldElement.getAnnotation(TableGenerator.class);
    if (tableGenerator != null) {
      throw new AptException(
          Message.DOMA4031,
          env,
          fieldElement,
          new Object[] {entityMeta.getEntityElement(), fieldElement.getSimpleName()});
    }
  }

  protected void doIdentityIdGeneratorMeta(
      EntityPropertyMeta propertyMeta, VariableElement fieldElement, EntityMeta entityMeta) {
    propertyMeta.setIdGeneratorMeta(new IdentityIdGeneratorMeta());
  }

  protected void doSequenceIdGeneratorMeta(
      EntityPropertyMeta propertyMeta, VariableElement fieldElement, EntityMeta entityMeta) {
    SequenceGeneratorAnnot sequenceGeneratorAnnot =
        SequenceGeneratorAnnot.newInstance(fieldElement, env);
    if (sequenceGeneratorAnnot == null) {
      throw new AptException(
          Message.DOMA4034,
          env,
          fieldElement,
          new Object[] {entityMeta.getEntityElement(), fieldElement.getSimpleName()});
    }
    validateSequenceIdGenerator(propertyMeta, fieldElement, sequenceGeneratorAnnot);
    SequenceIdGeneratorMeta idGeneratorMeta = new SequenceIdGeneratorMeta(sequenceGeneratorAnnot);
    propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
  }

  protected void validateSequenceIdGenerator(
      EntityPropertyMeta propertyMeta,
      VariableElement fieldElement,
      SequenceGeneratorAnnot sequenceGeneratorAnnot) {
    TypeElement typeElement =
        TypeMirrorUtil.toTypeElement(sequenceGeneratorAnnot.getImplementerValue(), env);
    if (typeElement == null) {
      throw new AptIllegalStateException("failed to convert to TypeElement");
    }
    if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
      throw new AptException(
          Message.DOMA4170,
          env,
          fieldElement,
          sequenceGeneratorAnnot.getAnnotationMirror(),
          sequenceGeneratorAnnot.getImplementer(),
          new Object[] {typeElement.getQualifiedName()});
    }
    ExecutableElement constructor = ElementUtil.getNoArgConstructor(typeElement, env);
    if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
      throw new AptException(
          Message.DOMA4171,
          env,
          fieldElement,
          sequenceGeneratorAnnot.getAnnotationMirror(),
          sequenceGeneratorAnnot.getImplementer(),
          new Object[] {typeElement.getQualifiedName()});
    }
  }

  protected void doTableIdGeneratorMeta(
      EntityPropertyMeta propertyMeta, VariableElement fieldElement, EntityMeta entityMeta) {
    TableGeneratorAnnot tableGeneratorAnnot = TableGeneratorAnnot.newInstance(fieldElement, env);
    if (tableGeneratorAnnot == null) {
      throw new AptException(
          Message.DOMA4035,
          env,
          fieldElement,
          new Object[] {entityMeta.getEntityElement(), fieldElement.getSimpleName()});
    }
    validateTableIdGenerator(propertyMeta, fieldElement, tableGeneratorAnnot);
    TableIdGeneratorMeta idGeneratorMeta = new TableIdGeneratorMeta(tableGeneratorAnnot);
    propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
  }

  protected void validateTableIdGenerator(
      EntityPropertyMeta propertyMeta,
      VariableElement fieldElement,
      TableGeneratorAnnot tableGeneratorAnnot) {
    TypeElement typeElement =
        TypeMirrorUtil.toTypeElement(tableGeneratorAnnot.getImplementerValue(), env);
    if (typeElement == null) {
      throw new AptIllegalStateException("failed to convert to TypeElement");
    }
    if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
      throw new AptException(
          Message.DOMA4168,
          env,
          fieldElement,
          tableGeneratorAnnot.getAnnotationMirror(),
          tableGeneratorAnnot.getImplementer(),
          new Object[] {typeElement.getQualifiedName()});
    }
    ExecutableElement constructor = ElementUtil.getNoArgConstructor(typeElement, env);
    if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
      throw new AptException(
          Message.DOMA4169,
          env,
          fieldElement,
          tableGeneratorAnnot.getAnnotationMirror(),
          tableGeneratorAnnot.getImplementer(),
          new Object[] {typeElement.getQualifiedName()});
    }
  }

  protected void doVersion(
      EntityPropertyMeta propertyMeta, VariableElement fieldElement, EntityMeta entityMeta) {
    Version version = fieldElement.getAnnotation(Version.class);
    if (version != null) {
      if (propertyMeta.isEmbedded()) {
        throw new AptException(
            Message.DOMA4304,
            env,
            fieldElement,
            new Object[] {
              entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
            });
      }
      if (entityMeta.hasVersionPropertyMeta()) {
        throw new AptException(
            Message.DOMA4024,
            env,
            fieldElement,
            new Object[] {
              entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
            });
      }
      if (!isNumber(propertyMeta.getCtType())) {
        throw new AptException(
            Message.DOMA4093,
            env,
            fieldElement,
            new Object[] {
              entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
            });
      }
      propertyMeta.setVersion(true);
    }
  }

  protected void doTenantId(
      EntityPropertyMeta propertyMeta, VariableElement fieldElement, EntityMeta entityMeta) {
    TenantId tenantId = fieldElement.getAnnotation(TenantId.class);
    if (tenantId != null) {
      if (propertyMeta.isEmbedded()) {
        throw new AptException(
            Message.DOMA4441,
            env,
            fieldElement,
            new Object[] {
              entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
            });
      }
      if (entityMeta.hasTenantIdPropertyMeta()) {
        throw new AptException(
            Message.DOMA4442,
            env,
            fieldElement,
            new Object[] {
              entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
            });
      }
      propertyMeta.setTenantId(true);
    }
  }

  protected void doColumn(
      EntityPropertyMeta propertyMeta, VariableElement fieldElement, EntityMeta entityMeta) {
    ColumnAnnot columnAnnot = ColumnAnnot.newInstance(fieldElement, env);
    if (columnAnnot == null) {
      return;
    }
    if (propertyMeta.isEmbedded()) {
      throw new AptException(
          Message.DOMA4306,
          env,
          fieldElement,
          columnAnnot.getAnnotationMirror(),
          new Object[] {
            entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
          });
    }
    if (propertyMeta.isId() || propertyMeta.isVersion()) {
      if (!columnAnnot.getInsertableValue()) {
        throw new AptException(
            Message.DOMA4088,
            env,
            fieldElement,
            columnAnnot.getAnnotationMirror(),
            columnAnnot.getInsertable(),
            new Object[] {
              entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
            });
      }
      if (!columnAnnot.getUpdatableValue()) {
        throw new AptException(
            Message.DOMA4089,
            env,
            fieldElement,
            columnAnnot.getAnnotationMirror(),
            columnAnnot.getUpdatable(),
            new Object[] {
              entityMeta.getEntityElement().getQualifiedName(), fieldElement.getSimpleName()
            });
      }
    }
    propertyMeta.setColumnAnnot(columnAnnot);
  }

  protected boolean isNumber(CtType ctType) {
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
                TypeMirror boxedType = TypeMirrorUtil.boxIfPrimitive(ctType.getTypeMirror(), env);
                return TypeMirrorUtil.isAssignable(boxedType, Number.class, env);
              }
            },
            null);
    return isNumber == Boolean.TRUE;
  }
}
