package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.box;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddablePropertyMeta;
import org.seasar.doma.jdbc.entity.DefaultPropertyDesc;
import org.seasar.doma.jdbc.entity.EmbeddableDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;

public class EmbeddableDescGenerator extends AbstractGenerator {

    private final EmbeddableMeta embeddableMeta;

    public EmbeddableDescGenerator(Context ctx, EmbeddableMeta embeddableMeta, CodeSpec codeSpec, Formatter formatter) {
        super(ctx, codeSpec, formatter);
        assertNotNull(embeddableMeta);
        this.embeddableMeta = embeddableMeta;
    }

    @Override
    public void generate() {
        printPackage();
        printClass();
    }

    private void printClass() {
        iprint("/** */%n");
        printGenerated();
        iprint("public final class %1$s implements %2$s<%3$s> {%n",
                /* 1 */codeSpec.getSimpleName(),
                /* 2 */EmbeddableDesc.class.getName(),
                /* 3 */embeddableMeta.getEmbeddableElement().getQualifiedName());
        print("%n");
        indent();
        printValidateVersionStaticInitializer();
        printFields();
        printMethods();
        unindent();
        iprint("}%n");
    }

    private void printFields() {
        printSingletonField();
    }

    private void printSingletonField() {
        iprint("private static final %1$s __singleton = new %1$s();%n", codeSpec.getSimpleName());
        print("%n");
    }

    private void printMethods() {
        printGetEmbeddablePropertyDescsMethod();
        printNewEmbeddableMethod();
        printGetSingletonInternalMethod();
    }

    private void printGetEmbeddablePropertyDescsMethod() {
        iprint("@Override%n");
        iprint("public <ENTITY> %1$s<%2$s<ENTITY, ?>> getEmbeddablePropertyDescs"
                        + "(String embeddedPropertyName, Class<ENTITY> entityClass, %3$s namingType) {%n",
                /* 1 */List.class.getName(),
                /* 2 */EntityPropertyDesc.class.getName(),
                /* 3 */NamingType.class.getName());
        iprint("    return %1$s.asList(%n", Arrays.class.getName());
        for (var it = embeddableMeta.getEmbeddablePropertyMetas().iterator(); it.hasNext(); ) {
            var pm = it.next();
            iprint("        new %1$s<ENTITY, %2$s, %3$s>(entityClass, %4$s, "
                            + "embeddedPropertyName + \".%5$s\", \"%6$s\", namingType, %7$s, %8$s, %9$s)",
                    /* 1 */DefaultPropertyDesc.class.getName(),
                    /* 2 */pm.getCtType().accept(new BasicTypeArgCodeBuilder(), null),
                    /* 3 */pm.getCtType().accept(new ContainerTypeArgCodeBuilder(), false),
                    /* 4 */pm.getCtType().accept(new ScalarSupplierCodeBuilder(), false),
                    /* 5 */pm.getName(),
                    /* 6 */pm.getColumnName(),
                    /* 7 */pm.isColumnInsertable(),
                    /* 8 */pm.isColumnUpdatable(),
                    /* 9 */pm.isColumnQuoteRequired());
            print(it.hasNext() ? ",%n" : "");
        }
        print(");%n");
        iprint("}%n");
        print("%n");
    }

    private void printNewEmbeddableMethod() {
        iprint("@Override%n");
        iprint("public <ENTITY> %1$s newEmbeddable(String embeddedPropertyName, %2$s<String, %3$s<ENTITY, ?>> __args) {%n",
                /* 1 */embeddableMeta.getEmbeddableElement().getQualifiedName(),
                /* 2 */Map.class.getName(),
                /* 3 */Property.class.getName());
        if (embeddableMeta.isAbstract()) {
            iprint("    return null;%n");
        } else {
            iprint("    return new %1$s(%n",
                    embeddableMeta.getEmbeddableElement().getQualifiedName());
            for (var it = embeddableMeta.getEmbeddablePropertyMetas().iterator(); it.hasNext(); ) {
                EmbeddablePropertyMeta propertyMeta = it.next();
                iprint("        (%1$s)(__args.get(embeddedPropertyName + \".%2$s\") != null "
                                + "? __args.get(embeddedPropertyName + \".%2$s\").get() : null)",
                        /* 1 */box(propertyMeta.getTypeName()),
                        /* 2 */propertyMeta.getName());
                if (it.hasNext()) {
                    print(",%n");
                }
            }
            print(");%n");
        }
        iprint("}%n");
        print("%n");
    }

    private void printGetSingletonInternalMethod() {
        iprint("/**%n");
        iprint(" * @return the singleton%n");
        iprint(" */%n");
        iprint("public static %1$s getSingletonInternal() {%n", codeSpec.getSimpleName());
        iprint("    return __singleton;%n");
        iprint("}%n");
        print("%n");
    }

}
