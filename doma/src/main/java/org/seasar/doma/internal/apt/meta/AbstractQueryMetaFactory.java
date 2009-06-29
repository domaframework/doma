package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor6;

import org.seasar.doma.Entity;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.apt.Models;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.Options;


/**
 * @author taedium
 * 
 */
public abstract class AbstractQueryMetaFactory implements QueryMetaFactory {

    protected final ProcessingEnvironment env;

    public AbstractQueryMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    protected void doTypeParameters(AbstractQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        for (TypeParameterElement element : method.getTypeParameters()) {
            String name = Models.getTypeName(element.asType(), daoMeta
                    .getTypeParameterMap(), env);
            queryMeta.addTypeParameterName(name);
        }
    }

    protected void doThrowTypes(AbstractQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        for (TypeMirror thrownType : method.getThrownTypes()) {
            queryMeta.addThrownTypeName(Models.getTypeName(thrownType, daoMeta
                    .getTypeParameterMap(), env));
        }
    }

    protected boolean isPrimitiveInt(TypeMirror typeMirror) {
        return typeMirror.accept(new TypeKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitPrimitiveAsInt(PrimitiveType t, Void p) {
                return true;
            }
        }, null);
    }

    protected boolean isPrimitiveIntArray(TypeMirror typeMirror) {
        return typeMirror.accept(new TypeKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitArray(ArrayType t, Void p) {
                return isPrimitiveInt(t.getComponentType());
            }

        }, null);
    }

    protected boolean isPrimitiveVoid(TypeMirror typeMirror) {
        return typeMirror.accept(new TypeKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitNoTypeAsVoid(NoType t, Void p) {
                return true;
            }
        }, null);
    }

    protected boolean isVoid(TypeMirror typeMirror) {
        return Models.isAssignable(typeMirror, Void.class, env);
    }

    protected boolean isEntity(TypeMirror typeMirror, DaoMeta daoMeta) {
        if (daoMeta.isGenericDao()) {
            return true;
        }
        TypeElement typeElement = Models.toTypeElement(typeMirror, env);
        return typeElement != null
                && typeElement.getAnnotation(Entity.class) != null;
    }

    protected boolean isDomain(TypeMirror typeMirror) {
        return Models.isAssignable(typeMirror, Domain.class, env);
    }

    protected boolean isAbstract(TypeMirror typeMirror) {
        TypeElement typeElement = Models.toTypeElement(typeMirror, env);
        return typeElement != null
                && typeElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    protected boolean isList(TypeMirror typeMirror) {
        TypeElement typeElement = Models.toTypeElement(typeMirror, env);
        if (typeElement != null) {
            return typeElement.getQualifiedName().contentEquals(List.class
                    .getName());
        }
        return false;
    }

    protected boolean isOptions(TypeMirror typeMirror,
            Class<? extends Options> optionsClass) {
        TypeElement typeElement = Models.toTypeElement(typeMirror, env);
        if (typeElement != null) {
            TypeElement optionTypeElement = env.getElementUtils()
                    .getTypeElement(optionsClass.getName());
            if (typeElement.equals(optionTypeElement)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isIterationCallback(TypeMirror typeMirror) {
        return Models.isAssignable(typeMirror, IterationCallback.class, env);
    }

}
