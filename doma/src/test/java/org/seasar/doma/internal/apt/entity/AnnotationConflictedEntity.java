package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;
import org.seasar.doma.domain.IntegerDomain;

@Entity
public interface AnnotationConflictedEntity {

    @Id
    @Version
    IntegerDomain id();
}
