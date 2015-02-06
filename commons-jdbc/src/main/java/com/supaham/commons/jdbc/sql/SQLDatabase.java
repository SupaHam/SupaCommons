package com.supaham.commons.jdbc.sql;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.supaham.commons.jdbc.utils.SQLUtils;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a SQL database manager used for interacting with a SQL database.
 */
public class SQLDatabase {

  private final Logger logger;
  private final SpringJDBCAgent jdbcAgent;
  private final TableMap tableMap = new TableMap();

  public SQLDatabase(@Nonnull Logger logger, @Nonnull SpringJDBCAgent jdbcAgent) {
    checkNotNull(logger, "logger cannot be null.");
    checkNotNull(jdbcAgent, "jdbc agent cannot be null.");
    this.logger = logger;
    this.jdbcAgent = jdbcAgent;
  }

  /**
   * Checks whether tables exist.
   */
  public void checkTables() {
    JdbcTemplate jdbcTemplate = this.jdbcAgent.createJdbcTemplate();
    for (Table table : this.tableMap.values()) {
      checkTable(jdbcTemplate, table);
    }
  }

  /**
   * Checks whether a {@link Table} exists in this {@link SQLDatabase}. If it does not exist it is
   * created using its own schema.
   *
   * @param template template to use for checking the table, in case it needs to be created
   * @param table table to check out
   */
  protected void checkTable(@Nonnull JdbcTemplate template, @Nonnull Table table) {
    checkNotNull(template, "template cannot be null.");
    checkNotNull(table, "table cannot be null.");
    checkArgument(this.tableMap.hasTable(table), "table doesn't belong to this database.");
    
    String name = table.getName();
    this.logger.fine("Checking table '" + name + "'.");
    if (!SQLUtils.hasTable(this.jdbcAgent.getDataSource(), name)) {
      this.logger.fine("'" + name + "' table doesn't exist, creating it...");
      template.execute(table.getSchema());
    } else {
      this.logger.finer("table '" + name + "' already exists");
    }
  }

  public SpringJDBCAgent getJdbcAgent() {
    return jdbcAgent;
  }

  /**
   * Gets this {@link SQLDatabase}'s {@link TableMap}.
   *
   * @return {@link TableMap} instance
   */
  public TableMap getTableMap() {
    return this.tableMap;
  }

  /* ================================
   * >> DELEGATE METHODS
   * ================================ */

  /**
   * @see TableMap#hasTable(String)
   */
  public boolean hasTable(@Nonnull String tableId) {
    return tableMap.hasTable(tableId);
  }

  /**
   * @see TableMap#hasTable(Table) 
   */
  public boolean hasTable(@Nonnull Table table) {
    return tableMap.hasTable(table);
  }

  /**
   * @see TableMap#hasTableByName(String)
   */
  public boolean hasTableByName(@Nonnull String tableName) {
    return tableMap.hasTableByName(tableName);
  }

  /**
   * @see TableMap#getTableIdByName(String) 
   */
  @Nullable
  public String getTableIdByName(@Nullable String tableName) {
    return tableMap.getTableIdByName(tableName);
  }

  /**
   * @see TableMap#getTable(String)
   */
  @Nullable
  public Table getTable(@Nullable String tableId) {
    return tableMap.getTable(tableId);
  }

  /**
   * @see TableMap#getTableName(String)
   */
  @Nullable
  public String getTableName(@Nullable String tableId) {
    return tableMap.getTableName(tableId);
  }

  /**
   * @see TableMap#addTable(String, String, String)
   */
  public void addTable(@Nonnull String id, @Nonnull String name, @Nonnull String schema)
      throws IllegalArgumentException {
    tableMap.addTable(id, name, schema);
  }

  /**
   * @see TableMap#addTable(String, Table)
   */
  public void addTable(@Nonnull String id, @Nonnull Table table) throws IllegalArgumentException {
    tableMap.addTable(id, table);
  }

  /**
   * @see TableMap#removeTable(Table) 
   */
  public boolean removeTable(@Nonnull Table table) {
    return tableMap.removeTable(table);
  }

  /**
   * @see TableMap#removeTable(String) 
   */
  @Nullable
  public Table removeTable(@Nonnull String id) {
    return tableMap.removeTable(id);
  }

  /**
   * @see TableMap#getTables()
   */
  public Map<String, Table> getTables() {
    return this.tableMap.getTables();
  }
}
