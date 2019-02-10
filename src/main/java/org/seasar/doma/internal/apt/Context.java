package org.seasar.doma.internal.apt;

import javax.annotation.processing.ProcessingEnvironment;
import org.seasar.doma.internal.apt.annot.Annotations;
import org.seasar.doma.internal.apt.cttype.CtTypes;
import org.seasar.doma.internal.apt.decl.Declarations;

public class Context {

  private final ProcessingEnvironment env;

  private boolean initialized;

  private Elements elements;

  private Types types;

  private Options options;

  private Reporter reporter;

  private Resources resources;

  private Annotations annotations;

  private Declarations declarations;

  private CtTypes ctTypes;

  public Context(ProcessingEnvironment env) {
    this.env = env;
  }

  public void init() {
    if (initialized) {
      throw new AptIllegalStateException("already initialized");
    }
    elements = new Elements(this, env);
    types = new Types(this, env);
    options = new Options(this, env);
    reporter = new Reporter(env);
    resources = new Resources(this, env);
    annotations = new Annotations(this);
    declarations = new Declarations(this);
    ctTypes = new CtTypes(this);
    initialized = true;
  }

  public Elements getElements() {
    assertInitialized();
    return elements;
  }

  public Types getTypes() {
    assertInitialized();
    return types;
  }

  public Options getOptions() {
    assertInitialized();
    return options;
  }

  public Reporter getReporter() {
    assertInitialized();
    return reporter;
  }

  public Resources getResources() {
    assertInitialized();
    return resources;
  }

  public Annotations getAnnotations() {
    assertInitialized();
    return annotations;
  }

  public Declarations getDeclarations() {
    assertInitialized();
    return declarations;
  }

  public CtTypes getCtTypes() {
    assertInitialized();
    return ctTypes;
  }

  private void assertInitialized() {
    if (!initialized) {
      throw new AptIllegalStateException("not yet initialized");
    }
  }
}
