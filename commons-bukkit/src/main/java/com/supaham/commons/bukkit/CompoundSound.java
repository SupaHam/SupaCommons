package com.supaham.commons.bukkit;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class CompoundSound extends Sound {
    private final SingleSound[] singleSounds;

    /**
     * Constructs a compound sound with a List of SingleSound objects.
     *
     * @param singleSounds sounds to play
     * @see SingleSound
     */
    public CompoundSound(@Nonnull SingleSound[] singleSounds) {
        this.singleSounds = singleSounds;
    }


    @Override
    public void play(@NotNull Player player, @NotNull Location location) {
        for (SingleSound singleSound : singleSounds) {
            singleSound.play(player, location);
        }
    }
}
