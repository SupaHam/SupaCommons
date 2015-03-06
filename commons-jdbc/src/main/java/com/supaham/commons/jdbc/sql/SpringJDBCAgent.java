package com.supaham.commons.jdbc.sql;

import static com.google.common.base.Preconditions.checkNotNull;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.supaham.commons.database.JDBCAgent;
import com.supaham.commons.jdbc.CDataSource;

import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

/**
 * Represents a Spring JDBC implementation of {@link JDBCAgent} that uses {@link SQLConfig}.
 */
public class SpringJDBCAgent implements JDBCAgent {

  private final SQLConfig configuration;
  private final DataSource dataSource;

  /**
   * Creates a new {@link SpringJDBCAgent}. This method takes a {@link ClassLoader} as a parameter
   * which is used to look for the MySQL Driver class. This method is equivalent to calling {@code
   * SpringJDBCAgent.createAgent(MySQLConfig, null)}.
   *
   * @param config mysql configuration used to connect to the database
   *
   * @return a new instance of {@link SpringJDBCAgent}
   *
   * @throws ClassNotFoundException thrown if the mysql driver is not found.
   * @throws SQLException thrown if the {@code config} data fails to connect to a mysql database
   * @see #createAgent(SQLConfig, ClassLoader)
   */
  public static SpringJDBCAgent createAgent(@Nonnull SQLConfig config)
      throws ClassNotFoundException, SQLException {
    return createAgent(config, Thread.currentThread().getContextClassLoader());
  }

  /**
   * Creates a new {@link SpringJDBCAgent}. This method takes a {@link ClassLoader} as a parameter
   * which is used to look for the MySQL Driver class.
   *
   * @param config mysql configuration used to connect to the database
   * @param classLoader the {@link ClassLoader} that will load the the database Driver class
   *
   * @return a new instance of {@link SpringJDBCAgent}
   *
   * @throws ClassNotFoundException thrown if the mysql driver is not found.
   * @throws SQLException thrown if the {@code config} data fails to connect to a mysql database
   */
  public static SpringJDBCAgent createAgent(@Nonnull SQLConfig config,
                                            @Nonnull ClassLoader classLoader)
      throws ClassNotFoundException, SQLException {
    return createAgent(config, classLoader, null);
  }

  /**
   * Creates a new {@link SpringJDBCAgent}. This method takes a {@link ClassLoader} as a parameter
   * which is used to look for the MySQL Driver class.
   *
   * @param config mysql configuration used to connect to the database
   * @param classLoader the {@link ClassLoader} that will load the the database Driver class
   *
   * @return a new instance of {@link SpringJDBCAgent}
   *
   * @throws ClassNotFoundException thrown if the mysql driver is not found.
   * @throws SQLException thrown if the {@code config} data fails to connect to a mysql database
   */
  public static SpringJDBCAgent createAgent(@Nonnull SQLConfig config,
                                            @Nonnull BoneCPConfig boneCPConfig)
      throws ClassNotFoundException, SQLException {
    return createAgent(config, Thread.currentThread().getContextClassLoader(), boneCPConfig);
  }

  /**
   * Creates a new {@link SpringJDBCAgent}. This method takes a {@link ClassLoader} as a parameter
   * which is used to look for the MySQL Driver class.
   *
   * @param config mysql configuration used to connect to the database
   * @param classLoader the {@link ClassLoader} that will load the the database Driver class
   * @param boneCPConfig {@link BoneCPConfig}
   *
   * @return a new instance of {@link SpringJDBCAgent}
   *
   * @throws ClassNotFoundException thrown if the mysql driver is not found.
   * @throws SQLException thrown if the {@code config} data fails to connect to a mysql database
   */
  public static SpringJDBCAgent createAgent(@Nonnull SQLConfig config,
                                            @Nullable ClassLoader classLoader,
                                            @Nullable BoneCPConfig boneCPConfig)
      throws ClassNotFoundException, SQLException {
    checkNotNull(classLoader, "class loader cannot be null.");

    ClassLoader previousClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(classLoader);

    if (boneCPConfig == null) {
      boneCPConfig = new BoneCPConfig();
    }
    if (config instanceof MySQLConfig) {
      Class.forName("com.mysql.jdbc.Driver");
      MySQLConfig mysql = (MySQLConfig) config;
      boneCPConfig.setJdbcUrl("jdbc:mysql://" + mysql.getIp() + ":" + mysql.getPort() + "/"
                              + mysql.getDatabase());
      boneCPConfig.setUser(mysql.getUsername());
      boneCPConfig.setPassword(mysql.getPassword());
    } else {
      Class.forName("org.sqlite.JDBC");
      boneCPConfig.setJdbcUrl("jdbc:sqlite:" + new File(config.getFile()).getAbsolutePath());
    }
    CDataSource dataSource = new CDataSource(new BoneCP(boneCPConfig));
    Thread.currentThread().setContextClassLoader(previousClassLoader);
    return new SpringJDBCAgent(config, dataSource);
  }

  private SpringJDBCAgent(@Nonnull SQLConfig configuration, @Nonnull DataSource dataSource)
      throws SQLException {
    checkNotNull(configuration, "configuration cannot be null.");
    checkNotNull(dataSource, "data source cannot be null.");
    this.configuration = configuration;
    this.dataSource = dataSource;
    Connection connection = this.dataSource.getConnection();
    checkNotNull(connection, "Failed to create connection, maybe the credentials are wrong?");
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public DataSource getDataSource() {
    return this.dataSource;
  }

  /**
   * Gets this {@link SpringJDBCAgent}'s {@link SQLConfig}.
   *
   * @return {@link SQLConfig}
   */
  public SQLConfig getConfiguration() {
    return configuration;
  }

  /**
   * Creates a new {@link JdbcTemplate} using this {@link SpringJDBCAgent}'s {@link DataSource}.
   *
   * @return newly constructed {@link JdbcTemplate}
   */
  public JdbcTemplate createJdbcTemplate() {
    return new JdbcTemplate(getDataSource());
  }

  /**
   * Creates a new {@link NamedParameterJdbcTemplate} using this {@link SpringJDBCAgent}'s {@link
   * DataSource}.
   *
   * @return newly constructed {@link NamedParameterJdbcTemplate}
   */
  public NamedParameterJdbcTemplate createNamedParameterJdbcTemplate() {
    return new NamedParameterJdbcTemplate(getDataSource());
  }
}
