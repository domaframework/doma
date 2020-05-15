package org.seasar.doma.internal.apt.annot;

import javax.lang.model.element.AnnotationMirror;

public class EmbeddableAnnot extends AbstractAnnot {

  static final String METAMODEL = "metamodel";

  private final MetamodelAnnot metamodelAnnot;

  EmbeddableAnnot(AnnotationMirror annotationMirror, MetamodelAnnot metamodelAnnot) {
    super(annotationMirror);
    this.metamodelAnnot = metamodelAnnot;
  }

  public MetamodelAnnot getMetamodelValue() {
    return metamodelAnnot;
  }
}
