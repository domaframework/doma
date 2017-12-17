package org.seasar.doma.internal.apt;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class TypesTest extends AptTestCase {

    public void test() throws Exception {
        addCompilationUnit(getClass());
        addProcessor(new AptProcessor(ctx -> {
            Types types = ctx.getTypes();
            TypeMirror type = types.getType(Ccc.class);
            Collection<TypeMirror> collection = types.supertypes(type);
            assertEquals(4, collection.size());
            Set<String> set = collection.stream()
                    .map(types::toTypeElement)
                    .map(TypeElement::getQualifiedName)
                    .map(Name::toString)
                    .collect(Collectors.toSet());
            assertTrue(set.contains(Object.class.getCanonicalName()));
            assertTrue(set.contains(Serializable.class.getCanonicalName()));
            assertTrue(set.contains(Aaa.class.getCanonicalName()));
            assertTrue(set.contains(Bbb.class.getCanonicalName()));
        }));
        compile();
        assertTrue(getCompiledResult());

    }

    @SuppressWarnings("serial")
    public static class Aaa implements Serializable {
    }

    public static interface Bbb {
    }

    @SuppressWarnings("serial")
    public static class Ccc extends Aaa implements Bbb {
    }
}
