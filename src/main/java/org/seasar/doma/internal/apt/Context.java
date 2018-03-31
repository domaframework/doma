package org.seasar.doma.internal.apt;

import javax.annotation.processing.ProcessingEnvironment;
import org.seasar.doma.internal.apt.codespec.CodeSpecs;
import org.seasar.doma.internal.apt.cttype.CtTypes;
import org.seasar.doma.internal.apt.decl.Declarations;
import org.seasar.doma.internal.apt.reflection.Reflections;

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

  public CodeSpecs getCodeSpecs() {
    return new CodeSpecs(this);
  }
}
