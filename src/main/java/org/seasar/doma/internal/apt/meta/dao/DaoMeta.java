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
package org.seasar.doma.internal.apt.meta.dao;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.AccessLevel;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;
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

    private final TypeElement daoElement;

    private AnnotateWithReflection annotateWithReflection;

    private ParentDaoMeta parentDaoMeta;

    private ConfigMeta configMeta;

    public DaoMeta(DaoReflection daoReflection, TypeElement daoElement) {
        assertNotNull(daoReflection, daoElement);
        this.daoReflection = daoReflection;
        this.daoElement = daoElement;
    }

    public TypeMirror getDaoType() {
        return daoElement.asType();
    }

    public TypeElement getDaoElement() {
        return daoElement;
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

    public AccessLevel getAccessLevel() {
        return daoReflection.getAccessLevelValue();
    }

    public AnnotateWithReflection getAnnotateWithReflection() {
        return annotateWithReflection;
    }

    public void setAnnotateWithReflection(AnnotateWithReflection annotateWithReflection) {
        this.annotateWithReflection = annotateWithReflection;
    }

    public List<AnnotationReflection> getAnnotationReflections(AnnotationTarget target) {
        assertNotNull(target);
        if (annotateWithReflection == null
                || annotateWithReflection.getAnnotationsValue() == null) {
            return Collections.emptyList();
        }
        List<AnnotationReflection> results = new ArrayList<AnnotationReflection>();
        for (AnnotationReflection annotationReflection : annotateWithReflection
                .getAnnotationsValue()) {
            if (target.name()
                    .contentEquals(annotationReflection.getTargetValue().getSimpleName())) {
                results.add(annotationReflection);
            }
        }
        return results;
    }

    public ParentDaoMeta getParentDaoMeta() {
        return parentDaoMeta;
    }

    public void setParentDaoMeta(ParentDaoMeta parentDaoMeta) {
        this.parentDaoMeta = parentDaoMeta;
    }

    public ConfigMeta getConfigMeta() {
        return configMeta;
    }

    public void setConfigMeta(ConfigMeta configMeta) {
        this.configMeta = configMeta;
    }

}
