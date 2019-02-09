package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.*;

import javax.lang.model.element.TypeElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.ClassName;

class TypeNamesTest extends CompilerSupport {

  private static class Inner {}

  @BeforeEach
  void beforeEach() {
    addCompilationUnit(getClass());
  }

  @AfterEach
  void afterEach() throws Exception {
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void newDaoImplClassName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            TypeNames typeNames = new TypeNames(ctx);
            TypeName typeName = typeNames.newDaoImplTypeName(typeElement);
            ClassName className = typeName.getClassName();
            assertEquals("java.lang.StringImpl", className.toString());
          }
        });
  }

  @Test
  void newDomainTypeClassName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            TypeNames typeNames = new TypeNames(ctx);
            TypeName typeName = typeNames.newDomainDescTypeName(typeElement);
            ClassName className = typeName.getClassName();
            assertEquals("java.lang._String", className.toString());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(Inner.class);
            TypeNames typeNames = new TypeNames(ctx);
            TypeName typeName = typeNames.newDomainDescTypeName(typeElement);
            ClassName className = typeName.getClassName();
            assertEquals(
                "org.seasar.doma.internal.apt._TypeNamesTest__Inner", className.toString());
          }
        });
  }

  @Test
  void newEmbeddableTypeClassName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            TypeNames typeNames = new TypeNames(ctx);
            TypeName typeName = typeNames.newEmbeddableDescTypeName(typeElement);
            ClassName className = typeName.getClassName();
            assertEquals("java.lang._String", className.toString());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(Inner.class);
            TypeNames typeNames = new TypeNames(ctx);
            TypeName typeName = typeNames.newEmbeddableDescTypeName(typeElement);
            ClassName className = typeName.getClassName();
            assertEquals(
                "org.seasar.doma.internal.apt._TypeNamesTest__Inner", className.toString());
          }
        });
  }

  @Test
  void newEntityTypeClassName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            TypeNames typeNames = new TypeNames(ctx);
            TypeName typeName = typeNames.newEntityDescTypeName(typeElement);
            ClassName className = typeName.getClassName();
            assertEquals("java.lang._String", className.toString());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(Inner.class);
            TypeNames typeNames = new TypeNames(ctx);
            TypeName typeName = typeNames.newEntityDescTypeName(typeElement);
            ClassName className = typeName.getClassName();
            assertEquals(
                "org.seasar.doma.internal.apt._TypeNamesTest__Inner", className.toString());
          }
        });
  }

  @Test
  void newExternalDomainTypeClassName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            TypeNames typeNames = new TypeNames(ctx);
            TypeName typeName = typeNames.newExternalDomainDescTypeName(typeElement);
            ClassName className = typeName.getClassName();
            assertEquals("__.java.lang._String", className.toString());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(Inner.class);
            TypeNames typeNames = new TypeNames(ctx);
            TypeName typeName = typeNames.newExternalDomainDescTypeName(typeElement);
            ClassName className = typeName.getClassName();
            assertEquals(
                "__.org.seasar.doma.internal.apt._TypeNamesTest__Inner", className.toString());
          }
        });
  }
}
