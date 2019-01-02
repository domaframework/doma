package org.seasar.doma.internal.apt;

import javax.annotation.processing.ProcessingEnvironment;
import org.seasar.doma.internal.apt.annot.Annotations;
import org.seasar.doma.internal.apt.decl.Declarations;

public class Context {

  private final ProcessingEnvironment env;

  public Context(ProcessingEnvironment env) {
    this.env = env;
  }

  public ProcessingEnvironment getEnv() {
    return env;
  }

  public Elements getElements() {
    return new Elements(this);
  }

  public Types getTypes() {
    return new Types(this);
  }

  public Options getOptions() {
    return new Options(this);
  }

  public Notifier getNotifier() {
    return new Notifier(this);
  }

  public Resources getResources() {
    return new Resources(this);
  }

  public Annotations getAnnotations() {
    return new Annotations(this);
  }

  public Declarations getDeclarations() {
    return new Declarations(this);
  }
}
