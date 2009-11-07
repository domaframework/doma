package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.mirror.DomainMirror;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.message.DomaMessageCode;

public class DomainMetaFactory {

    private final ProcessingEnvironment env;

    public DomainMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    public DomainMeta createDomainMeta(TypeElement classElement) {
        assertNotNull(classElement);
        DomainMirror domainMirror = DomainMirror.newInstance(classElement, env);
        if (domainMirror == null) {
            throw new AptIllegalStateException("domainMirror");
        }
        DomainMeta domainMeta = new DomainMeta(classElement, classElement
                .asType());
        domainMeta.setDomainMirror(domainMirror);
        doWrapperType(classElement, domainMeta);
        validateClass(classElement, domainMeta);
        validateConstructor(classElement, domainMeta);
        validateAccessorMethod(classElement, domainMeta);
        return domainMeta;
    }

    protected void doWrapperType(TypeElement classElement, DomainMeta domainMeta) {
        BasicType basicType = BasicType.newInstance(domainMeta.getValueType(),
                env);
        if (basicType == null) {
            DomainMirror domainMirror = domainMeta.getDomainMirror();
            throw new AptException(DomaMessageCode.DOMA4102, env, classElement,
                    domainMirror.getAnnotationMirror(), domainMirror
                            .getValueType(), domainMirror.getValueTypeValue());
        }
        domainMeta.setWrapperType(basicType.getWrapperType());
    }

    protected void validateClass(TypeElement classElement, DomainMeta domainMeta) {
        if (!classElement.getKind().isClass()) {
            DomainMirror domainMirror = domainMeta.getDomainMirror();
            throw new AptException(DomaMessageCode.DOMA4105, env, classElement,
                    domainMirror.getAnnotationMirror());
        }
        if (classElement.getModifiers().contains(Modifier.PRIVATE)) {
            throw new AptException(DomaMessageCode.DOMA4133, env, classElement);
        }
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(DomaMessageCode.DOMA4132, env, classElement);
        }
        if (classElement.getNestingKind().isNested()) {
            throw new AptException(DomaMessageCode.DOMA4106, env, classElement);
        }
        if (!classElement.getTypeParameters().isEmpty()) {
            throw new AptException(DomaMessageCode.DOMA4107, env, classElement);
        }
    }

    protected void validateConstructor(TypeElement classElement,
            DomainMeta domainMeta) {
        for (ExecutableElement constructor : ElementFilter
                .constructorsIn(classElement.getEnclosedElements())) {
            if (constructor.getModifiers().contains(Modifier.PRIVATE)) {
                continue;
            }
            List<? extends VariableElement> parameters = constructor
                    .getParameters();
            if (parameters.size() != 1) {
                continue;
            }
            TypeMirror parameterType = env.getTypeUtils().erasure(
                    parameters.get(0).asType());
            if (env.getTypeUtils().isSameType(parameterType,
                    domainMeta.getValueType())) {
                return;
            }
        }
        throw new AptException(DomaMessageCode.DOMA4103, env, classElement,
                domainMeta.getValueType());
    }

    protected void validateAccessorMethod(TypeElement classElement,
            DomainMeta domainMeta) {
        for (TypeElement t = classElement; t != null
                && t.asType().getKind() != TypeKind.NONE; t = TypeMirrorUtil
                .toTypeElement(t.getSuperclass(), env)) {
            for (ExecutableElement method : ElementFilter
                    .methodsIn(classElement.getEnclosedElements())) {
                if (!method.getSimpleName().contentEquals(
                        domainMeta.getAccessorMethod())) {
                    continue;
                }
                if (!method.getModifiers().contains(Modifier.PUBLIC)) {
                    continue;
                }
                if (!method.getParameters().isEmpty()) {
                    continue;
                }
                TypeMirror returnType = env.getTypeUtils().erasure(
                        method.getReturnType());
                if (env.getTypeUtils().isAssignable(returnType,
                        domainMeta.getValueType())) {
                    return;
                }
            }
        }
        throw new AptException(DomaMessageCode.DOMA4104, env, classElement,
                domainMeta.getValueType(), domainMeta.getAccessorMethod());
    }
}
