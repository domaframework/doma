package org.seasar.doma.jdbc;

import java.net.URL;
import org.seasar.doma.internal.util.ResourceUtil;

public interface ScriptFileLoader {

  default URL loadAsURL(String path) {
    return ResourceUtil.getResource(path);
  }
}
