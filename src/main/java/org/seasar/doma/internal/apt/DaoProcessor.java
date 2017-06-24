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

/**
 * @author taedium
 * 
 */
@SupportedAnnotationTypes({ "org.seasar.doma.Dao" })
@SupportedOptions({ Options.TEST, Options.DEBUG, Options.DAO_PACKAGE,
        Options.DAO_SUBPACKAGE, Options.DAO_SUFFIX, Options.EXPR_FUNCTIONS,
        Options.SQL_VALIDATION, Options.VERSION_VALIDATION,
        Options.RESOURCES_DIR })
public class DaoProcessor extends AbstractGeneratingProcessor<DaoMeta> {

    public DaoProcessor() {
        super(Dao.class);
    }

    @Override
    protected TypeElementMetaFactory<DaoMeta> createTypeElementMetaFactory() {
        List<QueryMetaFactory> queryMetaFactories = createQueryMetaFactory(ctx);
        return new DaoMetaFactory(ctx, queryMetaFactories);
    }

    private List<QueryMetaFactory> createQueryMetaFactory(Context ctx) {
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
    protected Generator createGenerator(Context ctx, TypeElement typeElement,
            DaoMeta meta)
            throws IOException {
        assertNotNull(typeElement, meta);
        return new DaoGenerator(ctx, typeElement, meta);
    }
}
