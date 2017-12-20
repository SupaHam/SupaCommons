package com.supaham.commons.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * Represents a {@link DataSource} implementation for {@link HikariDataSource}.
 */
public class CDataSource implements DataSource {
    
    private final HikariDataSource hikariCP;
    
    public CDataSource(HikariDataSource hikariCP) {
        this.hikariCP = hikariCP;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return hikariCP.getConnection();
    }

    @Override
    public Connection getConnection(String s, String s2) throws SQLException {
        return hikariCP.getConnection();
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
