package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.generator.ClassName;
import org.seasar.doma.internal.apt.generator.DaoImplGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
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
    return new DaoMetaFactory(ctx, queryMetaFactories);
  }

  protected List<QueryMetaFactory> createQueryMetaFactory() {
    List<QueryMetaFactory> factories = new ArrayList<QueryMetaFactory>();
    factories.add(new SqlFileSelectQueryMetaFactory(ctx));
    factories.add(new AutoModifyQueryMetaFactory(ctx));
    factories.add(new AutoBatchModifyQueryMetaFactory(ctx));
    factories.add(new AutoFunctionQueryMetaFactory(ctx));
    factories.add(new AutoProcedureQueryMetaFactory(ctx));
    factories.add(new SqlFileModifyQueryMetaFactory(ctx));
    factories.add(new SqlFileBatchModifyQueryMetaFactory(ctx));
    factories.add(new SqlFileScriptQueryMetaFactory(ctx));
    factories.add(new DefaultQueryMetaFactory(ctx));
    factories.add(new ArrayCreateQueryMetaFactory(ctx));
    factories.add(new BlobCreateQueryMetaFactory(ctx));
    factories.add(new ClobCreateQueryMetaFactory(ctx));
    factories.add(new NClobCreateQueryMetaFactory(ctx));
    factories.add(new SQLXMLCreateQueryMetaFactory(ctx));
    factories.add(new SqlProcessorQueryMetaFactory(ctx));
    return factories;
  }

  @Override
  protected ClassName createNameSpec(TypeElement typeElement, DaoMeta meta) {
    assertNotNull(typeElement, meta);
    return ctx.getClassNames().newDaoImplClassName(typeElement);
  }

  @Override
  protected Generator createGenerator(ClassName className, Printer printer, DaoMeta meta) {
    assertNotNull(className, meta, printer);
    return new DaoImplGenerator(ctx, className, printer, meta);
  }
}
