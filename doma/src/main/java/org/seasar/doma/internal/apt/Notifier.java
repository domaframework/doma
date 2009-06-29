package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.Assertions.*;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.message.MessageCode;
import org.seasar.doma.message.Messages;


/**
 * @author taedium
 * 
 */
public final class Notifier {

    public static void debug(ProcessingEnvironment env,
            MessageCode messageCode, Object... args) {
        assertNotNull(env, messageCode, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.OTHER, Messages
                .getMessage(messageCode, args));
    }

    public static void debug(ProcessingEnvironment env, String message) {
        assertNotNull(env, message);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.OTHER, message);
    }

    public static void notify(ProcessingEnvironment env, Kind kind,
            MessageCode messageCode, Object... args) {
        assertNotNull(env, messageCode, args);
        Messager messager = env.getMessager();
        messager.printMessage(kind, Messages.getMessage(messageCode, args));
    }

    public static void notify(ProcessingEnvironment env, Kind kind,
            MessageCode messageCode, Element element, Object... args) {
        assertNotNull(env, kind, element, args);
        Messager messager = env.getMessager();
        messager
                .printMessage(kind, Messages.getMessage(messageCode, args), element);
    }

    public static void notify(ProcessingEnvironment env, AptException e) {
        assertNotNull(env, e);
        Messager messager = env.getMessager();
        messager.printMessage(e.getKind(), e.getMessage(), e.getElement());
    }

}
