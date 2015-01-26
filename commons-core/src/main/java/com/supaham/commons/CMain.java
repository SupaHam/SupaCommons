package com.supaham.commons;

import static com.google.common.base.Preconditions.checkState;

import java.util.logging.Logger;

import lombok.Getter;
import lombok.NonNull;

/**
 * Commons main class.
 */
@Getter
public class CMain {
  
  private static CMain CMAIN = null;
  public final Logger logger;

  /**
   * Gets the instance of this singleton class.
   * @return {@link CMain} instance.
   */
  public static CMain get() {
    return CMain.CMAIN;
  }
  
  public static void main(@NonNull Logger logger) {
    checkState(CMain.CMAIN == null, "already initialized.");
    CMain.CMAIN = new CMain(logger);
  }
  
  private CMain(@NonNull Logger logger) {
    this.logger = logger;
  }
}
