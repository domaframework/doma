package org.seasar.doma.internal.apt.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Time;
import java.sql.Timestamp;
import org.seasar.doma.Entity;

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
