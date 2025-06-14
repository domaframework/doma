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
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;

public class ProcessingContext {

  private final ProcessingEnvironment env;

  private boolean initialized;
  private Options options;
  private Reporter reporter;
  private Resources resources;

  private ProcessingContext(ProcessingEnvironment env) {
    this.env = Objects.requireNonNull(env);
  }

  private void init() {
    if (initialized) {
      throw new AptIllegalStateException("already initialized");
    }
    var envClassName = env.getClass().getName();
    var isRunningOnEcj = envClassName.startsWith("org.eclipse.");
    var resourceDir = env.getOptions().get(Options.RESOURCES_DIR);
    var canAcceptDirectoryPath = isRunningOnEcj || resourceDir != null;
    var location = determineLocation(isRunningOnEcj);

    reporter = new Reporter(env.getMessager());
    resources =
        new Resources(
            env.getFiler(), reporter, resourceDir, canAcceptDirectoryPath, location, isDebug(env));
    options = new Options(env.getOptions(), resources);
    initialized = true;
  }

  private JavaFileManager.Location determineLocation(boolean isRunningOnEcj) {
    var unitTest = env.getOptions().get(Options.TEST_UNIT);
    var integrationTest = env.getOptions().get(Options.TEST_INTEGRATION);
    if (isRunningOnEcj
        && !Boolean.parseBoolean(unitTest)
        && !Boolean.parseBoolean(integrationTest)) {
      // Eclipse doesn’t support SOURCE_PATH, use CLASS_OUTPUT.
      return StandardLocation.CLASS_OUTPUT;
    }
    // To leverage Gradle’s incremental annotation processing, we recommend using the --source-path
    // option of javac.
    return StandardLocation.SOURCE_PATH;
  }

  private boolean isDebug(ProcessingEnvironment env) {
    var debug = env.getOptions().get(Options.DEBUG);
    return Boolean.parseBoolean(debug);
  }

  public RoundContext createRoundContext(RoundEnvironment roundEnvironment) {
    Objects.requireNonNull(roundEnvironment);
    var roundContext = new RoundContext(this, roundEnvironment);
    roundContext.init();
    return roundContext;
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

  public Elements getElements() {
    assertInitialized();
    return env.getElementUtils();
  }

  public Types getTypes() {
    assertInitialized();
    return env.getTypeUtils();
  }

  private void assertInitialized() {
    if (!initialized) {
      throw new AptIllegalStateException("not yet initialized");
    }
  }

  public static ProcessingContext of(ProcessingEnvironment env) {
    Objects.requireNonNull(env);
    var processingContext = new ProcessingContext(env);
    processingContext.init();
    return processingContext;
  }
}
