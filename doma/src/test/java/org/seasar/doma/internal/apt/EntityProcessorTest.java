package org.seasar.doma.internal.apt;

import org.seasar.doma.internal.apt.EntityProcessor;
import org.seasar.doma.internal.apt.entity.ChildEntity;
import org.seasar.doma.internal.apt.entity.ElementOfReturnListNotDomainEntity;
import org.seasar.doma.internal.apt.entity.ElementOfReturnListUnspecifiedEntity;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.internal.apt.entity.ListenerArgumentTypeIllegalEntity;
import org.seasar.doma.internal.apt.entity.NameUnsafeEntity_;
import org.seasar.doma.internal.apt.entity.NotTopLevelEntity;
import org.seasar.doma.internal.apt.entity.ParamSizeNotZeroEntity;
import org.seasar.doma.internal.apt.entity.PropertyNameReservedEntity;
import org.seasar.doma.internal.apt.entity.ReturnListNotTransientEntity;
import org.seasar.doma.internal.apt.entity.ReturnListTransientEntity;
import org.seasar.doma.internal.apt.entity.ReturnTypeNotConcreteDomainEntity;
import org.seasar.doma.internal.apt.entity.ReturnTypeNotDomainEntity;
import org.seasar.doma.internal.apt.entity.VersionDuplicatedEntity;
import org.seasar.doma.internal.apt.entity.VersionNotNumberEntity;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class EntityProcessorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("src/test/java");
    }

    public void testEmp() throws Exception {
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(Emp.class.getName());
        compile();
        assertGeneratedSource(Emp.class);
        assertTrue(getCompiledResult());
    }

    public void testParamSizeNotZero() throws Exception {
        Class<?> target = ParamSizeNotZeroEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4023);
    }

    public void testVersionDuplicated() throws Exception {
        Class<?> target = VersionDuplicatedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4024);
    }

    public void testNotTopLevel() throws Exception {
        Class<?> target = NotTopLevelEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4018);
    }

    public void testReturnTypeNotDomain() throws Exception {
        Class<?> target = ReturnTypeNotDomainEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4022);
    }

    public void testReturnTypeNotConcreteDomain() throws Exception {
        Class<?> target = ReturnTypeNotConcreteDomainEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4022);
    }

    public void testExtends() throws Exception {
        Class<?> target = ChildEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertGeneratedSource(ChildEntity.class);
    }

    public void testPropertyNameReserved() throws Exception {
        Class<?> target = PropertyNameReservedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4025);
    }

    public void testNameUnsafe() throws Exception {
        Class<?> target = NameUnsafeEntity_.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4026);
    }

    public void testElementOfReturnListUnspecified() throws Exception {
        Class<?> target = ElementOfReturnListUnspecifiedEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4029);
    }

    public void testElementOfReturnListNotDomain() throws Exception {
        Class<?> target = ElementOfReturnListNotDomainEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4030);
    }

    public void testReturnListNotTransient() throws Exception {
        Class<?> target = ReturnListNotTransientEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4031);
    }

    public void testReturnListTransient() throws Exception {
        Class<?> target = ReturnListTransientEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertTrue(getCompiledResult());
    }

    public void testVersionNotNumber() throws Exception {
        Class<?> target = VersionNotNumberEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4032);
    }

    public void testListenerArgumentTypeIllegal() throws Exception {
        Class<?> target = ListenerArgumentTypeIllegalEntity.class;
        EntityProcessor processor = new EntityProcessor();
        addProcessor(processor);
        addCompilationUnit(target);
        compile();
        assertFalse(getCompiledResult());
        assertMessageCode(MessageCode.DOMA4038);
    }
}
