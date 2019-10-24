package com.larryhsiao.twillio_whatsapp_client;

import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.tk.TkFixed;
import org.takes.tk.TkWithType;

import java.io.IOException;

/**
 * Entry point of this sample.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        new FtBasic(
                new TkFork(
                        new FkMethods("GET", new TkFixed("Hello world."))
                ),
                8080
        ).start(Exit.NEVER);
    }
}