package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.IOs;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public final class FileObjects {

    public static boolean exists(String path, ProcessingEnvironment env) {
        InputStream inputStream = getResourceAsStream(path, env);
        try {
            return inputStream != null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    ignoreException(e, env);
                }
            }
        }
    }

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

    public static String getResourceAsString(String path,
            ProcessingEnvironment env) throws WrapException {
        assertNotNull(path);
        assertTrue(path.length() > 0);
        InputStream inputStream = getResourceAsStream(path, env);
        if (inputStream == null) {
            return null;
        }
        return IOs.readAsString(inputStream);
    }

    protected static void ignoreException(Exception e, ProcessingEnvironment env) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        Notifier.notify(env, Kind.NOTE, MessageCode.DOMA4021, stringWriter);
    }
}
