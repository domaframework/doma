package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.meta.ArrayCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoFunctionQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoProcedureQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.BlobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.ClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.DaoMeta;
import org.seasar.doma.internal.apt.meta.DaoMetaFactory;
import org.seasar.doma.internal.apt.meta.DefaultQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.NClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.QueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SQLXMLCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileScriptQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileSelectQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlProcessorQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;

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
