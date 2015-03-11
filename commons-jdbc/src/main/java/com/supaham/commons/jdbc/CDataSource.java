package com.supaham.commons.jdbc;

import com.jolbox.bonecp.BoneCP;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * Represents a {@link DataSource} implementation for {@link BoneCP}.
 */
public class CDataSource implements DataSource {
    
    private final BoneCP boneCP;
    
    public CDataSource(BoneCP boneCP) {
        this.boneCP = boneCP;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return boneCP.getConnection();
    }

    @Override
    public Connection getConnection(String s, String s2) throws SQLException {
        return boneCP.getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int i) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> tClass) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;
    }
}
