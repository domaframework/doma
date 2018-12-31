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

  protected final List<QueryMeta> queryMetas = new ArrayList<QueryMeta>();

  protected final DaoAnnot daoAnnot;

  protected AnnotateWithAnnot annotateWithAnnot;

  protected TypeMirror daoType;

  protected TypeElement daoElement;

  protected String name;

  protected boolean error;

  protected ParentDaoMeta parentDaoMeta;

  protected String singletonMethodName;

  protected String singletonFieldName;

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
    return daoAnnot.hasUserDefinedConfig();
  }

  DaoAnnot getDaoAnnot() {
    return daoAnnot;
  }

  public TypeMirror getConfigType() {
    return daoAnnot.getConfigValue();
  }

  public AccessLevel getAccessLevel() {
    return daoAnnot.getAccessLevelValue();
  }

  public AnnotateWithAnnot getAnnotateWithAnnot() {
    return annotateWithAnnot;
  }

  public void setAnnotateWithAnnot(AnnotateWithAnnot annotateWithAnnot) {
    this.annotateWithAnnot = annotateWithAnnot;
  }

  public List<AnnotationAnnot> getAnnotationMirrors(AnnotationTarget target) {
    assertNotNull(target);
    if (annotateWithAnnot == null || annotateWithAnnot.getAnnotationsValue() == null) {
      return Collections.emptyList();
    }
    List<AnnotationAnnot> results = new ArrayList<AnnotationAnnot>();
    for (AnnotationAnnot annotationAnnot : annotateWithAnnot.getAnnotationsValue()) {
      if (target.name().contentEquals(annotationAnnot.getTargetValue().getSimpleName())) {
        results.add(annotationAnnot);
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
