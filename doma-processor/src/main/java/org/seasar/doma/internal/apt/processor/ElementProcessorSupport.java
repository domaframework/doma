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
package org.seasar.doma.internal.apt.processor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.internal.apt.*;
import org.seasar.doma.internal.apt.meta.ElementMeta;
import org.seasar.doma.message.Message;

class ElementProcessorSupport<M extends ElementMeta> {

  private final RoundContext ctx;
  private final Class<? extends Annotation> supportedAnnotationType;

  ElementProcessorSupport(RoundContext ctx, Class<? extends Annotation> supportedAnnotationType) {
    this.ctx = Objects.requireNonNull(ctx);
    this.supportedAnnotationType = Objects.requireNonNull(supportedAnnotationType);
  }

  List<M> processTypeElements(Set<? extends Element> elements, Function<TypeElement, M> handler) {
    return ElementFilter.typesIn(elements).stream()
        .map(it -> handleTypeElement(it, handler))
        .filter(Objects::nonNull)
        .filter(it -> !it.isError())
        .toList();
  }

  List<M> processMethodElements(
      Set<? extends Element> elements, Function<ExecutableElement, M> handler) {
    return ElementFilter.methodsIn(elements).stream()
        .map(it -> handleMethodElement(it, handler))
        .filter(Objects::nonNull)
        .filter(it -> !it.isError())
        .toList();
  }

  private M handleTypeElement(TypeElement element, Function<TypeElement, M> handler) {
    return handleElement(element, handler, () -> element.getQualifiedName().toString());
  }

  private M handleMethodElement(ExecutableElement element, Function<ExecutableElement, M> handler) {
    return handleElement(
        element,
        handler,
        () -> {
          var owner = element.getEnclosingElement();
          return owner + "#" + element.getSimpleName();
        });
  }

  private <E extends Element> M handleElement(
      E element, Function<E, M> handler, Supplier<String> elementNameSupplier) {
    var annotation = element.getAnnotation(supportedAnnotationType);
    if (annotation == null) {
      return null;
    }
    if (ctx.getOptions().isDebugEnabled()) {
      ctx.getReporter()
          .debug(Message.DOMA4090, new Object[] {getClass().getName(), elementNameSupplier.get()});
    }
    M result = null;
    try {
      if (ctx.getOptions().isTraceEnabled()) {
        var startTime = System.nanoTime();
        result = handler.apply(element);
        var endTime = System.nanoTime();
        var execTimeMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        ctx.getReporter()
            .debug(
                Message.DOMA4463,
                new Object[] {execTimeMillis, getClass().getName(), elementNameSupplier.get()});
      } else {
        result = handler.apply(element);
      }
    } catch (AptException e) {
      ctx.getReporter().report(e);
    } catch (AptIllegalOptionException e) {
      ctx.getReporter().report(Kind.ERROR, e.getMessage(), element);
      throw e;
    } catch (AptIllegalStateException e) {
      String stackTrace = getStackTraceAsString(e);
      ctx.getReporter().report(Kind.ERROR, Message.DOMA4039, element, new Object[] {stackTrace});
      throw e;
    } catch (RuntimeException | AssertionError e) {
      String stackTrace = getStackTraceAsString(e);
      ctx.getReporter().report(Kind.ERROR, Message.DOMA4016, element, new Object[] {stackTrace});
      throw e;
    }
    if (ctx.getOptions().isDebugEnabled()) {
      ctx.getReporter()
          .debug(Message.DOMA4091, new Object[] {getClass().getName(), elementNameSupplier.get()});
    }
    return result;
  }

  private String getStackTraceAsString(Throwable throwable) {
    Writer stringWriter = new StringWriter();
    throwable.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }
}
