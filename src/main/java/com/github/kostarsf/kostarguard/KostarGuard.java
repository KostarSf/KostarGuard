package com.github.kostarsf.kostarguard;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "kostarguard", name = "KostarGuard", version = "0.1.0-SNAPSHOT",
        url = "https://https://github.com/KostarSf/KostarGuard",
        authors = {"KostarSf"}, description = "A server protection for Velocity")
public class KostarGuard {
    private final ProxyServer server;
    private final Logger logger;

    private final Path dataDirectory;
    private final Path usernamesList;

    @Inject
    public KostarGuard(ProxyServer server, Logger logger,
                       @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        logger.info("Running KostarGuard v0.1.0-SNAPSHOT");

        usernamesList = getUsernamesList();
        UsernamesDatabase.syncWithFile(usernamesList);

        int entriesCount = UsernamesDatabase.getEntriesCount();
        logger.info("There is "+entriesCount+" entries in database");
    }

    private Path getUsernamesList() {
        Path usernamesList = dataDirectory.resolve("usernames.txt");

        try {
            if (Files.exists(dataDirectory) == false) {
                Files.createDirectory(dataDirectory);
            }
            if (Files.exists(usernamesList) == false) {
                Files.createFile(usernamesList);
                logger.info("New usernames list has been created");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usernamesList;
    }

    private void syncUsernamesDatabaseWithList() {

    }

    @Subscribe
    public void onPreLoginEvent(PreLoginEvent event) {
        String name = event.getUsername();
        logger.info("Player "+name+" is trying to connect...");

        UsernameValidation validation = new UsernameValidation(name);
        if (validation.isNewEntry()) {
            logger.info("Player "+name+" is a newcomer. New record has been created");
        }

        if (validation.isPassed()) {
            logger.info("Username is valid. Login request proceeded.");
        } else {
            logger.info("Bad letter casing for "+name+". A valid is "
                    +validation.getValidUsername()+". Login request rejected.");

            Component component = Component.text("Bad username");
            PreLoginEvent.PreLoginComponentResult result =
                    PreLoginEvent.PreLoginComponentResult.denied(component);
            event.setResult(result);
        }
    }
}
