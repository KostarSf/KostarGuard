package com.github.kostarsf.kostarguard;

import com.velocitypowered.api.command.RawCommand;
import net.kyori.adventure.text.Component;

public final class VersionCommand implements RawCommand {

    @Override
    public void execute(Invocation invocation) {
        invocation.source().sendMessage(Component.text(PluginDetails.NAME + " v" + PluginDetails.VERSION));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("command.base");
    }
}
