package org.seasar.doma.internal.apt.processor.dao;

import java.util.Iterator;
import java.util.List;
import org.seasar.doma.Dao;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface SqlValidationDao {

  void testBindVariable(String name);

  void testBindVariable_list(List<String> names);

  void testEmbeddedVariable(String orderBy);

  void testEmbeddedVariable_unsupportedType(Integer orderBy);

  void testFor(List<String> names);

  void testFor_notIterable(Iterator<String> names);

  void testFor_noTypeArgument(@SuppressWarnings("rawtypes") List names);

  void testExpand(String name);

  void testPopulate(String name);
}
