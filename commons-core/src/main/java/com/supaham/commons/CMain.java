package com.supaham.commons;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Preconditions;

import java.io.File;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

/**
 * Commons main class.
 *
 * @since 0.1
 */
public class CMain {

  private static CMain CMAIN = null;

  private final File homeDirectory = new File("supacommons/");
  private final Logger logger;

  /**
   * Gets the instance of this singleton class.
   *
   * @return {@link CMain} instance.
   */
  public static CMain get() {
    return CMain.CMAIN;
  }

  public static void main(@Nonnull Logger logger) {
    Preconditions.checkNotNull(logger, "logger cannot be null.");
    checkState(CMain.CMAIN == null, "already initialized.");
    CMain.CMAIN = new CMain(logger);
  }

  public static Logger getLogger() {
    return get().logger;
  }

  public static File getHomeDirectory() {
    return get().homeDirectory;
  }

  private CMain(@Nonnull Logger logger) {
    Preconditions.checkNotNull(logger, "logger cannot be null.");
    this.logger = logger;
    this.homeDirectory.mkdirs();
  }
}
