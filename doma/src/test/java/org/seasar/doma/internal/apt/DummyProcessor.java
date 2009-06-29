package org.seasar.doma.internal.apt;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * @author taedium
 * 
 */
public class DummyProcessor extends AbstractProcessor {

    public ProcessingEnvironment getProcessingEnvironment() {
        return processingEnv;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        return true;
    }

}
