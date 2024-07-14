package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
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
import org.seasar.doma.DataType;
import org.seasar.doma.Delete;
import org.seasar.doma.Domain;
import org.seasar.doma.DomainConverters;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.NClobFactory;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.SQLXMLFactory;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Sql;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.Table;
import org.seasar.doma.TableGenerator;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class Annotations {

  private final Context ctx;

  public Annotations(Context ctx) {
    this.ctx = ctx;
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

  public ArrayFactoryAnnot newArrayFactoryAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, ArrayFactory.class, ArrayFactoryAnnot::new);
  }

  public BatchDeleteAnnot newBatchDeleteAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, BatchDelete.class, BatchDeleteAnnot::new);
  }

  public BatchInsertAnnot newBatchInsertAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, BatchInsert.class, BatchInsertAnnot::new);
  }

  public BatchUpdateAnnot newBatchUpdateAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, BatchUpdate.class, BatchUpdateAnnot::new);
  }

  public BlobFactoryAnnot newBlobFactoryAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, BlobFactory.class, BlobFactoryAnnot::new);
  }

  public ClobFactoryAnnot newClobFactoryAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, ClobFactory.class, ClobFactoryAnnot::new);
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

  public DeleteAnnot newDeleteAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Delete.class, DeleteAnnot::new);
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

  public FunctionAnnot newFunctionAnnot(final ExecutableElement method) {
    assertNotNull(method);
    return newInstance(
        method,
        org.seasar.doma.Function.class,
        (annotationMirror, values) ->
            new FunctionAnnot(annotationMirror, values, method.getSimpleName().toString()));
  }

  public InsertAnnot newInsertAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Insert.class, InsertAnnot::new);
  }

  public MultiInsertAnnot newMultiInsertAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, MultiInsert.class, MultiInsertAnnot::new);
  }

  public MetamodelAnnot newMetamodelAnnot(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    return newInstance(annotationMirror, MetamodelAnnot::new);
  }

  public NClobFactoryAnnot newNClobFactoryAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, NClobFactory.class, NClobFactoryAnnot::new);
  }

  public ProcedureAnnot newProcedureAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(
        method,
        Procedure.class,
        (annotationMirror, values) ->
            new ProcedureAnnot(annotationMirror, values, method.getSimpleName().toString()));
  }

  public ResultSetAnnot newResultSetAnnot(VariableElement param) {
    assertNotNull(param);
    return newInstance(param, ResultSet.class, ResultSetAnnot::new);
  }

  public ScriptAnnot newScriptAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Script.class, ScriptAnnot::new);
  }

  public SelectAnnot newSelectAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Select.class, SelectAnnot::new);
  }

  public SequenceGeneratorAnnot newSequenceGeneratorAnnot(VariableElement field) {
    assertNotNull(field);
    return newInstance(field, SequenceGenerator.class, SequenceGeneratorAnnot::new);
  }

  @SuppressWarnings("deprecation")
  public SingletonConfigAnnot newSingletonConfigAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(
        typeElement, org.seasar.doma.SingletonConfig.class, SingletonConfigAnnot::new);
  }

  public SqlAnnot newSqlAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Sql.class, SqlAnnot::new);
  }

  public SqlProcessorAnnot newSqlProcessorAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, SqlProcessor.class, SqlProcessorAnnot::new);
  }

  public SQLXMLFactoryAnnot newSQLXMLFactoryAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, SQLXMLFactory.class, SQLXMLFactoryAnnot::new);
  }

  public TableGeneratorAnnot newTableGeneratorAnnot(VariableElement field) {
    assertNotNull(field);
    return newInstance(field, TableGenerator.class, TableGeneratorAnnot::new);
  }

  public TableAnnot newTableAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(typeElement, Table.class, TableAnnot::new);
  }

  public UpdateAnnot newUpdateAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Update.class, UpdateAnnot::new);
  }

  public ValueAnnot newValueAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newInstance(typeElement, ctx.getOptions().getLombokValue(), ValueAnnot::new);
  }

  private <ANNOT> ANNOT newInstance(
      Element element,
      Class<? extends java.lang.annotation.Annotation> annotationClass,
      Function<AnnotationMirror, ANNOT> function) {
    return newInstance(
        element, annotationClass, (annotationMirror, __) -> function.apply(annotationMirror));
  }

  private <ANNOT> ANNOT newInstance(
      Element element,
      Class<? extends java.lang.annotation.Annotation> annotationClass,
      BiFunction<AnnotationMirror, Map<String, AnnotationValue>, ANNOT> biFunction) {
    AnnotationMirror annotationMirror =
        ctx.getMoreElements().getAnnotationMirror(element, annotationClass);
    return newInstance(annotationMirror, biFunction);
  }

  private <ANNOT> ANNOT newInstance(
      Element element,
      String annotationClassName,
      BiFunction<AnnotationMirror, Map<String, AnnotationValue>, ANNOT> biFunction) {
    AnnotationMirror annotationMirror =
        ctx.getMoreElements().getAnnotationMirror(element, annotationClassName);
    return newInstance(annotationMirror, biFunction);
  }

  private <ANNOT> ANNOT newInstance(
      AnnotationMirror annotationMirror,
      BiFunction<AnnotationMirror, Map<String, AnnotationValue>, ANNOT> biFunction) {
    if (annotationMirror == null) {
      return null;
    }
    Map<String, AnnotationValue> values =
        ctx.getMoreElements().getValuesWithDefaults(annotationMirror);
    return biFunction.apply(annotationMirror, values);
  }
}
