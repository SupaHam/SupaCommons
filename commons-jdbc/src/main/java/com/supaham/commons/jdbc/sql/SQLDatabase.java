package com.supaham.commons.jdbc.sql;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import com.supaham.commons.jdbc.utils.SQLUtils;
import com.supaham.commons.placeholders.PlaceholderData;
import com.supaham.commons.placeholders.PlaceholderSet;

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
   * Checks whether a table (by id) exists in this {@link SQLDatabase}. If it does not exist it is
   * created using its {@link Table} schema. This method is equivalent to calling {@code
   * checkTable(Table)}, where the {@link Table} instance is retrieved via 
   * {@link #getTable(String)}.
   *
   * @param tableId table id to check out
   *
   * @return true if the table did not exist and was created, otherwise null if {@code tableId} is
   * not a valid tableId in this {@link SQLDatabase}
   *
   * @see #checkTable(JdbcTemplate, Table)
   */
  @Nullable
  protected Boolean checkTable(@Nonnull String tableId) {
    checkNotNullOrEmpty(tableId, "tableId");
    Table table = getTable(tableId);
    return table != null ? checkTable(table) : null;
  }

  /**
   * Checks whether a {@link Table} exists in this {@link SQLDatabase}. If it does not exist it is
   * created using its own schema. This method is equivalent to calling {@code
   * checkTable&#040;SpringJDBCAgent.createJdbcTemplate&#040;&#041;, Table&#041;}.
   *
   * @param table table to check out
   *
   * @return true if the table did not exist and was created
   *
   * @see #checkTable(JdbcTemplate, Table)
   */
  protected boolean checkTable(@Nonnull Table table) {
    return checkTable(this.jdbcAgent.createJdbcTemplate(), table);
  }

  /**
   * Checks whether a table (by id) exists in this {@link SQLDatabase}. If it does not exist it is
   * created using its {@link Table} schema. This method is equivalent to calling {@code
   * checkTable(JdbcTemplate, Table)}, where the {@link Table} instance is retrieved via 
   * {@link #getTable(String)}. 
   *
   * @param tableId table id to check out
   *
   * @return true if the table did not exist and was created, otherwise null if {@code tableId} is
   * not a valid tableId in this {@link SQLDatabase}
   *
   * @see #checkTable(JdbcTemplate, Table)
   */
  @Nullable
  protected Boolean checkTable(@Nonnull JdbcTemplate template, @Nonnull String tableId) {
    checkNotNullOrEmpty(tableId, "tableId");
    Table table = getTable(tableId);
    return table != null ? checkTable(template, table) : null;
  }

  /**
   * Checks whether a {@link Table} exists in this {@link SQLDatabase}. If it does not exist it is
   * created using its own schema. This method is equivalent to calling {@code
   * checkTable(JdbcTemplate, Table, null)}.
   *
   * @param template template to use for checking the table, in case it needs to be created
   * @param table table to check out
   *
   * @return true if the table did not exist and was created
   *
   * @see #checkTable(JdbcTemplate, Table, PlaceholderSet)
   */
  protected boolean checkTable(@Nonnull JdbcTemplate template, @Nonnull Table table) {
    return checkTable(template, table, null);
  }

  /**
   * Checks whether a table (by id) exists in this {@link SQLDatabase}. If it does not exist it is
   * created using its {@link Table} schema. This method is equivalent to calling {@link
   * #checkTable(Table, PlaceholderSet)}, where the {@link Table} instance is retrieved via 
   * {@link #getTable(String)}. 
   *
   * @param tableId table id to check out
   *
   * @return true if the table did not exist and was created, otherwise null if {@code tableId} is
   * not a valid tableId in this {@link SQLDatabase}
   *
   * @see #checkTable(JdbcTemplate, Table)
   */
  @Nullable
  protected Boolean checkTable(@Nonnull String tableId,
                               @Nullable PlaceholderSet placeholders) {
    checkNotNullOrEmpty(tableId, "tableId");
    Table table = getTable(tableId);
    return table != null ? checkTable(table, placeholders) : null;
  }

  /**
   * Checks whether a {@link Table} exists in this {@link SQLDatabase}. If it does not exist it is
   * created using its own schema. This method is equivalent to calling {@code
   * checkTable&#040;SpringJDBCAgent.createJdbcTemplate&#040;&#041;, Table, PlaceholderSet&#041;}
   *
   * @param table table to check out
   * @param placeholders {@link PlaceholderSet} to use on the schema
   *
   * @return true if the table did not exist and was created
   *
   * @see #checkTable(JdbcTemplate, Table, PlaceholderSet)
   */
  protected boolean checkTable(@Nonnull Table table,
                               @Nullable PlaceholderSet placeholders) {
    return checkTable(this.jdbcAgent.createJdbcTemplate(), table, placeholders);
  }

  /**
   * Checks whether a {@link Table} exists in this {@link SQLDatabase}. If it does not exist it is
   * created using its own schema.
   *
   * @param template template to use for checking the table, in case it needs to be created
   * @param table table to check out
   * @param placeholders {@link PlaceholderSet} to use on the schema
   * 
   * @return true if the table did not exist and was created
   */
  protected boolean checkTable(@Nonnull JdbcTemplate template, @Nonnull Table table,
                            @Nullable PlaceholderSet placeholders) {
    checkNotNull(template, "template cannot be null.");
    checkNotNull(table, "table cannot be null.");
    checkArgument(this.tableMap.hasTable(table), "table doesn't belong to this database.");

    // Don't attempt to create the table if it's schema is NO_SCHEMA
    if (table.getSchema().equals(Table.NO_SCHEMA)) {
      return true;
    }

    String name = table.getName();
    this.logger.fine("Checking table '" + name + "'.");
    if (!SQLUtils.hasTable(this.jdbcAgent.getDataSource(), name)) {
      this.logger.fine("'" + name + "' table doesn't exist, creating it...");
      String schema = table.getSchema();
      if (placeholders != null && !placeholders.isEmpty()) {
        PlaceholderData data = PlaceholderData.builder()
            .input(schema)
            .put(table)
            .put(template).build();
        schema = placeholders.apply(data);
      }
      template.execute(schema);
      return true;
    } else {
      this.logger.finer("table '" + name + "' already exists");
      return false;
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
   * Creates and adds a new {@link Table} with the schema as {@link Table#NO_SCHEMA} which
   * specifies
   * that when checking the table, don't bother creating if it doesn't exist. This is equivalent to
   * calling {@code TableMap.addTable(String, String, null)}.
   *
   * @see TableMap#addTable(String, String, String)
   */
  public void addTable(@Nonnull String id, @Nonnull String name)
      throws IllegalArgumentException {
    tableMap.addTable(id, name, null);
  }

  /**
   * @see TableMap#addTable(String, String, String)
   */
  public void addTable(@Nonnull String id, @Nonnull String name, @Nullable String schema)
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
