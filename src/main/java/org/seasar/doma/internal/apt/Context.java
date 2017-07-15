/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import javax.annotation.processing.ProcessingEnvironment;

import org.seasar.doma.internal.apt.cttype.CtTypes;
import org.seasar.doma.internal.apt.decl.Declarations;
import org.seasar.doma.internal.apt.reflection.Reflections;

/**
 * @author nakamura
 *
 */
public class Context {

    private final ProcessingEnvironment env;

    public Context(ProcessingEnvironment env) {
        this.env = env;
    }

    public ProcessingEnvironment getEnv() {
        return env;
    }

    public Reflections getReflections() {
        return new Reflections(this);
    }

    public CtTypes getCtTypes() {
        return new CtTypes(this);
    }

    public Declarations getDeclarations() {
        return new Declarations(this);
    }

    public Elements getElements() {
        return new Elements(this);
    }

    public Types getTypes() {
        return new Types(this);
    }

    public Resources getResources() {
        return new Resources(this);
    }

    public Options getOptions() {
        return new Options(this);
    }

    public Notifier getNotifier() {
        return new Notifier(this);
    }
}
