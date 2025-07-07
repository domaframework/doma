/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.processor.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.seasar.doma.internal.apt.AbstractCompilerTest;
import org.seasar.doma.internal.apt.DomaProcessor;
import org.seasar.doma.internal.apt.GeneratedClassNameParameterResolver;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;
import org.seasar.doma.internal.apt.lombok.Value;
import org.seasar.doma.message.Message;

class EntityProcessorTest extends AbstractCompilerTest {

  @BeforeEach
  void beforeEach() {
    addOption(
        "-Adoma.domain.converters=org.seasar.doma.internal.apt.processor.entity.DomainConvertersProvider");
    addProcessor(new DomaProcessor());

    // Add the dependent domains
    addCompilationUnit(Identifier.class);
    addCompilationUnit(Name.class);
    addCompilationUnit(Names.class);
    addCompilationUnit(Ver.class);
    addCompilationUnit(Weight.class);

    // Add the dependent external domains
    addCompilationUnit(BranchConverter.class);
    addCompilationUnit(PrimaryKeyConverter.class);
    addCompilationUnit(VersionNoConverter.class);
    addCompilationUnit(StringArrayConverter.class);

    // Add the dependent embeddables
    addCompilationUnit(UserAddress.class);
    addCompilationUnit(CustomerAddress.class);
    addCompilationUnit(OrderAddress.class);
  }

  @TestTemplate
  @ExtendWith(SuccessInvocationContextProvider.class)
  void success(
      Class<?>[] classes, URL expectedResourceUrl, String generatedClassName, String[] options)
      throws Exception {
    addOption(options);
    addCompilationUnit(classes);
    compile();
    assertEqualsGeneratedSourceWithResource(expectedResourceUrl, generatedClassName);
    assertTrue(getCompiledResult());
  }

  static class SuccessInvocationContextProvider implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
      return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        ExtensionContext context) {
      return Stream.of(
          invocationContext(Emp.class),
          invocationContext(Dept.class),
          invocationContext(User.class),
          invocationContext(Room.class),
          invocationContext(ImmutableUser.class),
          invocationContext(CommonChild.class),
          invocationContext(PackagePrivateEntity.class),
          invocationContext(BytesPropertyEntity.class),
          invocationContext(OptionalEntity.class),
          invocationContext(OptionalIntEntity.class),
          invocationContext(OptionalLongEntity.class),
          invocationContext(OptionalDoubleEntity.class),
          invocationContext(QuoteEntity.class),
          invocationContext(GenericListener1Entity.class),
          invocationContext(GenericListener3Entity.class),
          invocationContext(GenericListener6Entity.class),
          invocationContext(NamingType1Entity.class),
          invocationContext(NamingType2Entity.class),
          invocationContext(NamingType3Entity.class),
          invocationContext(ImmutableEntity.class),
          invocationContext(new Class[] {ImmutableChildEntity.class, ImmutableParentEntity.class}),
          invocationContext(TenantIdEntity.class),
          invocationContext(ParameterizedPropertyEntity.class),
          invocationContext(AbstractEntity.class),
          invocationContext(EnumPropertyEntity.class),
          invocationContext(DomainPropertyEntity.class),
          invocationContext(TransientPropertyEntity.class),
          invocationContext(
              new Class[] {OriginalStatesChildEntity.class, OriginalStatesParentEntity.class}),
          invocationContext(new Class[] {Child2NoInheritingEntity.class, Parent2Entity.class}),
          invocationContext(new Class[] {Child2InheritingEntity.class, Parent2Entity.class}),
          invocationContext(ChildEntity.class),
          invocationContext(PrimitivePropertyEntity.class),
          invocationContext(NotTopLevelEntity.class, NotTopLevelEntity.Hoge.class),
          invocationContext(
              NotTopLevelImmutableEntity.class, NotTopLevelImmutableEntity.Hoge.class),
          invocationContext(PrivateOriginalStatesEntity.class),
          invocationContext(PrivatePropertyEntity.class),
          invocationContext(LombokValue.class, "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(
              LombokAllArgsConstructor.class,
              "-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName()),
          invocationContext(Customer.class),
          invocationContext(Order.class));
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?> compilationUnit, String... options) {
      return invocationContext(new Class[] {compilationUnit}, options);
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?>[] compilationUnitArray, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return compilationUnitArray[0].getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(compilationUnitArray),
              new ResourceParameterResolver(compilationUnitArray[0]),
              new GeneratedClassNameParameterResolver(compilationUnitArray[0]),
              new SimpleParameterResolver(options));
        }
      };
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?> compilationUnit, Class<?> annotatedClass, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return compilationUnit.getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(new Class[] {compilationUnit}),
              new ResourceParameterResolver(compilationUnit),
              new GeneratedClassNameParameterResolver(annotatedClass),
              new SimpleParameterResolver(options));
        }
      };
    }
  }

  @TestTemplate
  @ExtendWith(ErrorInvocationContextProvider.class)
  void error(Class<?> clazz, Message message, String... options) throws Exception {
    addOption(options);
    addCompilationUnit(clazz);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(message);
  }

  static class ErrorInvocationContextProvider implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
      return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        ExtensionContext context) {
      return Stream.of(
          invocationContext(RawtypeOptionalEntity.class, Message.DOMA4232),
          invocationContext(WildcardOptionalEntity.class, Message.DOMA4233),
          invocationContext(IllegalMutableChildEntity.class, Message.DOMA4226),
          invocationContext(GenericListener8Entity.class, Message.DOMA4228),
          invocationContext(GenericListener5Entity.class, Message.DOMA4166),
          invocationContext(GenericListener4Entity.class, Message.DOMA4229),
          invocationContext(GenericListener2Entity.class, Message.DOMA4227),
          invocationContext(FinalMissingImmutableEntity.class, Message.DOMA4225),
          invocationContext(IllegalOriginalStatesImmutableEntity.class, Message.DOMA4224),
          invocationContext(WildcardPropertyEntity.class, Message.DOMA4205),
          invocationContext(RawtypePropertyEntity.class, Message.DOMA4204),
          invocationContext(IllegalVersionPropertyEntity.class, Message.DOMA4093),
          invocationContext(IllegalIdPropertyEntity.class, Message.DOMA4095),
          invocationContext(TableGeneratorWithoutGeneratedValueEntity.class, Message.DOMA4031),
          invocationContext(SequenceGeneratorWithoutGeneratedValueEntity.class, Message.DOMA4030),
          invocationContext(GeneratedValueWithCompositeIdEntity.class, Message.DOMA4036),
          invocationContext(GeneratedValueWithoutIdEntity.class, Message.DOMA4033),
          invocationContext(GeneratedValueNotNumberEntity.class, Message.DOMA4095),
          invocationContext(NoDefaultConstructorTableIdGeneratorEntity.class, Message.DOMA4169),
          invocationContext(AbstractTableIdGeneratorEntity.class, Message.DOMA4168),
          invocationContext(NoDefaultConstructorSequenceIdGeneratorEntity.class, Message.DOMA4171),
          invocationContext(NoDefaultConstructorEntityListenerEntity.class, Message.DOMA4167),
          invocationContext(AbstractSequenceIdGeneratorEntity.class, Message.DOMA4170),
          invocationContext(AbstractEntityListenerEntity.class, Message.DOMA4166),
          invocationContext(AnnotationConflictedEntity.class, Message.DOMA4086),
          invocationContext(ListenerArgumentTypeIllegalEntity.class, Message.DOMA4038),
          invocationContext(VersionNotNumberEntity.class, Message.DOMA4093),
          invocationContext(PropertyNameReservedEntity.class, Message.DOMA4025),
          invocationContext(Child3InheritingEntity.class, Message.DOMA4230),
          invocationContext(Outer_nonStaticInner.class, Message.DOMA4315),
          invocationContext(Outer_nonPublicInner.class, Message.DOMA4315),
          invocationContext(Outer_nonPublicMiddle.class, Message.DOMA4315),
          invocationContext(Outer__illegalName.class, Message.DOMA4317),
          invocationContext(UnsupportedPropertyEntity.class, Message.DOMA4096),
          invocationContext(VersionDuplicatedEntity.class, Message.DOMA4024),
          invocationContext(
              LombokValueNotImmutable.class,
              Message.DOMA4418,
              "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(
              LombokValueStaticConstructor.class,
              Message.DOMA4419,
              "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(
              LombokAllArgsConstructorNotImmutable.class,
              Message.DOMA4420,
              "-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName()),
          invocationContext(
              LombokAllArgsConstructorStaticName.class,
              Message.DOMA4421,
              "-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName()),
          invocationContext(
              LombokAllArgsConstructorAccess_private.class,
              Message.DOMA4422,
              "-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName()),
          invocationContext(
              LombokAllArgsConstructorAccess_none.class,
              Message.DOMA4426,
              "-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName()),
          invocationContext(WildcardOptionalEntity.class, Message.DOMA4233),
          invocationContext(IllegalEmbeddedPropertyEntity.class, Message.DOMA4498),
          invocationContext(IllegalColumnOverrideName.class, Message.DOMA4499));
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?> clazz, Message message, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return clazz.getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(clazz),
              new SimpleParameterResolver(message),
              new SimpleParameterResolver(options));
        }
      };
    }
  }
}
