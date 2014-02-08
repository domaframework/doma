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

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.Suppress;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.ForNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class BatchSqlValidator extends SqlValidator {

    protected boolean embeddedVariableWarningNotified;

    protected boolean ifWarningNotified;

    protected boolean forWarningNotified;

    protected Suppress suppress;

    public BatchSqlValidator(ProcessingEnvironment env,
            ExecutableElement methodElement,
            Map<String, TypeMirror> parameterTypeMap, String path) {
        super(env, methodElement, parameterTypeMap, path);
        suppress = methodElement.getAnnotation(Suppress.class);
    }

    @Override
    public Void visitEmbeddedVariableNode(EmbeddedVariableNode node, Void p) {
        if (!isSuppressed(Message.DOMA4181) && !embeddedVariableWarningNotified) {
            Notifier.notify(env, Kind.WARNING, Message.DOMA4181, methodElement,
                    path);
            embeddedVariableWarningNotified = true;
        }
        return super.visitEmbeddedVariableNode(node, p);
    }

    @Override
    public Void visitIfNode(IfNode node, Void p) {
        if (!isSuppressed(Message.DOMA4182) && !ifWarningNotified) {
            Notifier.notify(env, Kind.WARNING, Message.DOMA4182, methodElement,
                    path);
            ifWarningNotified = true;
        }
        return super.visitIfNode(node, p);
    }

    @Override
    public Void visitForNode(ForNode node, Void p) {
        if (!isSuppressed(Message.DOMA4183) && !forWarningNotified) {
            Notifier.notify(env, Kind.WARNING, Message.DOMA4183, methodElement,
                    path);
            forWarningNotified = true;
        }
        return super.visitForNode(node, p);
    }

    protected boolean isSuppressed(Message message) {
        if (suppress != null) {
            for (Message suppressMessage : suppress.messages()) {
                if (suppressMessage == message) {
                    return true;
                }
            }
        }
        return false;
    }

}
