/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.mock;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.seasar.doma.internal.util.AssertionUtil;

/**
 * @author taedium
 * 
 */
public class MockCallableStatement extends MockPreparedStatement implements
        CallableStatement {

    public List<Object> outParameters = new ArrayList<Object>();

    public List<RegisterOutParameter> registerOutParameters = new ArrayList<RegisterOutParameter>();

    protected boolean wasNull;

    public MockCallableStatement() {
    }

    public MockCallableStatement(MockResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public Array getArray(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Array getArray(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BigDecimal getBigDecimal(int parameterIndex, int scale)
            throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Blob getBlob(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Blob getBlob(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
        notYetImplemented();
        return false;
    }

    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
        notYetImplemented();
        return false;
    }

    @Override
    public byte getByte(int parameterIndex) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public byte getByte(String parameterName) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Clob getClob(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Clob getClob(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Date getDate(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Date getDate(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public double getDouble(int parameterIndex) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public double getDouble(String parameterName) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public float getFloat(int parameterIndex) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public float getFloat(String parameterName) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public int getInt(int parameterIndex) throws SQLException {
        wasNull = false;
        Object value = outParameters.get(parameterIndex - 1);
        if (value == null) {
            wasNull = true;
            return 0;
        }
        return (Integer) value;
    }

    @Override
    public int getInt(String parameterName) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public long getLong(int parameterIndex) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public long getLong(String parameterName) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public NClob getNClob(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public String getNString(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public String getNString(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map)
            throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Object getObject(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map)
            throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Object getObject(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Ref getRef(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Ref getRef(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public RowId getRowId(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public short getShort(int parameterIndex) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public short getShort(String parameterName) throws SQLException {
        notYetImplemented();
        return 0;
    }

    @Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public String getString(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public String getString(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Time getTime(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Time getTime(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal)
            throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Timestamp getTimestamp(String parameterName, Calendar cal)
            throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public URL getURL(int parameterIndex) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public URL getURL(String parameterName) throws SQLException {
        notYetImplemented();
        return null;
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType,
            String typeName) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType)
            throws SQLException {
        registerOutParameters.add(new RegisterOutParameter(parameterIndex,
                sqlType));
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType,
            int scale) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType,
            String typeName) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setBigDecimal(String parameterName, BigDecimal x)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, int length)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream,
            long length) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader,
            int length) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader,
            long length) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setClob(String parameterName, Reader reader, long length)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setClob(String parameterName, Reader reader)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setDate(String parameterName, Date x, Calendar cal)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setDate(String parameterName, Date x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setFloat(String parameterName, float x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setInt(String parameterName, int x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setLong(String parameterName, long x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value,
            long length) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setNClob(String parameterName, NClob value) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setNClob(String parameterName, Reader reader, long length)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setNClob(String parameterName, Reader reader)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setNString(String parameterName, String value)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setNull(String parameterName, int sqlType, String typeName)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType,
            int scale) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setObject(String parameterName, Object x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setShort(String parameterName, short x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setString(String parameterName, String x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setTime(String parameterName, Time x, Calendar cal)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setTime(String parameterName, Time x) throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x)
            throws SQLException {
        notYetImplemented();

    }

    @Override
    public void setURL(String parameterName, URL val) throws SQLException {
        notYetImplemented();

    }

    @Override
    public boolean wasNull() throws SQLException {
        return wasNull;
    }

    @SuppressWarnings("all")
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType,
            int scaleOrLength) throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType)
            throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public long executeLargeUpdate() throws SQLException {
        AssertionUtil.notYetImplemented();
        return 0;
    }

    @SuppressWarnings("all")
    public void closeOnCompletion() throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public boolean isCloseOnCompletion() throws SQLException {
        AssertionUtil.notYetImplemented();
        return false;
    }

    @SuppressWarnings("all")
    public long getLargeUpdateCount() throws SQLException {
        AssertionUtil.notYetImplemented();
        return 0;
    }

    @SuppressWarnings("all")
    public void setLargeMaxRows(long max) throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public long getLargeMaxRows() throws SQLException {
        AssertionUtil.notYetImplemented();
        return 0;
    }

    @SuppressWarnings("all")
    public long[] executeLargeBatch() throws SQLException {
        AssertionUtil.notYetImplemented();
        return null;
    }

    @SuppressWarnings("all")
    public long executeLargeUpdate(String sql) throws SQLException {
        AssertionUtil.notYetImplemented();
        return 0;
    }

    @SuppressWarnings("all")
    public long executeLargeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException {
        AssertionUtil.notYetImplemented();
        return 0;
    }

    @SuppressWarnings("all")
    public long executeLargeUpdate(String sql, int[] columnIndexes)
            throws SQLException {
        AssertionUtil.notYetImplemented();
        return 0;
    }

    @SuppressWarnings("all")
    public long executeLargeUpdate(String sql, String[] columnNames)
            throws SQLException {
        AssertionUtil.notYetImplemented();
        return 0;
    }

    @SuppressWarnings("all")
    public <T> T getObject(int parameterIndex, Class<T> type)
            throws SQLException {
        AssertionUtil.notYetImplemented();
        return null;
    }

    @SuppressWarnings("all")
    public <T> T getObject(String parameterName, Class<T> type)
            throws SQLException {
        AssertionUtil.notYetImplemented();
        return null;
    }

    @SuppressWarnings("all")
    public void setObject(String parameterName, Object x,
            SQLType targetSqlType, int scaleOrLength) throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public void setObject(String parameterName, Object x, SQLType targetSqlType)
            throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public void registerOutParameter(int parameterIndex, SQLType sqlType)
            throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public void registerOutParameter(int parameterIndex, SQLType sqlType,
            int scale) throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public void registerOutParameter(int parameterIndex, SQLType sqlType,
            String typeName) throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public void registerOutParameter(String parameterName, SQLType sqlType)
            throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public void registerOutParameter(String parameterName, SQLType sqlType,
            int scale) throws SQLException {
        AssertionUtil.notYetImplemented();
    }

    @SuppressWarnings("all")
    public void registerOutParameter(String parameterName, SQLType sqlType,
            String typeName) throws SQLException {
        AssertionUtil.notYetImplemented();
    }

}
