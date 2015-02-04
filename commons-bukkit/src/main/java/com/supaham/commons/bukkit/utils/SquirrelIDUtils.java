package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import com.sk89q.squirrelid.cache.HashMapCache;
import com.sk89q.squirrelid.cache.ProfileCache;
import com.sk89q.squirrelid.cache.SQLiteCache;
import com.sk89q.squirrelid.resolver.BukkitPlayerService;
import com.sk89q.squirrelid.resolver.CacheForwardingService;
import com.sk89q.squirrelid.resolver.CombinedProfileService;
import com.sk89q.squirrelid.resolver.HttpRepositoryService;
import com.supaham.commons.CMain;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with the SquirrelID dependency. This class contains methods such as
 * {@link #createCacheForwardingService(ProfileCache)}, {@link #getProfileCache()}, and more.
 *
 * @since 0.1
 */
public class SquirrelIDUtils {
  
  public static final File CACHE_FILE;
  private static ProfileCache PROFILE_CACHE;
  
  static {
    CACHE_FILE = new File(CMain.getHomeDirectory(), "profiles.sqlite");
    CACHE_FILE.getParentFile().mkdirs();
    
    try {
      PROFILE_CACHE = new SQLiteCache(CACHE_FILE);
    } catch (IOException e) {
      CMain.getLogger().warning("Failed to initialize SQLite profile cache:");
      e.printStackTrace();
      PROFILE_CACHE = new HashMapCache();
    }
  }

  public static ProfileCache getProfileCache() {
    return PROFILE_CACHE;
  }
  
  public static CacheForwardingService createCacheForwardingService() {
    return createCacheForwardingService(PROFILE_CACHE);
  }

  public static CacheForwardingService createCacheForwardingService(
      @Nonnull ProfileCache profileCache) {
    checkNotNull(profileCache, "profileCache cannot be null.");
    return new CacheForwardingService(
        new CombinedProfileService(BukkitPlayerService.getInstance(),
                                   HttpRepositoryService.forMinecraft()), profileCache);
  }
  
  private SquirrelIDUtils() {
  }
}
