package com.larryhsiao.twillio_whatsapp_client;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.rq.RqPrint;
import org.takes.rs.RsEmpty;
import org.takes.tk.TkFixed;

import java.io.IOException;

/**
 * Entry point of this sample.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        Options options = new Options();
        options.addOption("p", "port", true, "Specify the port to run.");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("p")) {
            port = Integer.parseInt(cmd.getOptionValue("p"));
        }

        new FtBasic(
            new TkFork(
                new FkRegex(
                    "/twilio/callback/message",
                    new TkFork(
                        new FkMethods(
                            "POST",
                            new Take() {
                                @Override
                                public Response act(Request req) throws IOException {
                                    System.out.println(new RqPrint(req).printHead());
                                    System.out.println(new RqPrint(req).printBody());
                                    return new RsEmpty();
                                }
                            }
                        )
                    )
                ),
                new FkRegex(
                    "/twilio/callback/status",
                    new TkFork(
                        new FkMethods(
                            "POST",
                            new Take() {
                                @Override
                                public Response act(Request req) throws IOException {
                                    System.out.println(new RqPrint(req).printHead());
                                    System.out.println(new RqPrint(req).printBody());
                                    return new RsEmpty();
                                }
                            }
                        )
                    )
                )
            ),
            port
        ).start(Exit.NEVER);
    }
}