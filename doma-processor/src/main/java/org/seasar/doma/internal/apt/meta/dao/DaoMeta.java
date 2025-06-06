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
package org.seasar.doma.internal.apt.meta.dao;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.AccessLevel;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.internal.apt.annot.AnnotateWithAnnot;
import org.seasar.doma.internal.apt.annot.AnnotationAnnot;
import org.seasar.doma.internal.apt.annot.DaoAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;

public class DaoMeta implements TypeElementMeta {

  private final List<QueryMeta> queryMetas = new ArrayList<>();

  private final DaoAnnot daoAnnot;

  private List<AnnotateWithAnnot> annotateWithAnnots = Collections.emptyList();

  private TypeMirror type;

  private TypeElement typeElement;

  private String name;

  private boolean error;

  private ParentDaoMeta parentDaoMeta;

  private String singletonMethodName;

  private String singletonFieldName;

  public DaoMeta(DaoAnnot daoAnnot) {
    assertNotNull(daoAnnot);
    this.daoAnnot = daoAnnot;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TypeMirror getType() {
    return type;
  }

  public void setType(TypeMirror type) {
    this.type = type;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public void setTypeElement(TypeElement typeElement) {
    this.typeElement = typeElement;
  }

  public void addQueryMeta(QueryMeta queryMeta) {
    queryMetas.add(queryMeta);
  }

  public List<QueryMeta> getQueryMetas() {
    return queryMetas;
  }

  DaoAnnot getDaoAnnot() {
    return daoAnnot;
  }

  public AccessLevel getAccessLevel() {
    return daoAnnot.getAccessLevelValue();
  }

  public List<AnnotateWithAnnot> getAnnotateWithAnnots() {
    return annotateWithAnnots;
  }

  public void setAnnotateWithAnnots(List<AnnotateWithAnnot> annotateWithAnnots) {
    this.annotateWithAnnots = annotateWithAnnots;
  }

  public List<AnnotationAnnot> getAnnotationMirrors(AnnotationTarget target) {
    assertNotNull(target);
    List<AnnotationAnnot> results = new ArrayList<>();
    for (AnnotateWithAnnot annotateWithAnnot : annotateWithAnnots) {
      for (AnnotationAnnot annotationAnnot : annotateWithAnnot.getAnnotationsValue()) {
        if (target.name().contentEquals(annotationAnnot.getTargetValue().getSimpleName())) {
          results.add(annotationAnnot);
        }
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
