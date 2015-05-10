package com.supaham.commons;

import java.util.UUID;

/**
 * Represents an object that has a {@link UUID}.
 *
 * @since 0.2.3
 */
public interface Uuidable {

  /**
   * Returns this object's uuid.
   *
   * @return this object's uuid
   */
  UUID getUuid();
}
