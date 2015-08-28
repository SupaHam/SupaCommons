package com.supaham.commons.geo;

/**
 * Represents a Geographical location.
 *
 * @since 0.1
 */
public class GeoLocation {

  /**
   * Latitude of this Geographical location.
   */
  private float latitude;
  /**
   * Longitude of this Geographical location.
   */
  private float longitude;
  /**
   * Timezone of this Geographical location.
   */
  // TODO later, Not very important atm.
  // private ZoneId timezone;

  /**
   * Constructs a new immutable {@link GeoLocation}.
   *
   * @param latitude latitude coordinate of the location
   * @param longitude longitude coordinate of the location
   */
  public GeoLocation(float latitude, float longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public float getLatitude() {
    return latitude;
  }

  public float getLongitude() {
    return longitude;
  }
}
