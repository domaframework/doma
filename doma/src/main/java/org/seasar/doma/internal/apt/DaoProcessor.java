package org.seasar.doma.internal.apt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.internal.apt.meta.AutoBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoFunctionQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoProcedureQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.DaoMeta;
import org.seasar.doma.internal.apt.meta.DaoMetaFactory;
import org.seasar.doma.internal.apt.meta.QueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileSelectQueryMetaFactory;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes( { "org.seasar.doma.Dao",
        "org.seasar.doma.GenericDao" })
@SupportedOptions( { Options.TEST, Options.DEBUG, Options.SUFFIX })
public class DaoProcessor extends DomaAbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        for (TypeElement a : annotations) {
            DaoMetaFactory daoMetaFactory = createDaoMetaFactory();
            for (TypeElement daoElement : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(a))) {
                try {
                    DaoMeta daoMeta = daoMetaFactory.createDaoMeta(daoElement);
                    if (daoMeta.isGenericDao()) {
                        continue;
                    }
                    generateDao(daoElement, daoMeta);
                } catch (AptException e) {
                    Notifier.notify(processingEnv, e);
                } catch (AptIllegalStateException e) {
                    Notifier
                            .notify(processingEnv, Kind.ERROR, MessageCode.DOMA4039, daoElement);
                    throw e;
                } catch (RuntimeException e) {
                    Notifier
                            .notify(processingEnv, Kind.ERROR, MessageCode.DOMA4016, daoElement);
                    throw e;
                }
            }
        }
        return true;
    }

    protected void generateDao(TypeElement daoElement, DaoMeta daoMeta) {
        String qualifiedName = daoElement.getQualifiedName()
                + Options.getSuffix(processingEnv);
        Generator generator = createDaoGenerator(qualifiedName, daoMeta);
        generate(generator, qualifiedName, daoElement);
    }

    protected DaoMetaFactory createDaoMetaFactory() {
        List<QueryMetaFactory> commandMetaFactories = new ArrayList<QueryMetaFactory>();
        commandMetaFactories.add(new AutoModifyQueryMetaFactory(processingEnv));
        commandMetaFactories.add(new AutoBatchModifyQueryMetaFactory(
                processingEnv));
        commandMetaFactories
                .add(new AutoFunctionQueryMetaFactory(processingEnv));
        commandMetaFactories.add(new AutoProcedureQueryMetaFactory(
                processingEnv));
        commandMetaFactories.add(new SqlFileSelectQueryMetaFactory(
                processingEnv));
        commandMetaFactories.add(new SqlFileModifyQueryMetaFactory(
                processingEnv));
        commandMetaFactories.add(new SqlFileBatchModifyQueryMetaFactory(
                processingEnv));
        return new DaoMetaFactory(processingEnv, commandMetaFactories);
    }

    protected DaoGenerator createDaoGenerator(String qualifiedName,
            DaoMeta daoMeta) {
        return new DaoGenerator(processingEnv, qualifiedName, daoMeta);
    }
}
