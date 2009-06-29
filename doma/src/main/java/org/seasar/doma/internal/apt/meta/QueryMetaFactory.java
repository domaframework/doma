package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;

/**
 * @author taedium
 * 
 */
public interface QueryMetaFactory {

    QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta);
}
