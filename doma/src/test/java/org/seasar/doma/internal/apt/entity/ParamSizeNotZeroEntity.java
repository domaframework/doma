package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.domain.StringDomain;

/**
 * @author taedium
 * 
 */
@Entity
public interface ParamSizeNotZeroEntity {

    StringDomain name(String s);
}
