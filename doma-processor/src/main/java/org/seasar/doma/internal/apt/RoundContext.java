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
  private final Annotations annotations;
  private final Declarations declarations;
  private final CtTypes ctTypes;
  private final Names names;
  private final List<ExternalDomainMeta> externalDomainMetaList = new ArrayList<>();

  public RoundContext(
      ProcessingContext processingContext,
      RoundEnvironment roundEnvironment,
      Set<? extends TypeElement> annotationElements) {
    this.processingContext = Objects.requireNonNull(processingContext);
    this.roundEnvironment = Objects.requireNonNull(roundEnvironment);
    this.annotationElements = Objects.requireNonNull(annotationElements);
    this.annotations = new Annotations(this);
    this.declarations = new Declarations(this);
    this.ctTypes = new CtTypes(this);
    this.names = new Names(this);
  }

  public RoundEnvironment getRoundEnvironment() {
    return roundEnvironment;
  }

  public MoreElements getMoreElements() {
    return processingContext.getMoreElements();
  }

  public MoreTypes getMoreTypes() {
    return processingContext.getMoreTypes();
  }

  public Options getOptions() {
    return processingContext.getOptions();
  }

  public Reporter getReporter() {
    return processingContext.getReporter();
  }

  public Resources getResources() {
    return processingContext.getResources();
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public Declarations getDeclarations() {
    return declarations;
  }

  public CtTypes getCtTypes() {
    return ctTypes;
  }

  public Names getNames() {
    return names;
  }

  public List<ExternalDomainMeta> getExternalDomainMetaList() {
    return externalDomainMetaList;
  }

  public Set<? extends Element> getElementsAnnotatedWith(String annotationName) {
    Objects.requireNonNull(annotationName);
    return annotationElements.stream()
        .filter(a -> a.getQualifiedName().contentEquals(annotationName))
        .findFirst()
        .map(roundEnvironment::getElementsAnnotatedWith)
        .orElse(Set.of());
  }
}
