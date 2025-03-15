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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.Annotations;
import org.seasar.doma.internal.apt.cttype.CtTypes;
import org.seasar.doma.internal.apt.decl.Declarations;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMeta;

public class RoundContext {

  private final ProcessingContext processingContext;
  private final RoundEnvironment roundEnvironment;
  private final Set<? extends TypeElement> annotationElements;
  private final List<ExternalDomainMeta> externalDomainMetaList = new ArrayList<>();
  private boolean initialized;
  private Annotations annotations;
  private Declarations declarations;
  private CtTypes ctTypes;
  private Names names;

  RoundContext(
      ProcessingContext processingContext,
      RoundEnvironment roundEnvironment,
      Set<? extends TypeElement> annotationElements) {
    this.processingContext = Objects.requireNonNull(processingContext);
    this.roundEnvironment = Objects.requireNonNull(roundEnvironment);
    this.annotationElements = Objects.requireNonNull(annotationElements);
  }

  void init() {
    if (initialized) {
      throw new AptIllegalStateException("already initialized");
    }
    annotations = new Annotations(this);
    declarations = new Declarations(this);
    ctTypes = new CtTypes(this);
    names = new Names(this);
    initialized = true;
  }

  public RoundEnvironment getRoundEnvironment() {
    assertInitialized();
    return roundEnvironment;
  }

  public MoreElements getMoreElements() {
    assertInitialized();
    return processingContext.getMoreElements();
  }

  public MoreTypes getMoreTypes() {
    assertInitialized();
    return processingContext.getMoreTypes();
  }

  public Options getOptions() {
    assertInitialized();
    return processingContext.getOptions();
  }

  public Reporter getReporter() {
    assertInitialized();
    return processingContext.getReporter();
  }

  public Resources getResources() {
    assertInitialized();
    return processingContext.getResources();
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

  public List<ExternalDomainMeta> getExternalDomainMetaList() {
    assertInitialized();
    return externalDomainMetaList;
  }

  public Set<? extends Element> getElementsAnnotatedWith(String annotationName) {
    assertInitialized();
    Objects.requireNonNull(annotationName);
    return annotationElements.stream()
        .filter(a -> a.getQualifiedName().contentEquals(annotationName))
        .findFirst()
        .map(roundEnvironment::getElementsAnnotatedWith)
        .orElse(Set.of());
  }

  private void assertInitialized() {
    if (!initialized) {
      throw new AptIllegalStateException("not yet initialized");
    }
  }
}
