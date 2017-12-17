package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.util.Formatter;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
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
    protected CodeSpec createCodeSpec(DaoMeta meta) {
        ParentDaoMeta parentDaoMeta = meta.getParentDaoMeta();
        TypeElement parentDaoElement = null;
        if (parentDaoMeta != null) {
            parentDaoElement = parentDaoMeta.getDaoElement();
        }
        return ctx.getCodeSpecs().newDaoImplCodeSpec(meta.getDaoElement(), parentDaoElement);
    }

    @Override
    protected Generator createGenerator(DaoMeta meta, CodeSpec codeSpec, Formatter formatter)
            throws IOException {
        assertNotNull(meta, codeSpec, formatter);
        return new DaoImplGenerator(ctx, meta, codeSpec, formatter);
    }
}
