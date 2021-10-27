package org.seasar.doma.it.other.postgresql;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.VeryLongCharactersNamedTableDao;
import org.seasar.doma.it.dao.VeryLongCharactersNamedTableDaoImpl;
import org.seasar.doma.it.entity.VeryLongCharactersNamedTable;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
@Run(onlyIf = {Dbms.POSTGRESQL})
public class LongNameSerialSequenceTest {

  @Test
  public void testInsert(Config config) throws Exception {
    VeryLongCharactersNamedTableDao dao = new VeryLongCharactersNamedTableDaoImpl(config);
    VeryLongCharactersNamedTable entity = new VeryLongCharactersNamedTable();
    entity.setValue("foo");
    dao.insert(entity);
  }

  @Test
  public void testBatchInsert(Config config) throws Exception {
    VeryLongCharactersNamedTableDao dao = new VeryLongCharactersNamedTableDaoImpl(config);
    VeryLongCharactersNamedTable entity1 = new VeryLongCharactersNamedTable();
    entity1.setValue("foo");
    VeryLongCharactersNamedTable entity2 = new VeryLongCharactersNamedTable();
    entity2.setValue("bar");
    dao.insert(Arrays.asList(entity1, entity2));
  }
}
