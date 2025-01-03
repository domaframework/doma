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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.message.MessageResource;

public final class Reporter {

  private final Messager messager;

  Reporter(Messager messager) {
    assertNotNull(messager);
    this.messager = messager;
  }

  public void debug(MessageResource messageResource, Object[] args) {
    assertNotNull(messageResource, args);
    messager.printMessage(Kind.OTHER, messageResource.getMessage(args));
  }

  public void debug(CharSequence message) {
    assertNotNull(message);
    messager.printMessage(Kind.OTHER, message);
  }

  public void report(Kind kind, MessageResource messageResource, Object[] args) {
    assertNotNull(messageResource, args);
    messager.printMessage(kind, messageResource.getMessage(args));
  }

  public void report(Kind kind, MessageResource messageResource, Element element, Object[] args) {
    assertNotNull(kind, element, args);
    messager.printMessage(kind, messageResource.getMessage(args), element);
  }

  public void report(Kind kind, String message, Element element) {
    assertNotNull(kind, message, element);
    messager.printMessage(kind, message, element);
  }

  public void report(AptException e) {
    assertNotNull(e);
    messager.printMessage(
        e.getKind(),
        e.getMessage(),
        e.getElement(),
        e.getAnnotationMirror(),
        e.getAnnotationValue());
  }
}
