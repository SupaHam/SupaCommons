package com.supaham.commons.utils;

import java.util.Collection;
import java.util.List;

/**
 * Utility methods for working with {@link Collection}s. This class contains methods such as
 * {@link #getRandomElement(List)}, and more.
 *
 * @since 0.1
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }
    
    /**
     * Gets a random element in a {@link List}.
     *
     * @param list list to use
     * @return the random element
     */
    public static <T> T getRandomElement(List<T> list) {
        return list.get(NumberUtils.getRandom().nextInt(list.size()));
    }
}
