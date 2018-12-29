package org.seasar.doma.internal.apt.dao;

import java.sql.Blob;
import org.seasar.doma.BlobFactory;
import org.seasar.doma.Dao;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface BlobFactoryDao {

  @BlobFactory
  Blob create();
}
