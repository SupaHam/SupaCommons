package com.supaham.commons.geo;

/**
 * Represents a safe null implementation of {@link GeoLocation}.
 *
 * @since 0.1
 */
public class NullGeoLocation extends GeoLocation {

  public NullGeoLocation() {
    super(0, 0);
  }

  @Override
  public boolean equals(Object obj) {
    return obj == null || obj instanceof NullGeoLocation; // Not sure if '== null' is a good idea...
  }
}
