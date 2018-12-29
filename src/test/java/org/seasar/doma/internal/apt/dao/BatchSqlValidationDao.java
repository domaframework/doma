package org.seasar.doma.internal.apt.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Suppress;
import org.seasar.doma.message.Message;

@Dao(config = MyConfig.class)
public interface BatchSqlValidationDao {

  void testEmbeddedVariable(String orderBy);

  @Suppress(messages = Message.DOMA4181)
  void testEmbeddedVariableSuppressed(String orderBy);

  void testIf();

  @Suppress(messages = Message.DOMA4182)
  void testIfSuppressed();

  void testFor(List<String> names);

  @Suppress(messages = Message.DOMA4183)
  void testForSuppressed(List<String> names);

  void testIfAndEmbeddedVariable(String orderBy);

  @Suppress(messages = {Message.DOMA4181, Message.DOMA4182})
  void testIfAndEmbeddedVariableSuppressed(String orderBy);

  void testPopulate(String name);
}
