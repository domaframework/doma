package org.seasar.doma.internal.jdbc.scalar;

import example.holder.InternationalPhoneNumber;
import example.holder.PhoneNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import junit.framework.TestCase;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.wrapper.EnumWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

public class ScalarsTest extends TestCase {

  private final ClassHelper classHelper = new ClassHelper() {};

  public void testWrapBasic() throws Exception {
    assertNotNull(Scalars.wrap(true, boolean.class, false, classHelper));
    assertNotNull(Scalars.wrap(true, Boolean.class, false, classHelper));
    assertNotNull(Scalars.wrap((byte) 1, byte.class, false, classHelper));
    assertNotNull(Scalars.wrap(Byte.valueOf((byte) 1), Byte.class, false, classHelper));
    assertNotNull(Scalars.wrap((short) 1, short.class, false, classHelper));
    assertNotNull(Scalars.wrap(Short.valueOf((short) 1), Short.class, false, classHelper));
    assertNotNull(Scalars.wrap(1, int.class, false, classHelper));
    assertNotNull(Scalars.wrap(Integer.valueOf(1), Integer.class, false, classHelper));
    assertNotNull(Scalars.wrap(1L, long.class, false, classHelper));
    assertNotNull(Scalars.wrap(Long.valueOf(1), Long.class, false, classHelper));
    assertNotNull(Scalars.wrap(1f, float.class, false, classHelper));
    assertNotNull(Scalars.wrap(Float.valueOf(1), Float.class, false, classHelper));
    assertNotNull(Scalars.wrap(1d, double.class, false, classHelper));
    assertNotNull(Scalars.wrap(Double.valueOf(1), Double.class, false, classHelper));
    assertNotNull(Scalars.wrap(new byte[] {1}, byte[].class, false, classHelper));
    assertNotNull(Scalars.wrap("", String.class, false, classHelper));
    assertNotNull(Scalars.wrap(new BigDecimal("1"), BigDecimal.class, false, classHelper));
    assertNotNull(Scalars.wrap(new BigInteger("1"), BigInteger.class, false, classHelper));
    assertNotNull(Scalars.wrap(Date.valueOf("2009-01-23"), Date.class, false, classHelper));
    assertNotNull(Scalars.wrap(Time.valueOf("12:34:56"), Time.class, false, classHelper));
    assertNotNull(
        Scalars.wrap(
            Timestamp.valueOf("2009-01-23 12:34:56"), Timestamp.class, false, classHelper));
    assertNotNull(Scalars.wrap(new java.util.Date(), java.util.Date.class, false, classHelper));
    assertNotNull(Scalars.wrap(LocalDate.of(2009, 01, 23), LocalDate.class, false, classHelper));
    assertNotNull(
        Scalars.wrap(
            LocalDateTime.of(2009, 01, 23, 12, 34, 56), LocalDateTime.class, false, classHelper));
    assertNotNull(Scalars.wrap(LocalTime.of(12, 34, 56), LocalTime.class, false, classHelper));
    assertNotNull(Scalars.wrap(null, Array.class, false, classHelper));
    assertNotNull(Scalars.wrap(null, Blob.class, false, classHelper));
    assertNotNull(Scalars.wrap(null, Clob.class, false, classHelper));
    assertNotNull(Scalars.wrap(null, NClob.class, false, classHelper));
  }

  public void testWrapBasic_primitiveType() throws Exception {
    var supplier = Scalars.wrap(Integer.valueOf(10), int.class, false, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertEquals(Integer.valueOf(10), scalar.get());

    var wrapper = scalar.getWrapper();
    assertEquals(IntegerWrapper.class, wrapper.getClass());
    assertEquals(Integer.valueOf(10), wrapper.get());
  }

  public void testWrapBasic_null() throws Exception {
    var supplier = Scalars.wrap(null, Integer.class, false, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertNull(null, scalar.get());

    var wrapper = scalar.getWrapper();
    assertEquals(IntegerWrapper.class, wrapper.getClass());
    assertNull(null, wrapper.get());
  }

  @SuppressWarnings("unchecked")
  public void testWrapBasic_optional() throws Exception {
    var supplier = Scalars.wrap(Integer.valueOf(10), Integer.class, true, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertTrue(scalar.get() instanceof Optional);
    var optional = (Optional<Integer>) scalar.get();
    assertEquals(Integer.valueOf(10), optional.get());

    var wrapper = scalar.getWrapper();
    assertEquals(IntegerWrapper.class, wrapper.getClass());
    assertEquals(Integer.valueOf(10), wrapper.get());
  }

  @SuppressWarnings("unchecked")
  public void testWrapBasic_optional_null() throws Exception {
    var supplier = Scalars.wrap(null, Integer.class, true, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertTrue(scalar.get() instanceof Optional);
    var optional = (Optional<Integer>) scalar.get();
    assertFalse(optional.isPresent());

    var wrapper = scalar.getWrapper();
    assertEquals(IntegerWrapper.class, wrapper.getClass());
    assertNull(wrapper.get());
  }

  public void testWrapEnum() throws Exception {
    var supplier = Scalars.wrap(MyEnum.AAA, MyEnum.class, false, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertEquals(MyEnum.AAA, scalar.get());

    var wrapper = supplier.get().getWrapper();
    assertEquals(EnumWrapper.class, wrapper.getClass());
    assertEquals(MyEnum.AAA, wrapper.get());
  }

  public void testWrapEnum_null() throws Exception {
    var supplier = Scalars.wrap(null, MyEnum.class, false, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertNull(scalar.get());

    var wrapper = scalar.getWrapper();
    assertEquals(EnumWrapper.class, wrapper.getClass());
    assertNull(wrapper.get());
  }

  @SuppressWarnings("unchecked")
  public void testWrapEnum_optional() throws Exception {
    var supplier = Scalars.wrap(MyEnum.AAA, MyEnum.class, true, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertTrue(scalar.get() instanceof Optional);
    var optional = (Optional<MyEnum>) scalar.get();
    assertEquals(MyEnum.AAA, optional.get());

    var wrapper = scalar.getWrapper();
    assertEquals(EnumWrapper.class, wrapper.getClass());
    assertEquals(MyEnum.AAA, wrapper.get());
  }

  @SuppressWarnings("unchecked")
  public void testWrapEnum_optional_null() throws Exception {
    var supplier = Scalars.wrap(null, MyEnum.class, true, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertTrue(scalar.get() instanceof Optional);
    var optional = (Optional<MyEnum>) scalar.get();
    assertFalse(optional.isPresent());

    var wrapper = scalar.getWrapper();
    assertEquals(EnumWrapper.class, wrapper.getClass());
    assertNull(wrapper.get());
  }

  public void testWrapHolder() throws Exception {
    var phoneNumber = new PhoneNumber("123-456-789");
    var supplier = Scalars.wrap(phoneNumber, PhoneNumber.class, false, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertEquals(phoneNumber, scalar.get());

    var wrapper = scalar.getWrapper();
    assertEquals(StringWrapper.class, wrapper.getClass());
    assertEquals("123-456-789", wrapper.get());
  }

  public void testWrapHolder_subclass() throws Exception {
    PhoneNumber phoneNumber = new InternationalPhoneNumber("123-456-789");
    var supplier = Scalars.wrap(phoneNumber, InternationalPhoneNumber.class, false, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertTrue(scalar.get() instanceof InternationalPhoneNumber);

    var wrapper = scalar.getWrapper();
    assertEquals(StringWrapper.class, wrapper.getClass());
    assertEquals("123-456-789", wrapper.get());
  }

  public void testWrapHolder_null() throws Exception {
    var supplier = Scalars.wrap(null, PhoneNumber.class, false, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertTrue(scalar.get() instanceof PhoneNumber);
    var phoneNumber = (PhoneNumber) scalar.get();
    assertNull(phoneNumber.getValue());

    var wrapper = scalar.getWrapper();
    assertEquals(StringWrapper.class, wrapper.getClass());
    assertNull(wrapper.get());
  }

  @SuppressWarnings("unchecked")
  public void testWrapHolder_option() throws Exception {
    var phoneNumber = new PhoneNumber("123-456-789");
    var supplier = Scalars.wrap(phoneNumber, PhoneNumber.class, true, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertTrue(scalar.get() instanceof Optional);
    var optional = (Optional<PhoneNumber>) scalar.get();
    assertEquals(phoneNumber, optional.get());

    var wrapper = scalar.getWrapper();
    assertEquals(StringWrapper.class, wrapper.getClass());
    assertEquals("123-456-789", wrapper.get());
  }

  @SuppressWarnings("unchecked")
  public void testWrapHolder_option_null() throws Exception {
    var supplier = Scalars.wrap(null, PhoneNumber.class, true, classHelper);
    assertNotNull(supplier);

    var scalar = supplier.get();
    assertTrue(scalar.get() instanceof Optional);
    var optional = (Optional<PhoneNumber>) scalar.get();
    assertFalse(optional.isPresent());

    var wrapper = scalar.getWrapper();
    assertEquals(StringWrapper.class, wrapper.getClass());
    assertEquals(null, wrapper.get());
  }

  public enum MyEnum {
    AAA,
    BBB,
    CCC
  }
}
