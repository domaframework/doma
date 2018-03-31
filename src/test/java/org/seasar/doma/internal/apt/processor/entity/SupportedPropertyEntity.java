package org.seasar.doma.internal.apt.processor.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import org.seasar.doma.Entity;

/** @author taedium */
@Entity
public class SupportedPropertyEntity {

  boolean primitiveBoolean;

  byte primitiveByte;

  short primitiveShort;

  int primitiveInt;

  long primitiveLong;

  float primitiveFloat;

  double primitiveDouble;

  byte[] primitiveBytes;

  Boolean booleanObject;

  Byte byteObject;

  Integer integerObject;

  Long longObject;

  Float floatObject;

  Double doubleObject;

  String string;

  Blob blob;

  Clob clob;

  NClob nclob;

  Array array;

  BigDecimal bigDecimal;

  BigInteger bigInteger;

  Date date;

  Time time;

  Timestamp timestamp;
}
