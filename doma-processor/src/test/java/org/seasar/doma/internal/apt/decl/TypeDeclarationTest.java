package org.seasar.doma.internal.apt.decl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.TestProcessor;

class TypeDeclarationTest extends CompilerSupport {

  @SuppressWarnings("InnerClassMayBeStatic")
  private class Inner {}

  @SuppressWarnings("unused")
  private String myField;

  @SuppressWarnings("unused")
  private static String myStaticField;

  @SuppressWarnings("unused")
  private Function<String, Integer> myFunctionField;

  @SuppressWarnings("unused")
  public String myMethod(Integer integer) {
    return null;
  }

  @SuppressWarnings("unused")
  public String varArgs(String... args) {
    return null;
  }

  @SuppressWarnings("unused")
  public static String myStaticMethod(Integer integer) {
    return null;
  }

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
  void getType() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(String.class);
            boolean isSameType =
                ctx.getMoreTypes().isSameTypeWithErasure(typeDeclaration.getType(), String.class);
            assertTrue(isSameType);
          }
        });
  }

  @Test
  void getBinaryName() {
    Class<?> testClass = getClass();
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(Inner.class);
            assertEquals(
                testClass.getName() + "$" + Inner.class.getSimpleName(),
                typeDeclaration.getBinaryName());
          }
        });
  }

  @Test
  void isUnknownType() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(String.class);
            assertFalse(typeDeclaration.isUnknownType());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newUnknownTypeDeclaration();
            assertTrue(typeDeclaration.isUnknownType());
          }
        });
  }

  @Test
  void isNullType() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror nullType = ctx.getMoreTypes().getNullType();
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(nullType);
            assertTrue(typeDeclaration.isNullType());
          }
        });
  }

  @Test
  void isBooleanType() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(Boolean.class);
            assertTrue(typeDeclaration.isBooleanType());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(boolean.class);
            assertTrue(typeDeclaration.isBooleanType());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(String.class);
            assertFalse(typeDeclaration.isBooleanType());
          }
        });
  }

  @Test
  void isTextType() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(int.class);
            assertFalse(typeDeclaration.isTextType());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(String.class);
            assertTrue(typeDeclaration.isTextType());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(char.class);
            assertTrue(typeDeclaration.isTextType());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(Character.class);
            assertTrue(typeDeclaration.isTextType());
          }
        });
  }

  @Test
  void isNumberType() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(int.class);
            assertTrue(typeDeclaration.isNumberType());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(Integer.class);
            assertTrue(typeDeclaration.isNumberType());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(BigDecimal.class);
            assertTrue(typeDeclaration.isNumberType());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(String.class);
            assertFalse(typeDeclaration.isNumberType());
          }
        });
  }

  @Test
  void is() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(String.class);
            assertTrue(typeDeclaration.is(String.class));
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(String.class);
            assertFalse(typeDeclaration.is(Integer.class));
          }
        });
  }

  @Test
  void getTypeParameterDeclarations() {
    Class<?> testClass = getClass();
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(String.class);
            List<TypeParameterDeclaration> typeParams =
                typeDeclaration.getTypeParameterDeclarations();
            assertTrue(typeParams.isEmpty());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(testClass);
            Optional<FieldDeclaration> field =
                typeDeclaration.getFieldDeclaration("myFunctionField");
            assertNotNull(field);
            assertTrue(field.isPresent());

            List<TypeParameterDeclaration> typeParams =
                field.get().getTypeDeclaration().getTypeParameterDeclarations();
            assertEquals(2, typeParams.size());

            TypeParameterDeclaration typeParam1 = typeParams.get(0);
            TypeVariable typeVariable1 =
                ctx.getMoreTypes().toTypeVariable(typeParam1.getFormalType());
            assertEquals("T", typeVariable1.asElement().getSimpleName().toString());
            assertTrue(
                ctx.getMoreTypes().isSameTypeWithErasure(typeParam1.getActualType(), String.class));

            TypeParameterDeclaration typeParam2 = typeParams.get(1);
            TypeVariable typeVariable2 =
                ctx.getMoreTypes().toTypeVariable(typeParam2.getFormalType());
            assertEquals("R", typeVariable2.asElement().getSimpleName().toString());
            assertTrue(
                ctx.getMoreTypes()
                    .isSameTypeWithErasure(typeParam2.getActualType(), Integer.class));
          }
        });
  }

  @Test
  void getConstructorDeclaration() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(String.class);
            Optional<ConstructorDeclaration> constructorDeclaration =
                typeDeclaration.getConstructorDeclaration(Collections.emptyList());
            assertTrue(constructorDeclaration.isPresent());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newTypeDeclaration(String.class);
            TypeDeclaration arg1 = ctx.getDeclarations().newTypeDeclaration(Object.class);
            Optional<ConstructorDeclaration> constructorDeclaration =
                typeDeclaration.getConstructorDeclaration(Collections.singletonList(arg1));
            assertFalse(constructorDeclaration.isPresent());
          }
        });
  }

  @Test
  void getFieldDeclaration() {
    Class<?> testClass = getClass();
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(testClass);
            Optional<FieldDeclaration> fieldDeclaration =
                typeDeclaration.getFieldDeclaration("myField");
            assertTrue(fieldDeclaration.isPresent());
            FieldDeclaration f = fieldDeclaration.get();
            assertEquals("myField", f.getElement().getSimpleName().toString());
            assertEquals("java.lang.String", f.getTypeDeclaration().getBinaryName());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(testClass);
            Optional<FieldDeclaration> fieldDeclaration =
                typeDeclaration.getFieldDeclaration("noSuchField");
            assertFalse(fieldDeclaration.isPresent());
          }
        });
  }

  @Test
  void getStaticFieldDeclaration() {
    Class<?> testClass = getClass();
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(testClass);
            Optional<FieldDeclaration> fieldDeclaration =
                typeDeclaration.getStaticFieldDeclaration("myStaticField");
            assertTrue(fieldDeclaration.isPresent());
            FieldDeclaration f = fieldDeclaration.get();
            assertEquals("myStaticField", f.getElement().getSimpleName().toString());
            assertEquals("java.lang.String", f.getTypeDeclaration().getBinaryName());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(testClass);
            Optional<FieldDeclaration> fieldDeclaration =
                typeDeclaration.getStaticFieldDeclaration("noSuchField");
            assertFalse(fieldDeclaration.isPresent());
          }
        });
  }

  @Test
  void getMethodDeclaration() {
    Class<?> testClass = getClass();
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(testClass);
            TypeDeclaration parameterDeclaration =
                ctx.getDeclarations().newTypeDeclaration(Integer.class);
            Optional<MethodDeclaration> methodDeclaration =
                typeDeclaration.getMethodDeclaration(
                    "myMethod", Collections.singletonList(parameterDeclaration));
            assertTrue(methodDeclaration.isPresent());
            MethodDeclaration m = methodDeclaration.get();
            assertEquals("myMethod", m.getElement().getSimpleName().toString());
            TypeDeclaration returnTypeDeclaration = m.getReturnTypeDeclaration();
            assertTrue(returnTypeDeclaration.is(String.class));
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(testClass);
            TypeDeclaration parameterDeclaration =
                ctx.getDeclarations().newTypeDeclaration(Integer.class);
            Optional<MethodDeclaration> methodDeclaration =
                typeDeclaration.getMethodDeclaration(
                    "noSuchMethod", Collections.singletonList(parameterDeclaration));
            assertFalse(methodDeclaration.isPresent());
          }
        });
  }

  @Test
  void getStaticMethodDeclarations() {
    Class<?> testClass = getClass();
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(testClass);
            TypeDeclaration parameterDeclaration =
                ctx.getDeclarations().newTypeDeclaration(Integer.class);
            Optional<MethodDeclaration> methodDeclaration =
                typeDeclaration.getStaticMethodDeclaration(
                    "myStaticMethod", Collections.singletonList(parameterDeclaration));
            assertTrue(methodDeclaration.isPresent());
            MethodDeclaration m = methodDeclaration.get();
            assertEquals("myStaticMethod", m.getElement().getSimpleName().toString());
            TypeDeclaration returnTypeDeclaration = m.getReturnTypeDeclaration();
            assertTrue(returnTypeDeclaration.is(String.class));
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(testClass);
            TypeDeclaration parameterDeclaration =
                ctx.getDeclarations().newTypeDeclaration(Integer.class);
            Optional<MethodDeclaration> methodDeclarations =
                typeDeclaration.getStaticMethodDeclaration(
                    "noSuchMethod", Collections.singletonList(parameterDeclaration));
            assertFalse(methodDeclarations.isPresent());
          }
        });
  }

  @Test
  void emulateConcatOperation() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration left = ctx.getDeclarations().newTypeDeclaration(String.class);
            TypeDeclaration right = ctx.getDeclarations().newTypeDeclaration(String.class);
            TypeDeclaration result = left.emulateConcatOperation(right);
            assertNotNull(result);
            assertTrue(result.isTextType());
          }
        });
  }

  @Test
  void emulateArithmeticOperation() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration left = ctx.getDeclarations().newTypeDeclaration(Integer.class);
            TypeDeclaration right = ctx.getDeclarations().newTypeDeclaration(BigDecimal.class);
            TypeDeclaration result = left.emulateArithmeticOperation(right);
            assertNotNull(result);
            assertTrue(result.is(BigDecimal.class));
          }
        });
  }

  @Test
  void isSameType() {}
}
