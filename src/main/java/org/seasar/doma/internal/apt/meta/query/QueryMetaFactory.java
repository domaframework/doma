package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;

public interface QueryMetaFactory {

  QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta);
}
