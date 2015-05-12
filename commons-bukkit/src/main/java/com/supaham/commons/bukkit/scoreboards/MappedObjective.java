package com.supaham.commons.bukkit.scoreboards;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import com.supaham.commons.utils.StringUtils;

import org.bukkit.scoreboard.Objective;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a mapped {@link Objective}, providing the ability to display entries in a specific
 * order with key-value support. This feature utilizes the entry's score in the scoreboard, making
 * it impossible to utilize from this API (or any for that matter).
 *
 * @see IndexedObjective
 */
public class MappedObjective {

  private final String NULL_ENTRY = "~~NULLSTRING~~"; // null-safety for the table
  private final CommonScoreboard scoreboard;
  private final IndexedObjective objective;
  private final Table<Integer, String, String> table = HashBasedTable.create(7, 1);

  /**
   * Constructs a new mapped objective. This operation only sets the display slot. The name of the
   * objective is the given id.
   *
   * @param scoreboard scoreboard this objective should belong to
   * @param id id of the objective
   */
  public MappedObjective(@Nonnull CommonScoreboard scoreboard, @Nonnull String id) {
    this.scoreboard = Preconditions.checkNotNull(scoreboard, "scoreboard cannot be null.");
    StringUtils.checkNotNullOrEmpty(id, "id");
    this.objective = new IndexedObjective(scoreboard, id);
  }

  private int sanitizeIndex(int index) {
    return index * 2;
  }

  private int sanitizeForValueIndex(int index) {
    return index + 1;
  }

  private int validateIndex(int index) {
    if (index < 0 || index > 6) {
      throw new IndexOutOfBoundsException(String.valueOf(index));
    }
    return index;
  }

  private boolean isNull(String string) {
    return string == null || string.equals(NULL_ENTRY);
  }

  /**
   * Returns whether this objective contains an index.
   *
   * @param index inclusive index of the entry to check
   *
   * @return whether this objective contains the index
   */
  public boolean contains(int index) {
    return !this.table.row(index).isEmpty();
  }

  /**
   * Returns whether this indexed objective contains a key.
   *
   * @param key key to check
   *
   * @return whether this objective contains the key
   */
  public boolean contains(String key) {
    return !this.table.column(key).isEmpty();
  }

  /**
   * Returns a key by index. If the index is not set in this objective, null is returned.
   *
   * @param index index to get key for
   *
   * @return key, nullable
   */
  public String getKey(int index) {
    validateIndex(index);
    Map<String, String> row = this.table.row(index);
    return row.isEmpty() ? null : row.keySet().iterator().next();
  }

  /**
   * Returns an inclusive index by key. If the key is not found in this objective, -1 is returned.
   *
   * @param key key to get index for
   *
   * @return inclusive index
   */
  public int getIndex(@Nonnull String key) {
    Preconditions.checkNotNull(key, "key cannot be null.");
    Map<Integer, String> column = this.table.column(key);
    return column.isEmpty() ? -1 : column.keySet().iterator().next();
  }

  /**
   * Returns a value by index.
   *
   * @param index index to get value from
   *
   * @return value, nullable
   */
  public String getValue(int index) {
    validateIndex(index);
    Map<String, String> row = this.table.row(index);
    if (row.isEmpty()) {
      return null;
    }
    String value = row.values().iterator().next();
    return isNull(value) ? null : value;
  }

  /**
   * Returns a value by key.
   *
   * @param key key to get value from
   *
   * @return found value, nullable
   */
  public String getValue(@Nonnull String key) {
    Preconditions.checkNotNull(key, "key cannot be null.");
    Map<Integer, String> column = this.table.column(key);
    if (column.isEmpty()) {
      return null;
    }
    String value = column.values().iterator().next();
    return isNull(value) ? null : value;
  }

  /**
   * Inserts an entry into this objective. If the given value is null, it simply won't be added to
   * the display.
   *
   * @param index inclusive index to insert the entry into
   * @param key key to insert alongside the index
   * @param value value to insert alongside the key, nullable
   *
   * @return the previous key replaced by this insertion
   */
  public String put(int index, @Nonnull String key, @Nullable String value) {
    validateIndex(index);
    Preconditions.checkNotNull(key, "key cannot be null.");

    int sanIndex = sanitizeIndex(index);
    String oldKey = objective.put(sanIndex, key);

    String valueToSet = value;
    if (valueToSet == null) { // null-safety for the table
      valueToSet = NULL_ENTRY;
    }
    int valueIndex = sanitizeForValueIndex(sanIndex);
    if (isNull(valueToSet)) { // Value is null, remove it in case it exists.
      objective.remove(valueIndex);
    } else {
      objective.put(valueIndex, value);
    }
    this.table.put(index, key, valueToSet);
    return oldKey;
  }

  /**
   * Removes an entry by index from this objective.
   *
   * @param index inclusive index to remove
   *
   * @return the removed key.
   */
  public String remove(int index) {
    validateIndex(index);
    Map<String, String> row = this.table.row(index);
    if (row.isEmpty()) { // index not set
      return null;
    }
    int sanIndex = sanitizeIndex(index);
    this.objective.remove(sanIndex);
    String key = row.keySet().iterator().next();
    String value = getValue(key);
    if (!isNull(value)) {
      this.objective.remove(sanitizeForValueIndex(sanIndex));
    }
    return key;
  }

  public CommonScoreboard getScoreboard() {
    return scoreboard;
  }

  public IndexedObjective getObjective() {
    return objective;
  }
}
