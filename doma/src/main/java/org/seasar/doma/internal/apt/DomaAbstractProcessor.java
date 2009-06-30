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

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;

import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public abstract class DomaAbstractProcessor extends AbstractProcessor {

    protected void generate(Generator generator, String name,
            TypeElement originalTypeElement) {
        if (Options.isDebugEnabled(processingEnv)) {
            Notifier.debug(processingEnv, MessageCode.DOMA4012, name);
        }
        Filer filer = processingEnv.getFiler();
        Printer printer = null;
        try {
            FileObject fileObject = filer
                    .createSourceFile(name, originalTypeElement);
            printer = new Printer(fileObject.openWriter());
            generator.generate(printer);
        } catch (IOException e) {
            throw new AptException(MessageCode.DOMA4011, processingEnv,
                    originalTypeElement, e, originalTypeElement, name, e);
        } finally {
            if (printer != null) {
                printer.close();
            }
        }
        if (Options.isDebugEnabled(processingEnv)) {
            Notifier.debug(processingEnv, MessageCode.DOMA4013, name);
        }
    }

}
