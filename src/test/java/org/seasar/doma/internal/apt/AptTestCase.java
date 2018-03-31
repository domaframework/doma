package org.seasar.doma.internal.apt;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Consumer;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import junit.framework.AssertionFailedError;
import org.seasar.aptina.unit.AptinaTestCase;
import org.seasar.doma.Dao;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.Holder;
import org.seasar.doma.internal.Conventions;
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
    var path = getClass().getName().replace(".", "/");
    var suffix = "_" + getName().substring("test".length()) + ".txt";
    return ResourceUtil.getResourceAsString(path + suffix);
  }

  protected void assertGeneratedSource(Class<?> originalClass) throws Exception {
    var generatedClassName = getGeneratedClassName(originalClass);
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
        || originalClass.isAnnotationPresent(Holder.class)) {
      return Conventions.createDescClassName(originalClass.getName());
    }
    throw new AssertionFailedError("annotation not found.");
  }

  protected void assertMessage(Message message) {
    var diagnostics = getDiagnostics();
    if (diagnostics.size() == 1) {
      var m = extractMessage(diagnostics.get(0));
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
    var diagnostics = getDiagnostics();
    if (!diagnostics.isEmpty()) {
      fail();
    }
  }

  @Override
  protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
    List<Diagnostic<? extends JavaFileObject>> results = new ArrayList<>();
    for (var diagnostic : super.getDiagnostics()) {
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
    for (var diagnostic : getDiagnostics()) {
      return extractMessage(diagnostic);
    }
    return null;
  }

  protected Message extractMessage(Diagnostic<? extends JavaFileObject> diagnostic) {
    var message = diagnostic.getMessage(getLocale());
    var start = message.indexOf('[');
    var end = message.indexOf(']');
    if (start > -1 && end > -1) {
      var code = message.substring(start + 1, end);
      if (code.startsWith("DOMA")) {
        return Enum.valueOf(Message.class, code);
      }
    }
    return null;
  }

  @SupportedAnnotationTypes("*")
  protected static class AptProcessor extends AbstractProcessor {

    private final Consumer<Context> consumer;

    private Context ctx;

    public AptProcessor(Consumer<Context> consumer) {
      this.consumer = consumer;
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
      super.init(env);
      this.ctx = new Context(env);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
      return SourceVersion.latest();
    }

    @Override
    public boolean process(
        final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
      if (roundEnv.processingOver()) {
        return true;
      }
      this.consumer.accept(this.ctx);
      return true;
    }
  }
}
