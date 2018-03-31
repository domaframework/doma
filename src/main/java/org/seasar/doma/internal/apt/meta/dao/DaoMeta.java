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

  private final TypeElement daoElement;

  private AnnotateWithAnnot annotateWithAnnot;

  private ParentDaoMeta parentDaoMeta;

  private ConfigMeta configMeta;

  public DaoMeta(DaoAnnot daoAnnot, TypeElement daoElement) {
    assertNotNull(daoAnnot, daoElement);
    this.daoAnnot = daoAnnot;
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
    return daoAnnot.hasUserDefinedConfig();
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

  public List<AnnotationAnnot> getAnnotationAnnots(AnnotationTarget target) {
    assertNotNull(target);
    if (annotateWithAnnot == null || annotateWithAnnot.getAnnotationsValue() == null) {
      return Collections.emptyList();
    }
    List<AnnotationAnnot> results = new ArrayList<>();
    for (AnnotationAnnot annotationAnnot : annotateWithAnnot.getAnnotationsValue()) {
      if (target.name().contentEquals(annotationAnnot.getTargetValue().getSimpleName())) {
        results.add(annotationAnnot);
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
