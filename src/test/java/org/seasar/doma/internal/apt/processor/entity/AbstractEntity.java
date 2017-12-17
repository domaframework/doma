package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/**
 * @author taedium
 * 
 */
@Entity
public abstract class AbstractEntity {

    @Id
    Integer id;
}
