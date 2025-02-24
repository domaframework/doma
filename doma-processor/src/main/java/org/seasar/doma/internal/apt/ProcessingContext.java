/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt;

import java.util.Objects;
import javax.annotation.processing.ProcessingEnvironment;

public class ProcessingContext {

  private final ProcessingEnvironment env;

  private boolean initialized;
  private MoreElements moreElements;
  private MoreTypes moreTypes;
  private Options options;
  private Reporter reporter;
  private Resources resources;

  public ProcessingContext(ProcessingEnvironment env) {
    this.env = Objects.requireNonNull(env);
  }

  public void init() {
    if (initialized) {
      throw new AptIllegalStateException("already initialized");
    }
    moreElements = new MoreElements(this, env.getElementUtils());
    moreTypes = new MoreTypes(this, env.getTypeUtils());
    reporter = new Reporter(env.getMessager());
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

  private void assertInitialized() {
    if (!initialized) {
      throw new AptIllegalStateException("not yet initialized");
    }
  }
}
