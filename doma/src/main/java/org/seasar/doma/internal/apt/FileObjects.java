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
package org.seasar.doma.internal.apt;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public final class FileObjects {

    public static InputStream getResourceAsStream(String path,
            ProcessingEnvironment env) {
        Filer filer = env.getFiler();
        try {
            FileObject fileObject = filer
                    .getResource(StandardLocation.CLASS_OUTPUT, "", path);
            return fileObject.openInputStream();
        } catch (IOException e) {
            ignoreException(e, env);
        }
        return null;
    }

    protected static void ignoreException(Exception e, ProcessingEnvironment env) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        Notifier.notify(env, Kind.NOTE, DomaMessageCode.DOMA4021, stringWriter);
    }
}
