/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageResource;

/**
 * @author taedium
 * 
 */
public class AptException extends DomaException {

    private static final long serialVersionUID = 1L;

    private final Kind kind;

    private final Element element;

    private AnnotationMirror annotationMirror;

    private AnnotationValue annotationValue;

    public AptException(MessageResource messageResource, Element element) {
        this(messageResource, element, new Object[] {});
    }

    public AptException(MessageResource messageResource, Element element, Object[] args) {
        this(messageResource, Kind.ERROR, element, null, null, null, args);
    }

    public AptException(MessageResource messageResource, Element element, Throwable cause) {
        this(messageResource, element, cause, new Object[] {});
    }

    public AptException(MessageResource messageResource, Element element, Throwable cause,
            Object[] args) {
        this(messageResource, Kind.ERROR, element, null, null, cause, args);
    }

    public AptException(MessageResource messageResource, Element element,
            AnnotationMirror annotationMirror) {
        this(messageResource, element, annotationMirror, new Object[] {});
    }

    public AptException(MessageResource messageResource, Element element,
            AnnotationMirror annotationMirror, Object[] args) {
        this(messageResource, Kind.ERROR, element, annotationMirror, null, null, args);
    }

    public AptException(MessageResource messageResource, Element element,
            AnnotationMirror annotationMirror, AnnotationValue annotationValue) {
        this(messageResource, element, annotationMirror, annotationValue, new Object[] {});
    }

    public AptException(MessageResource messageResource, Element element,
            AnnotationMirror annotationMirror, AnnotationValue annotationValue, Object[] args) {
        this(messageResource, Kind.ERROR, element, annotationMirror, annotationValue, null, args);
    }

    private AptException(MessageResource messageResource, Kind kind, Element element,
            AnnotationMirror annotationMirror, AnnotationValue annotationValue, Throwable cause,
            Object[] args) {
        super(messageResource, cause, args);
        this.kind = kind;
        this.element = element;
        this.annotationMirror = annotationMirror;
        this.annotationValue = annotationValue;
    }

    public Kind getKind() {
        return kind;
    }

    public Element getElement() {
        return element;
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public void setAnnotationMirror(AnnotationMirror annotationMirror) {
        this.annotationMirror = annotationMirror;
    }

    public AnnotationValue getAnnotationValue() {
        return annotationValue;
    }

    public void setAnnotationValue(AnnotationValue annotationValue) {
        this.annotationValue = annotationValue;
    }

}
