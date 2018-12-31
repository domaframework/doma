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
import org.seasar.doma.internal.apt.mirror.AnnotateWithMirror;
import org.seasar.doma.internal.apt.mirror.AnnotationMirror;
import org.seasar.doma.internal.apt.mirror.DaoMirror;

public class DaoMeta implements TypeElementMeta {

  protected final List<QueryMeta> queryMetas = new ArrayList<QueryMeta>();

  protected final DaoMirror daoMirror;

  protected AnnotateWithMirror annotateWithMirror;

  protected TypeMirror daoType;

  protected TypeElement daoElement;

  protected String name;

  protected boolean error;

  protected ParentDaoMeta parentDaoMeta;

  protected String singletonMethodName;

  protected String singletonFieldName;

  public DaoMeta(DaoMirror daoMirror) {
    assertNotNull(daoMirror);
    this.daoMirror = daoMirror;
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
    return daoMirror.hasUserDefinedConfig();
  }

  DaoMirror getDaoMirror() {
    return daoMirror;
  }

  public TypeMirror getConfigType() {
    return daoMirror.getConfigValue();
  }

  public AccessLevel getAccessLevel() {
    return daoMirror.getAccessLevelValue();
  }

  public AnnotateWithMirror getAnnotateWithMirror() {
    return annotateWithMirror;
  }

  public void setAnnotateWithMirror(AnnotateWithMirror annotateWithMirror) {
    this.annotateWithMirror = annotateWithMirror;
  }

  public List<AnnotationMirror> getAnnotationMirrors(AnnotationTarget target) {
    assertNotNull(target);
    if (annotateWithMirror == null || annotateWithMirror.getAnnotationsValue() == null) {
      return Collections.emptyList();
    }
    List<AnnotationMirror> results = new ArrayList<AnnotationMirror>();
    for (AnnotationMirror annotationMirror : annotateWithMirror.getAnnotationsValue()) {
      if (target.name().contentEquals(annotationMirror.getTargetValue().getSimpleName())) {
        results.add(annotationMirror);
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
