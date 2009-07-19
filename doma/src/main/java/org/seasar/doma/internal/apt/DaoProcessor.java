/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.internal.apt.meta.ArrayCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoFunctionQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoProcedureQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.BlobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.ClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.DaoMeta;
import org.seasar.doma.internal.apt.meta.DaoMetaFactory;
import org.seasar.doma.internal.apt.meta.NClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.QueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileSelectQueryMetaFactory;
import org.seasar.doma.internal.util.IOs;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes( { "org.seasar.doma.Dao" })
@SupportedOptions( { Options.TEST, Options.DEBUG, Options.SUFFIX })
public class DaoProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        for (TypeElement a : annotations) {
            List<QueryMetaFactory> queryMetaFactories = createQueryMetaFactory();
            DaoMetaFactory daoMetaFactory = createDaoMetaFactory(queryMetaFactories);
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

    protected List<QueryMetaFactory> createQueryMetaFactory() {
        List<QueryMetaFactory> factories = new ArrayList<QueryMetaFactory>();
        factories.add(new AutoModifyQueryMetaFactory(processingEnv));
        factories.add(new AutoBatchModifyQueryMetaFactory(processingEnv));
        factories.add(new AutoFunctionQueryMetaFactory(processingEnv));
        factories.add(new AutoProcedureQueryMetaFactory(processingEnv));
        factories.add(new ArrayCreateQueryMetaFactory(processingEnv));
        factories.add(new BlobCreateQueryMetaFactory(processingEnv));
        factories.add(new ClobCreateQueryMetaFactory(processingEnv));
        factories.add(new NClobCreateQueryMetaFactory(processingEnv));
        factories.add(new SqlFileSelectQueryMetaFactory(processingEnv));
        factories.add(new SqlFileModifyQueryMetaFactory(processingEnv));
        factories.add(new SqlFileBatchModifyQueryMetaFactory(processingEnv));
        return factories;
    }

    protected DaoMetaFactory createDaoMetaFactory(
            List<QueryMetaFactory> queryMetaFactories) {
        return new DaoMetaFactory(processingEnv, queryMetaFactories);
    }

    protected void generateDao(TypeElement daoElement, DaoMeta daoMeta) {
        DaoGenerator daoGenerator = null;
        try {
            daoGenerator = createDaoGenerator(daoElement, daoMeta);
            daoGenerator.generate();
        } catch (IOException e) {
            throw new AptException(MessageCode.DOMA4011, processingEnv,
                    daoElement, e, daoElement.getQualifiedName(), e);
        } finally {
            IOs.close(daoGenerator);
        }
    }

    protected DaoGenerator createDaoGenerator(TypeElement daoElement,
            DaoMeta daoMeta) throws IOException {
        return new DaoGenerator(processingEnv, daoElement, daoMeta);
    }
}
