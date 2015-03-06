package com.supaham.commons.bukkit.worldedit;

import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.auth.Authorizer;

import org.bukkit.command.CommandSender;

/**
 * @since 0.1
 */
public class CommandAuthorizer implements Authorizer {
    
    @Override
    public boolean testPermission(CommandLocals locals, String permission) {
        return locals.get(CommandSender.class).hasPermission(permission);
    }
}
