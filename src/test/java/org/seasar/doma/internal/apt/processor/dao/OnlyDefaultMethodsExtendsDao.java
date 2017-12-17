package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;

/**
 * @author nakamura
 *
 */
@Dao(config = MyConfig.class)
public interface OnlyDefaultMethodsExtendsDao extends OnlyDefaultMethods<String> {

}
