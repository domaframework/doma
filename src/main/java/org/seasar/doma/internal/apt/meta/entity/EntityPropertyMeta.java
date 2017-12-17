package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.id.IdGeneratorMeta;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;

/**
 * 
 * @author taedium
 * 
 */
public class EntityPropertyMeta extends AbstractPropertyMeta {

    private final String fieldPrefix;

    private boolean id;

    private boolean version;

    private IdGeneratorMeta idGeneratorMeta;

    public EntityPropertyMeta(VariableElement fieldElement, String name, CtType ctType,
            ColumnReflection columnReflection, String fieldPrefix) {
        super(fieldElement, name, ctType, columnReflection);
        assertNotNull(fieldPrefix);
        this.fieldPrefix = fieldPrefix;
    }

    public String getFieldName() {
        return fieldPrefix + name;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isVersion() {
        return version;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }

    public IdGeneratorMeta getIdGeneratorMeta() {
        return idGeneratorMeta;
    }

    public void setIdGeneratorMeta(IdGeneratorMeta idGeneratorMeta) {
        this.idGeneratorMeta = idGeneratorMeta;
    }

    public boolean isEmbedded() {
        return ctType.accept(new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(false) {
            @Override
            public Boolean visitEmbeddableCtType(EmbeddableCtType ctType, Void p)
                    throws RuntimeException {
                return true;
            }
        }, null);
    }

    public EmbeddableCtType getEmbeddableCtType() {
        return ctType.accept(new SimpleCtTypeVisitor<EmbeddableCtType, Void, RuntimeException>() {

            @Override
            protected EmbeddableCtType defaultAction(CtType ctType, Void p)
                    throws RuntimeException {
                throw new AptIllegalStateException("getEmbeddableCtType");
            }

            @Override
            public EmbeddableCtType visitEmbeddableCtType(EmbeddableCtType ctType, Void p)
                    throws RuntimeException {
                return ctType;
            }
        }, null);
    }
}
