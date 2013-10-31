/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.it.helper.task;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.seasar.doma.it.dao.ScriptDao;
import org.seasar.doma.jdbc.ScriptException;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.env.Env;

/**
 * SQLを実行するタスクです。
 * 
 * @author taedium
 */
public class ExecSqlFileTask extends Task {

    @Override
    public void execute() throws BuildException {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    getClass().getClassLoader());
            Env.setFilePath("env_ut.txt");
            SingletonS2ContainerFactory.setConfigPath("jdbc.dicon");
            SingletonS2ContainerFactory.init();
            try {
                run();
            } finally {
                SingletonS2ContainerFactory.destroy();
            }
        } catch (Throwable throwable) {
            StringWriter writer = new StringWriter();
            throwable.printStackTrace(new PrintWriter(writer));
            throw new BuildException(writer.toString());
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    protected void run() {
        ScriptDao dao = ScriptDao.get();
        try {
            dao.drop();
        } catch (ScriptException e) {
            log(e, Project.MSG_WARN);
        }
        dao.create();
    }
}