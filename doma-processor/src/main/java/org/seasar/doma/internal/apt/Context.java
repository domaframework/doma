package org.seasar.doma.internal.apt;

import javax.annotation.processing.ProcessingEnvironment;
import org.seasar.doma.internal.apt.annot.Annotations;
import org.seasar.doma.internal.apt.cttype.CtTypes;
import org.seasar.doma.internal.apt.decl.Declarations;

public class Context {

  private final ProcessingEnvironment env;

  private boolean initialized;

  private MoreElements moreElements;

  private MoreTypes moreTypes;

  private Options options;

  private Reporter reporter;

  private Resources resources;

  private Annotations annotations;

  private Declarations declarations;

  private CtTypes ctTypes;

  private Names names;

  public Context(ProcessingEnvironment env) {
    this.env = env;
  }

  public void init() {
    if (initialized) {
      throw new AptIllegalStateException("already initialized");
    }
    moreElements = new MoreElements(this, env.getElementUtils());
    moreTypes = new MoreTypes(this, env.getTypeUtils());
    reporter = new Reporter(env.getMessager());
    annotations = new Annotations(this);
    declarations = new Declarations(this);
    ctTypes = new CtTypes(this);
    names = new Names(this);
    resources = new Resources(env.getFiler(), env.getOptions().get(Options.RESOURCES_DIR));
    options = new Options(env.getOptions(), resources);
    initialized = true;
  }

  public MoreElements getMoreElements() {
    assertInitialized();
    return moreElements;
  }

  public MoreTypes getMoreTypes() {
    assertInitialized();
    return moreTypes;
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

  public Names getNames() {
    assertInitialized();
    return names;
  }

  private void assertInitialized() {
    if (!initialized) {
      throw new AptIllegalStateException("not yet initialized");
    }
  }
}
