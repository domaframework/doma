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
package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.AnnotateWith;
import org.seasar.doma.AssociationLinker;
import org.seasar.doma.Column;
import org.seasar.doma.Dao;
import org.seasar.doma.DataType;
import org.seasar.doma.Domain;
import org.seasar.doma.DomainConverters;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.ResultSet;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Sql;
import org.seasar.doma.Table;
import org.seasar.doma.TableGenerator;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class Annotations {

  private final RoundContext ctx;

  public Annotations(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
  }

  public AggregateStrategyAnnot newAggregateStrategyAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(typeElement, AggregateStrategy.class, AggregateStrategyAnnot::new);
  }

  public AllArgsConstructorAnnot newAllArgsConstructorAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(
        typeElement, ctx.getOptions().getLombokAllArgsConstructor(), AllArgsConstructorAnnot::new);
  }

  public List<AnnotateWithAnnot> newAnnotateWithAnnots(TypeElement typeElement) {
    assertNotNull(typeElement);
    List<AnnotationMirror> annotateWiths = new ArrayList<>();
    {
      AnnotationMirror annotateWith =
          ctx.getMoreElements().getAnnotationMirror(typeElement, AnnotateWith.class);
      if (annotateWith != null) {
        annotateWiths.add(annotateWith);
      }
    }
    for (AnnotationMirror annotationMirror : typeElement.getAnnotationMirrors()) {
      TypeElement ownerElement =
          ctx.getMoreElements().toTypeElement(annotationMirror.getAnnotationType().asElement());
      if (ownerElement == null) {
        continue;
      }
      AnnotationMirror annotateWith =
          ctx.getMoreElements().getAnnotationMirror(ownerElement, AnnotateWith.class);
      if (annotateWith != null) {
        annotateWiths.add(annotateWith);
      }
    }
    return annotateWiths.stream()
        .map(
            annotateWith -> {
              Map<String, AnnotationValue> values =
                  ctx.getMoreElements().getValuesWithDefaults(annotateWith);
              AnnotationValue annotations = values.get(AnnotateWithAnnot.ANNOTATIONS);
              ArrayList<AnnotationAnnot> annotationsValue = new ArrayList<>();
              for (AnnotationMirror annotationMirror :
                  AnnotationValueUtil.toAnnotationList(annotations)) {
                annotationsValue.add(newAnnotationAnnot(annotationMirror));
              }
              return new AnnotateWithAnnot(annotateWith, annotations, annotationsValue);
            })
        .collect(Collectors.toList());
  }

  private AnnotationAnnot newAnnotationAnnot(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    return newInstance(annotationMirror, AnnotationAnnot::new);
  }

  public AssociationLinkerAnnot newAssociationLinkerAnnot(VariableElement field) {
    assertNotNull(field);
    return newInstance(field, AssociationLinker.class, AssociationLinkerAnnot::new);
  }

  public ArrayFactoryAnnot newArrayFactoryAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return newInstance(annotation, ArrayFactoryAnnot::new);
  }

  public BatchDeleteAnnot newBatchDeleteAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return newInstance(annotation, BatchDeleteAnnot::new);
  }

  public BatchInsertAnnot newBatchInsertAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return newInstance(annotation, BatchInsertAnnot::new);
  }

  public BatchUpdateAnnot newBatchUpdateAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return newInstance(annotation, BatchUpdateAnnot::new);
  }

  public BlobFactoryAnnot newBlobFactoryAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return new BlobFactoryAnnot(annotation);
  }

  public ClobFactoryAnnot newClobFactoryAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return new ClobFactoryAnnot(annotation);
  }

  public ColumnAnnot newColumnAnnot(VariableElement field) {
    assertNotNull(field);
    return newInstance(field, Column.class, ColumnAnnot::new);
  }

  public DaoAnnot newDaoAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(typeElement, Dao.class, DaoAnnot::new);
  }

  public DataTypeAnnot newDataTypeAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(typeElement, DataType.class, DataTypeAnnot::new);
  }

  public DomainAnnot newDomainAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(typeElement, Domain.class, DomainAnnot::new);
  }

  public DomainConvertersAnnot newDomainConvertersAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(typeElement, DomainConverters.class, DomainConvertersAnnot::new);
  }

  public DeleteAnnot newDeleteAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    ReturningAnnot returningAnnot = newReturningAnnot(annotation, ModifyAnnot.RETURNING);
    Map<String, AnnotationValue> valuesWithDefaults =
        ctx.getMoreElements().getValuesWithDefaults(annotation);
    return new DeleteAnnot(annotation, returningAnnot, valuesWithDefaults);
  }

  public EmbeddableAnnot newEmbeddableAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    AnnotationMirror embeddableMirror =
        ctx.getMoreElements().getAnnotationMirror(typeElement, Embeddable.class);
    if (embeddableMirror == null) {
      return null;
    }
    Map<String, AnnotationValue> valuesWithoutDefaults =
        ctx.getMoreElements().getValuesWithoutDefaults(embeddableMirror);
    AnnotationValue metamodel = valuesWithoutDefaults.get(EmbeddableAnnot.METAMODEL);
    MetamodelAnnot metamodelAnnot = null;
    if (metamodel != null) {
      AnnotationMirror metamodelMirror = AnnotationValueUtil.toAnnotation(metamodel);
      if (metamodelMirror != null) {
        metamodelAnnot = newMetamodelAnnot(metamodelMirror);
      }
    }
    return new EmbeddableAnnot(embeddableMirror, metamodelAnnot);
  }

  public EntityAnnot newEntityAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    AnnotationMirror entityMirror =
        ctx.getMoreElements().getAnnotationMirror(typeElement, Entity.class);
    Map<String, AnnotationValue> valuesWithoutDefaults =
        ctx.getMoreElements().getValuesWithoutDefaults(entityMirror);
    AnnotationValue metamodel = valuesWithoutDefaults.get(EntityAnnot.METAMODEL);
    MetamodelAnnot metamodelAnnot = null;
    if (metamodel != null) {
      AnnotationMirror metamodelMirror = AnnotationValueUtil.toAnnotation(metamodel);
      if (metamodelMirror != null) {
        metamodelAnnot = newMetamodelAnnot(metamodelMirror);
      }
    }
    Map<String, AnnotationValue> valuesWithDefaults =
        ctx.getMoreElements().getValuesWithDefaults(entityMirror);
    return new EntityAnnot(entityMirror, metamodelAnnot, valuesWithDefaults);
  }

  public FunctionAnnot newFunctionAnnot(final AnnotationMirror annotation, String name) {
    assertNotNull(annotation);
    return newInstance(
        annotation,
        (annotationMirror, values) -> new FunctionAnnot(annotationMirror, values, name));
  }

  public InsertAnnot newInsertAnnot(AnnotationMirror annotation) {
    ReturningAnnot returningAnnot = newReturningAnnot(annotation, ModifyAnnot.RETURNING);
    Map<String, AnnotationValue> valuesWithDefaults =
        ctx.getMoreElements().getValuesWithDefaults(annotation);
    return new InsertAnnot(annotation, returningAnnot, valuesWithDefaults);
  }

  public MultiInsertAnnot newMultiInsertAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    ReturningAnnot returningAnnot = newReturningAnnot(annotation, ModifyAnnot.RETURNING);
    Map<String, AnnotationValue> valuesWithDefaults =
        ctx.getMoreElements().getValuesWithDefaults(annotation);
    return new MultiInsertAnnot(annotation, returningAnnot, valuesWithDefaults);
  }

  public MetamodelAnnot newMetamodelAnnot(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    return newInstance(annotationMirror, MetamodelAnnot::new);
  }

  public NClobFactoryAnnot newNClobFactoryAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return new NClobFactoryAnnot(annotation);
  }

  public ProcedureAnnot newProcedureAnnot(AnnotationMirror annotation, String name) {
    assertNotNull(annotation);
    return newInstance(
        annotation,
        (annotationMirror, values) -> new ProcedureAnnot(annotationMirror, values, name));
  }

  public ResultSetAnnot newResultSetAnnot(VariableElement param) {
    assertNotNull(param);
    return newInstance(param, ResultSet.class, ResultSetAnnot::new);
  }

  private ReturningAnnot newReturningAnnot(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    return newInstance(annotationMirror, ReturningAnnot::new);
  }

  private ReturningAnnot newReturningAnnot(
      AnnotationMirror ownerAnnotationMirror, String returningElementName) {
    assertNotNull(ownerAnnotationMirror, returningElementName);
    Map<String, AnnotationValue> valuesWithoutDefaults =
        ctx.getMoreElements().getValuesWithoutDefaults(ownerAnnotationMirror);
    AnnotationValue returning = valuesWithoutDefaults.get(returningElementName);
    if (returning == null) {
      return null;
    }
    AnnotationMirror returningMirror = AnnotationValueUtil.toAnnotation(returning);
    if (returningMirror == null) {
      return null;
    }
    return newReturningAnnot(returningMirror);
  }

  public ScriptAnnot newScriptAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return newInstance(annotation, ScriptAnnot::new);
  }

  public SelectAnnot newSelectAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return newInstance(annotation, SelectAnnot::new);
  }

  public SequenceGeneratorAnnot newSequenceGeneratorAnnot(VariableElement field) {
    assertNotNull(field);
    return newInstance(field, SequenceGenerator.class, SequenceGeneratorAnnot::new);
  }

  public SqlAnnot newSqlAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Sql.class, SqlAnnot::new);
  }

  public SqlProcessorAnnot newSqlProcessorAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return new SqlProcessorAnnot(annotation);
  }

  public SQLXMLFactoryAnnot newSQLXMLFactoryAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    return new SQLXMLFactoryAnnot(annotation);
  }

  public TableGeneratorAnnot newTableGeneratorAnnot(VariableElement field) {
    assertNotNull(field);
    return newInstance(field, TableGenerator.class, TableGeneratorAnnot::new);
  }

  public TableAnnot newTableAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(typeElement, Table.class, TableAnnot::new);
  }

  public UpdateAnnot newUpdateAnnot(AnnotationMirror annotation) {
    assertNotNull(annotation);
    ReturningAnnot returningAnnot = newReturningAnnot(annotation, ModifyAnnot.RETURNING);
    Map<String, AnnotationValue> valuesWithDefaults =
        ctx.getMoreElements().getValuesWithDefaults(annotation);
    return new UpdateAnnot(annotation, returningAnnot, valuesWithDefaults);
  }

  public ValueAnnot newValueAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(typeElement, ctx.getOptions().getLombokValue(), ValueAnnot::new);
  }

  private <ANNOT> ANNOT newInstance(
      Element element,
      Class<? extends java.lang.annotation.Annotation> annotationClass,
      BiFunction<AnnotationMirror, Map<String, AnnotationValue>, ANNOT> biFunction) {
    AnnotationMirror annotationMirror =
        ctx.getMoreElements().getAnnotationMirror(element, annotationClass);
    if (annotationMirror == null) {
      return null;
    }
    return newInstance(annotationMirror, biFunction);
  }

  private <ANNOT> ANNOT newInstance(
      Element element,
      String annotationClassName,
      BiFunction<AnnotationMirror, Map<String, AnnotationValue>, ANNOT> biFunction) {
    AnnotationMirror annotationMirror =
        ctx.getMoreElements().getAnnotationMirror(element, annotationClassName);
    if (annotationMirror == null) {
      return null;
    }
    return newInstance(annotationMirror, biFunction);
  }

  private <ANNOT> ANNOT newInstance(
      AnnotationMirror annotationMirror,
      BiFunction<AnnotationMirror, Map<String, AnnotationValue>, ANNOT> biFunction) {
    assertNotNull(annotationMirror);
    Map<String, AnnotationValue> values =
        ctx.getMoreElements().getValuesWithDefaults(annotationMirror);
    return biFunction.apply(annotationMirror, values);
  }
}
