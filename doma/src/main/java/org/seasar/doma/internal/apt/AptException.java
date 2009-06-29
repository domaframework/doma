package org.seasar.doma.internal.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class AptException extends DomaException {

    private static final long serialVersionUID = 1L;

    protected final Kind kind;

    protected final Element element;

    public AptException(MessageCode messageCode, ProcessingEnvironment env,
            Element element, Object... args) {
        this(messageCode, env, element, null, args);
    }

    public AptException(MessageCode messageCode, ProcessingEnvironment env,
            Kind kind, Element element, Object... args) {
        this(messageCode, env, kind, element, null, args);
    }

    public AptException(MessageCode messageCode, ProcessingEnvironment env,
            Element element, Throwable cause, Object... args) {
        this(messageCode, env, Kind.ERROR, element, cause, args);
    }

    public AptException(MessageCode messageCode, ProcessingEnvironment env,
            Kind kind, Element element, Throwable cause, Object... args) {
        super(messageCode, cause, args);
        this.kind = kind;
        this.element = element;
        if (Options.isDebugEnabled(env)) {
            Notifier.debug(env, MessageCode.DOMA4074, messageCode, cause);
        }
    }

    public Kind getKind() {
        return kind;
    }

    public Element getElement() {
        return element;
    }

}
