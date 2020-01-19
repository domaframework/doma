package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.cdi.ApplicationScoped;

@Dao
@AnnotateWith(
    annotations = {@Annotation(target = AnnotationTarget.CLASS, type = ApplicationScoped.class)})
public interface ApplicationScopedDao {}
