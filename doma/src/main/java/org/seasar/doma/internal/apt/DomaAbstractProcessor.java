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
