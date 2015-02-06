package com.supaham.commons.jdbc.sql;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import com.supaham.commons.utils.MapUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a Map implementation that is meant to store {@link Table}s to ids. This class is
 * completely independent from anything SQL-related.
 */
public class TableMap {

  private final Map<String, Table> tables;

  public TableMap() {
    this(new HashMap<String, Table>());
  }

  public TableMap(@Nonnull Map<String, Table> tables) {
    checkNotNull(tables, "tables cannot be null.");
    this.tables = tables;
  }

  /**
   * Checks if this {@link TableMap} contains a table id.
   *
   * @param tableId table id to check for
   *
   * @return true if this {@link TableMap} contains the {@code tableId}
   *
   * @see #hasTable(Table)
   * @see #hasTableByName(String)
   */
  public boolean hasTable(@Nonnull String tableId) {
    checkNotNullOrEmpty(tableId, "tableId");
    return this.tables.containsKey(tableId);
  }

  /**
   * Checks if this {@link TableMap} contains a table. This is not to be confused with the actual
   * database, the {@link TableMap} can contain a table, along side a display name and a schema.
   *
   * @param table table to check for
   *
   * @return true if this {@link TableMap} contains the {@code tableName}
   */
  public boolean hasTable(@Nonnull Table table) {
    checkNotNull(table, "table");
    return this.tables.containsValue(table);
  }

  /**
   * Checks if this {@link TableMap} contains a table by its name.
   *
   * @param tableName table name to check for
   *
   * @return true if this {@link TableMap} contains a table with the name as {@code tableName}
   *
   * @throws NullPointerException thrown if {@code tableName} is null
   * @throws IllegalArgumentException thrown if {@code tableName} is empty
   * @see #getTableIdByName(String)
   */
  public boolean hasTableByName(@Nonnull String tableName) {
    checkNotNullOrEmpty(tableName, "tableName");
    return getTableIdByName(tableName) != null;
  }

  /**
   * Gracefully gets a table id by a {@link Table}'s name.
   *
   * @param tableName table name to check for. If null or empty, null is returned.
   *
   * @return the table id the the {@code tableName} belongs to
   */
  @Nullable
  public String getTableIdByName(@Nullable String tableName) {
    if (tableName == null || tableName.isEmpty()) {
      return null;
    }
    for (Entry<String, Table> entry : tables.entrySet()) {
      if (entry.getValue().getName().equals(tableName)) {
        return entry.getKey();
      }
    }
    return null;
  }

  /**
   * Gracefully gets a {@link Table}'s name from this {@link TableMap}.
   *
   * @param tableId id of the table to get the name for. If null or empty, null is returned.
   *
   * @return the name assigned to table {@code id}, nullable
   */
  @Nullable
  public Table getTable(@Nullable String tableId) {
    if (tableId == null || tableId.isEmpty()) {
      return null;
    }
    return this.tables.get(tableId);
  }

  /**
   * Gracefully gets a {@link Table}'s name from this {@link TableMap} by table id.
   *
   * @param tableId id of the table to get the name for. If null or empty, null is returned.
   *
   * @return the name assigned to table {@code id}, nullable
   *
   * @see #getTable(String)
   */
  @Nullable
  public String getTableName(@Nullable String tableId) {
    Table table = getTable(tableId);
    return table == null ? null : table.getName();
  }

  /**
   * Adds a table to this display name
   *
   * @param id
   * @param name
   * @param schema
   */
  public void addTable(@Nonnull String id, @Nonnull String name, @Nonnull String schema)
      throws IllegalArgumentException {
    checkNotNullOrEmpty(id, "id");
    Table table = new Table(name, schema);
    addTable(id, table);
  }

  /**
   * Adds a {@link Table} to this {@link TableMap}.
   *
   * @param id id to give to the table.
   * @param table table to add
   *
   * @throws IllegalArgumentException thrown if the {@code id} already exists in this table
   */
  public void addTable(@Nonnull String id, @Nonnull Table table) throws IllegalArgumentException {
    checkNotNullOrEmpty(id, "id");
    checkArgument(!this.tables.containsKey(id), "table already exists in this database.");
    this.tables.put(id, table);
  }

  /**
   * Removes a {@link Table} from this {@link TableMap}.
   *
   * @param table table to remove
   *
   * @return true if the {@code table} was found and removed
   */
  public boolean removeTable(@Nonnull Table table) {
    return MapUtils.removeValue(this.tables, table) != null;
  }

  /**
   * Removes a {@link Table} by id from this {@link TableMap}.
   *
   * @param id id of the table to remove
   *
   * @return removed table, nullable
   */
  @Nullable
  public Table removeTable(@Nonnull String id) {
    checkNotNullOrEmpty(id, "id");
    return this.tables.remove(id);
  }

  /**
   * Gets a {@link Map} of the table ids and their {@link Table} instances from this {@link
   * TableMap}.
   *
   * @return map of tables
   */
  public Map<String, Table> getTables() {
    return Collections.unmodifiableMap(this.tables);
  }

  /* ================================
   * >> DELEGATE METHODS
   * ================================ */

  /**
   * @see Map#clear()
   */
  public void clear() {
    this.tables.clear();
  }

  /**
   * @see Map#keySet()
   */
  public Set<String> keySet() {
    return tables.keySet();
  }

  /**
   * @see Map#values()
   */
  public Collection<Table> values() {
    return tables.values();
  }

  /**
   * @see Map#entrySet()
   */
  public Set<Entry<String, Table>> entrySet() {
    return tables.entrySet();
  }
}
