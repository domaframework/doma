package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.generator.DaoGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.dao.DaoMetaFactory;
import org.seasar.doma.internal.apt.meta.query.ArrayCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoFunctionQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoProcedureQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.BlobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.ClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.DefaultQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.NClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.QueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SQLXMLCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileScriptQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileSelectQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlProcessorQueryMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.Dao"})
@SupportedOptions({
  Options.TEST,
  Options.DEBUG,
  Options.DAO_PACKAGE,
  Options.DAO_SUBPACKAGE,
  Options.DAO_SUFFIX,
  Options.EXPR_FUNCTIONS,
  Options.SQL_VALIDATION,
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR
})
public class DaoProcessor extends AbstractGeneratingProcessor<DaoMeta> {

  public DaoProcessor() {
    super(Dao.class);
  }

  @Override
  protected TypeElementMetaFactory<DaoMeta> createTypeElementMetaFactory() {
    List<QueryMetaFactory> queryMetaFactories = createQueryMetaFactory();
    return new DaoMetaFactory(processingEnv, queryMetaFactories);
  }

  protected List<QueryMetaFactory> createQueryMetaFactory() {
    List<QueryMetaFactory> factories = new ArrayList<QueryMetaFactory>();
    factories.add(new SqlFileSelectQueryMetaFactory(processingEnv));
    factories.add(new AutoModifyQueryMetaFactory(processingEnv));
    factories.add(new AutoBatchModifyQueryMetaFactory(processingEnv));
    factories.add(new AutoFunctionQueryMetaFactory(processingEnv));
    factories.add(new AutoProcedureQueryMetaFactory(processingEnv));
    factories.add(new SqlFileModifyQueryMetaFactory(processingEnv));
    factories.add(new SqlFileBatchModifyQueryMetaFactory(processingEnv));
    factories.add(new SqlFileScriptQueryMetaFactory(processingEnv));
    factories.add(new DefaultQueryMetaFactory(processingEnv));
    factories.add(new ArrayCreateQueryMetaFactory(processingEnv));
    factories.add(new BlobCreateQueryMetaFactory(processingEnv));
    factories.add(new ClobCreateQueryMetaFactory(processingEnv));
    factories.add(new NClobCreateQueryMetaFactory(processingEnv));
    factories.add(new SQLXMLCreateQueryMetaFactory(processingEnv));
    factories.add(new SqlProcessorQueryMetaFactory(processingEnv));
    return factories;
  }

  @Override
  protected Generator createGenerator(TypeElement typeElement, DaoMeta meta) throws IOException {
    assertNotNull(typeElement, meta);
    return new DaoGenerator(processingEnv, typeElement, meta);
  }
}
