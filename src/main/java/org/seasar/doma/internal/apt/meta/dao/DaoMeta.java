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

public class DaoMeta implements TypeElementMeta {

  private final List<QueryMeta> queryMetas = new ArrayList<>();

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
    if (annotateWithReflection == null || annotateWithReflection.getAnnotationsValue() == null) {
      return Collections.emptyList();
    }
    List<AnnotationReflection> results = new ArrayList<>();
    for (AnnotationReflection annotationReflection : annotateWithReflection.getAnnotationsValue()) {
      if (target.name().contentEquals(annotationReflection.getTargetValue().getSimpleName())) {
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
