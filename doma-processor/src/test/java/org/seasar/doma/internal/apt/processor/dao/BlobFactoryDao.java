package org.seasar.doma.internal.apt.processor.dao;

import java.sql.Blob;
import org.seasar.doma.BlobFactory;
import org.seasar.doma.Dao;

@Dao(config = MyConfig.class)
public interface BlobFactoryDao {

  @BlobFactory
  Blob create();
}
