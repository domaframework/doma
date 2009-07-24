package org.seasar.doma.it;

import org.seasar.doma.tool.sqlfile.ExecSqlFileTask;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.env.Env;

public class ItExecSqlFileTask extends ExecSqlFileTask {

    @Override
    protected void prepare() {
        Env.setFilePath("env_ut.txt");
        SingletonS2ContainerFactory.setConfigPath("jdbc.dicon");
        SingletonS2ContainerFactory.init();
        super.prepare();
    }

}
