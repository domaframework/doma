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
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.AccessLevel;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.internal.apt.reflection.AnnotateWithReflection;
import org.seasar.doma.internal.apt.reflection.AnnotationReflection;
import org.seasar.doma.internal.apt.reflection.DaoReflection;

/**
 * 
 * @author taedium
 * 
 */
public class DaoMeta implements TypeElementMeta {

    private final List<QueryMeta> queryMetas = new ArrayList<QueryMeta>();

    private final DaoReflection daoReflection;

    private AnnotateWithReflection annotateWithReflection;

    private TypeMirror daoType;

    private TypeElement daoElement;

    private String name;

    private boolean error;

    private ParentDaoMeta parentDaoMeta;

    private String singletonMethodName;

    private String singletonFieldName;

    public DaoMeta(DaoReflection daoReflection) {
        assertNotNull(daoReflection);
        this.daoReflection = daoReflection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeMirror getDaoType() {
        return daoType;
    }

    public void setDaoType(TypeMirror daoType) {
        this.daoType = daoType;
    }

    public TypeElement getDaoElement() {
        return daoElement;
    }

    public void setDaoElement(TypeElement daoElement) {
        this.daoElement = daoElement;
    }

    public void addQueryMeta(QueryMeta queryMeta) {
        queryMetas.add(queryMeta);
    }

    public List<QueryMeta> getQueryMetas() {
        return queryMetas;
    }

    public boolean hasUserDefinedConfig() {
        return daoReflection.hasUserDefinedConfig();
    }

    public DaoReflection getDaoReflection() {
        return daoReflection;
    }

    public TypeMirror getConfigType() {
        return daoReflection.getConfigValue();
    }

    public AccessLevel getAccessLevel() {
        return daoReflection.getAccessLevelValue();
    }

    public AnnotateWithReflection getAnnotateWithMirror() {
        return annotateWithReflection;
    }

    public void setAnnotateWithMirror(AnnotateWithReflection annotateWithReflection) {
        this.annotateWithReflection = annotateWithReflection;
    }

    public List<AnnotationReflection> getAnnotationMirrors(AnnotationTarget target) {
        assertNotNull(target);
        if (annotateWithReflection == null
                || annotateWithReflection.getAnnotationsValue() == null) {
            return Collections.emptyList();
        }
        List<AnnotationReflection> results = new ArrayList<AnnotationReflection>();
        for (AnnotationReflection annotationReflection : annotateWithReflection
                .getAnnotationsValue()) {
            if (target.name().contentEquals(
                    annotationReflection.getTargetValue().getSimpleName())) {
                results.add(annotationReflection);
            }
        }
        return results;
    }

    @Override
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ParentDaoMeta getParentDaoMeta() {
        return parentDaoMeta;
    }

    public void setParentDaoMeta(ParentDaoMeta parentDaoMeta) {
        this.parentDaoMeta = parentDaoMeta;
    }

    public String getSingletonMethodName() {
        return singletonMethodName;
    }

    public void setSingletonMethodName(String singletonMethodName) {
        this.singletonMethodName = singletonMethodName;
    }

    public String getSingletonFieldName() {
        return singletonFieldName;
    }

    public void setSingletonFieldName(String singletonFieldName) {
        this.singletonFieldName = singletonFieldName;
    }

}
