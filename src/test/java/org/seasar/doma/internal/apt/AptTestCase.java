package org.seasar.doma.internal.apt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import junit.framework.AssertionFailedError;
import org.seasar.aptina.unit.AptinaTestCase;
import org.seasar.doma.Dao;
import org.seasar.doma.Domain;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.MetaUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.message.Message;

public abstract class AptTestCase extends AptinaTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addSourcePath("src/test/java");
    addSourcePath("src/test/resources");
    setCharset("UTF-8");
    setLocale(Locale.JAPAN);
    TimeZone.setDefault(TimeZone.getTimeZone("GMT+9"));
  }

  @Override
  protected void tearDown() throws Exception {
    TimeZone.setDefault(null);
    super.tearDown();
  }

  protected String getExpectedContent() throws Exception {
    String path = getClass().getName().replace(".", "/");
    String suffix = "_" + getName().substring("test".length()) + ".txt";
    return ResourceUtil.getResourceAsString(path + suffix);
  }

  protected void assertGeneratedSource(Class<?> originalClass) throws Exception {
    String generatedClassName = getGeneratedClassName(originalClass);
    try {
      assertEqualsGeneratedSource(getExpectedContent(), generatedClassName);
    } catch (AssertionFailedError error) {
      System.out.println(getGeneratedSource(generatedClassName));
      throw error;
    }
  }

  protected String getGeneratedClassName(Class<?> originalClass) {
    if (originalClass.isAnnotationPresent(Dao.class)) {
      return originalClass.getName() + Options.Constants.DEFAULT_DAO_SUFFIX;
    }
    if (originalClass.isAnnotationPresent(Entity.class)
        || originalClass.isAnnotationPresent(Embeddable.class)
        || originalClass.isAnnotationPresent(Domain.class)
        || originalClass.isAnnotationPresent(ExternalDomain.class)) {
      return MetaUtil.toFullMetaName(originalClass.getName());
    }
    throw new AssertionFailedError("annotation not found.");
  }

  protected void assertMessage(Message message) {
    List<Diagnostic<? extends JavaFileObject>> diagnostics = getDiagnostics();
    if (diagnostics.size() == 1) {
      Message m = extractMessage(diagnostics.get(0));
      if (m == null) {
        fail();
      }
      if (message == m) {
        return;
      }
      fail("actual message id: " + m.name());
    }
    fail();
  }

  protected void assertNoMessage() {
    List<Diagnostic<? extends JavaFileObject>> diagnostics = getDiagnostics();
    if (!diagnostics.isEmpty()) {
      fail();
    }
  }

  @Override
  protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
    List<Diagnostic<? extends JavaFileObject>> results =
        new ArrayList<Diagnostic<? extends JavaFileObject>>();
    for (Diagnostic<? extends JavaFileObject> diagnostic : super.getDiagnostics()) {
      switch (diagnostic.getKind()) {
        case ERROR:
        case WARNING:
        case MANDATORY_WARNING:
          results.add(diagnostic);
          break;
        default:
          // do nothing
      }
    }
    return results;
  }

  protected Message getMessageCode() {
    for (Diagnostic<? extends JavaFileObject> diagnostic : getDiagnostics()) {
      return extractMessage(diagnostic);
    }
    return null;
  }

  protected Message extractMessage(Diagnostic<? extends JavaFileObject> diagnostic) {
    String message = diagnostic.getMessage(getLocale());
    int start = message.indexOf('[');
    int end = message.indexOf(']');
    if (start > -1 && end > -1) {
      String code = message.substring(start + 1, end);
      if (code.startsWith("DOMA")) {
        return Enum.valueOf(Message.class, code);
      }
    }
    return null;
  }

  protected ExecutableElement createMethodElement(
      Class<?> clazz, String methodName, Class<?>... parameterClasses) {
    ProcessingEnvironment env = getProcessingEnvironment();
    TypeElement typeElement = ElementUtil.getTypeElement(clazz, env);
    for (TypeElement t = typeElement;
        t != null && t.asType().getKind() != TypeKind.NONE;
        t = TypeMirrorUtil.toTypeElement(t.getSuperclass(), env)) {
      for (ExecutableElement methodElement : ElementFilter.methodsIn(t.getEnclosedElements())) {
        if (!methodElement.getSimpleName().contentEquals(methodName)) {
          continue;
        }
        List<? extends VariableElement> parameterElements = methodElement.getParameters();
        if (parameterElements.size() != parameterClasses.length) {
          continue;
        }
        int i = 0;
        for (Iterator<? extends VariableElement> it = parameterElements.iterator();
            it.hasNext(); ) {
          TypeMirror parameterType = it.next().asType();
          Class<?> parameterClass = parameterClasses[i];
          if (!TypeMirrorUtil.isSameType(parameterType, parameterClass, env)) {
            return null;
          }
        }
        return methodElement;
      }
    }
    return null;
  }

  protected LinkedHashMap<String, TypeMirror> createParameterTypeMap(
      ExecutableElement methodElement) {
    LinkedHashMap<String, TypeMirror> result = new LinkedHashMap<String, TypeMirror>();
    for (VariableElement parameter : methodElement.getParameters()) {
      String name = parameter.getSimpleName().toString();
      TypeMirror type = parameter.asType();
      result.put(name, type);
    }
    return result;
  }
}
