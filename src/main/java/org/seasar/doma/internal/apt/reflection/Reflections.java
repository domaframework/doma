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
package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.ArrayFactory;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.BlobFactory;
import org.seasar.doma.ClobFactory;
import org.seasar.doma.Column;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.Holder;
import org.seasar.doma.HolderConverters;
import org.seasar.doma.Insert;
import org.seasar.doma.NClobFactory;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.SQLXMLFactory;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.Table;
import org.seasar.doma.TableGenerator;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

/**
 * @author nakamura
 *
 */
public class Reflections {

    private final ProcessingEnvironment env;

    public Reflections(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    public AllArgsConstructorReflection newAllArgsConstructorReflection(
            TypeElement clazz) {
        assertNotNull(clazz);
        return newInstance(clazz, Options.getLombokAllArgsConstructor(env),
                AllArgsConstructorReflection::new);
    }

    public AnnotateWithReflection newAnnotateWithReflection(
            TypeElement typeElement) {
        assertNotNull(typeElement);
        AnnotationMirror annotateWith = ElementUtil
                .getAnnotationMirror(typeElement, AnnotateWith.class, env);
        if (annotateWith == null) {
            for (AnnotationMirror annotationMirror : typeElement
                    .getAnnotationMirrors()) {
                TypeElement ownerElement = ElementUtil.toTypeElement(
                        annotationMirror.getAnnotationType().asElement(), env);
                if (ownerElement == null) {
                    continue;
                }
                annotateWith = ElementUtil.getAnnotationMirror(ownerElement,
                        AnnotateWith.class, env);
                if (annotateWith != null) {
                    break;
                }
            }
            if (annotateWith == null) {
                return null;
            }
        }
        Map<String, AnnotationValue> values = ElementUtil
                .getElementValuesWithDefaults(annotateWith, env);
        AnnotationValue annotations = values.get("annotations");
        ArrayList<AnnotationReflection> annotationsValue = new ArrayList<>();
        for (AnnotationMirror annotationMirror : AnnotationValueUtil
                .toAnnotationList(annotations)) {
            annotationsValue.add(newAnnotationReflection(annotationMirror));
        }
        return new AnnotateWithReflection(annotateWith, annotations,
                annotationsValue);
    }

    private AnnotationReflection newAnnotationReflection(
            AnnotationMirror annotationMirror) {
        assertNotNull(annotationMirror);
        Map<String, AnnotationValue> values = ElementUtil
                .getElementValuesWithDefaults(annotationMirror, env);
        return new AnnotationReflection(annotationMirror, values);
    }

    public ArrayFactoryReflection newArrayFactoryReflection(
            ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, ArrayFactory.class,
                ArrayFactoryReflection::new);
    }

    public BatchDeleteReflection newBatchDeleteReflection(
            ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, BatchDelete.class,
                BatchDeleteReflection::new);
    }

    public BatchInsertReflection newBatchInsertReflection(
            ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, BatchInsert.class,
                BatchInsertReflection::new);
    }

    public BatchUpdateReflection newBatchUpdateReflection(
            ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, BatchUpdate.class,
                BatchUpdateReflection::new);
    }

    public BlobFactoryReflection newBlobFactoryReflection(
            ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, BlobFactory.class,
                BlobFactoryReflection::new);
    }

    public ClobFactoryReflection newClobFactoryReflection(
            ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, ClobFactory.class,
                ClobFactoryReflection::new);
    }

    public ColumnReflection newColumnReflection(VariableElement field) {
        assertNotNull(field);
        return newInstance(field, Column.class, ColumnReflection::new);
    }

    public DaoReflection newDaoReflection(TypeElement interfaze) {
        assertNotNull(interfaze);
        return newInstance(interfaze, Dao.class,
                (annotationMirror, values) -> new DaoReflection(
                        annotationMirror, env, values));
    }

    public DeleteReflection newDeleteReflection(ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, Delete.class, DeleteReflection::new);
    }

    public EmbeddableReflection newEmbeddableReflection(TypeElement clazz) {
        assertNotNull(clazz);
        return newInstance(clazz, Embeddable.class, EmbeddableReflection::new);
    }

    public EntityReflection newEntityReflection(TypeElement clazz) {
        assertNotNull(clazz);
        return newInstance(clazz, Entity.class, EntityReflection::new);
    }
    
    public FunctionReflection newFunctionReflection(
            final ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, org.seasar.doma.Function.class,
                (annotationMirror, values) -> new FunctionReflection(
                        annotationMirror, method.getSimpleName().toString(),
                        values));
    }

    public HolderConvertersReflection newHolderConvertersReflection(
            TypeElement interfaze) {
        assertNotNull(interfaze);
        return newInstance(interfaze, HolderConverters.class,
                HolderConvertersReflection::new);
    }

    public HolderReflection newHolderReflection(TypeElement clazz) {
        assertNotNull(clazz);
        return newInstance(clazz, Holder.class, HolderReflection::new);
    }

    public InsertReflection newInsertReflection(ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, Insert.class, InsertReflection::new);
    }

    public NClobFactoryReflection newNClobFactoryReflection(
            ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, NClobFactory.class,
                NClobFactoryReflection::new);
    }

    public ProcedureReflection newProcedureReflection(
            ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, Procedure.class,
                (annotationMirror, values) -> new ProcedureReflection(
                        annotationMirror, method.getSimpleName().toString(),
                        values));
    }

    public ResultSetReflection newResultSetReflection(VariableElement param) {
        assertNotNull(param);
        return newInstance(param, ResultSet.class, ResultSetReflection::new);
    }

    public ScriptReflection newScriptReflection(ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, Script.class, ScriptReflection::new);
    }

    public SelectReflection newSelectReflection(ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, Select.class, SelectReflection::new);
    }

    public SequenceGeneratorReflection newSequenceGeneratorReflection(
            VariableElement field) {
        assertNotNull(field);
        return newInstance(field, SequenceGenerator.class,
                SequenceGeneratorReflection::new);
    }

    public SingletonConfigReflection newSingletonConfigReflection(
            TypeElement clazz) {
        assertNotNull(clazz);
        return newInstance(clazz, SingletonConfig.class,
                SingletonConfigReflection::new);
    }

    public SqlProcessorReflection newSqlProcessorReflection(
            ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, SqlProcessor.class,
                SqlProcessorReflection::new);
    }

    public SQLXMLFactoryReflection newSQLXMLFactoryReflection(
            ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, SQLXMLFactory.class,
                SQLXMLFactoryReflection::new);
    }

    public TableGeneratorReflection newTableGeneratorReflection(
            VariableElement field) {
        assertNotNull(field);
        return newInstance(field, TableGenerator.class,
                TableGeneratorReflection::new);
    }

    public TableReflection newTableReflection(TypeElement clazz) {
        assertNotNull(clazz);
        return newInstance(clazz, Table.class, TableReflection::new);
    }

    public UpdateReflection newUpdateReflection(ExecutableElement method) {
        assertNotNull(method);
        return newInstance(method, Update.class, UpdateReflection::new);
    }

    public ValueReflection newValueReflection(TypeElement clazz) {
        assertNotNull(clazz);
        return newInstance(clazz, Options.getLombokValue(env),
                ValueReflection::new);
    }

    private <REFLECTION> REFLECTION newInstance(Element element,
            Class<? extends Annotation> annotationClass,
            Function<AnnotationMirror, REFLECTION> function) {
        return newInstance(element, annotationClass,
                (first, second) -> function.apply(first));
    }

    private <REFLECTION> REFLECTION newInstance(Element element,
            Class<? extends Annotation> annotationClass,
            BiFunction<AnnotationMirror, Map<String, AnnotationValue>, REFLECTION> biFunction) {
        AnnotationMirror annotationMirror = ElementUtil
                .getAnnotationMirror(element, annotationClass, env);
        if (annotationMirror == null) {
            return null;
        }
        Map<String, AnnotationValue> values = ElementUtil
                .getElementValuesWithDefaults(annotationMirror, env);
        return biFunction.apply(annotationMirror, values);
    }

    private <REFLECTION> REFLECTION newInstance(Element element,
            String annotationClassName,
            BiFunction<AnnotationMirror, Map<String, AnnotationValue>, REFLECTION> biFunction) {
        AnnotationMirror annotationMirror = ElementUtil
                .getAnnotationMirror(element, annotationClassName, env);
        if (annotationMirror == null) {
            return null;
        }
        Map<String, AnnotationValue> values = ElementUtil
                .getElementValuesWithDefaults(annotationMirror, env);
        return biFunction.apply(annotationMirror, values);
    }

}
