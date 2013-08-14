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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.meta.ArrayCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoFunctionQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.AutoProcedureQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.BlobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.ClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.DaoMeta;
import org.seasar.doma.internal.apt.meta.DaoMetaFactory;
import org.seasar.doma.internal.apt.meta.DelegateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.NClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.QueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileScriptQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.SqlFileSelectQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;

/**
 * @author taedium
 * 
 */
@SupportedAnnotationTypes({ "org.seasar.doma.Dao" })
@SupportedOptions({ Options.TEST, Options.DEBUG, Options.DAO_PACKAGE,
        Options.DAO_SUBPACKAGE, Options.DAO_SUFFIX, Options.EXPR_FUNCTIONS,
        Options.SQL_VALIDATION, Options.VERSION_VALIDATION })
public class DaoProcessor extends AbstractGeneratingProcessor<DaoMeta> {

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
        factories.add(new DelegateQueryMetaFactory(processingEnv));
        factories.add(new ArrayCreateQueryMetaFactory(processingEnv));
        factories.add(new BlobCreateQueryMetaFactory(processingEnv));
        factories.add(new ClobCreateQueryMetaFactory(processingEnv));
        factories.add(new NClobCreateQueryMetaFactory(processingEnv));
        return factories;
    }

    @Override
    protected Generator createGenerator(TypeElement typeElement, DaoMeta meta)
            throws IOException {
        assertNotNull(typeElement, meta);
        return new DaoGenerator(processingEnv, typeElement, meta);
    }
}
