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
package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.util.Formatter;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.generator.CodeSpec;
import org.seasar.doma.internal.apt.generator.CodeSpecFactory;
import org.seasar.doma.internal.apt.generator.DaoImplCodeSpecFactory;
import org.seasar.doma.internal.apt.generator.DaoImplGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.dao.DaoMetaFactory;
import org.seasar.doma.internal.apt.meta.dao.ParentDaoMeta;

/**
 * @author taedium
 * 
 */
@SupportedAnnotationTypes({ "org.seasar.doma.Dao" })
@SupportedOptions({ Options.TEST, Options.DEBUG, Options.DAO_PACKAGE, Options.DAO_SUBPACKAGE,
        Options.DAO_SUFFIX, Options.EXPR_FUNCTIONS, Options.SQL_VALIDATION,
        Options.VERSION_VALIDATION, Options.RESOURCES_DIR })
public class DaoProcessor extends AbstractGeneratingProcessor<DaoMeta> {

    public DaoProcessor() {
        super(Dao.class);
    }

    @Override
    protected TypeElementMetaFactory<DaoMeta> createTypeElementMetaFactory(
            TypeElement typeElement) {
        return new DaoMetaFactory(ctx, typeElement);
    }

    @Override
    protected CodeSpecFactory createCodeSpecFactory(DaoMeta meta) {
        ParentDaoMeta parentDaoMeta = meta.getParentDaoMeta();
        TypeElement parentDaoElement = null;
        if (parentDaoMeta != null) {
            parentDaoElement = parentDaoMeta.getDaoElement();
        }
        return new DaoImplCodeSpecFactory(ctx, meta.getDaoElement(), parentDaoElement);
    }

    @Override
    protected Generator createGenerator(DaoMeta meta, CodeSpec codeSpec, Formatter formatter)
            throws IOException {
        assertNotNull(meta, codeSpec, formatter);
        return new DaoImplGenerator(ctx, meta, codeSpec, formatter);
    }
}
