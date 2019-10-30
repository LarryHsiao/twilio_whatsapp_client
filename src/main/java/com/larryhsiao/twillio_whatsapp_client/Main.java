package com.larryhsiao.twillio_whatsapp_client;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.rq.RqPrint;
import org.takes.rq.form.RqFormSmart;
import org.takes.rs.RsWithStatus;

/**
 * Entry point of this sample.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        Options options = new Options();
        options.addOption("p", "port", true, "Specify the port to run.");
        options.addOption("i", "id", true, "The Twilio Account Id");
        options.addOption("t", "token", true, "The Twilio auth token");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (!cmd.hasOption("i") || !cmd.hasOption("t")) {
            throw new RuntimeException("Please provide account_id and token");
        }

        final String accountId = cmd.getOptionValue("i");
        final String authToken = cmd.getOptionValue("t");

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
                            req -> {
                                RqFormSmart form = new RqFormSmart(req);
                                okhttp3.Response res = new OkHttpClient().newCall(new okhttp3.Request.Builder()
                                    .url("https://api.twilio.com/2010-04-01/Accounts/" + accountId + "/Messages.json")
                                    .post(new FormBody.Builder()
                                        .add("From", form.single("To"))
                                        .add("Body", "Hello there " + form.single("Body"))
                                        .add("To", form.single("From"))
                                        .add("MediaUrl","https://demo.twilio.com/owl.png?_ga=2.190318443.1560312424.1572420697-1071513074.1571815802")
                                        .build())
                                    .addHeader("Authorization", Credentials.basic(accountId, authToken))
                                    .build()
                                ).execute();
                                if (!res.isSuccessful()) {
                                    return new RsWithStatus(500);
                                }
                                return new RsWithStatus(203);
                            }
                        )
                    )
                ),
                new FkRegex(
                    "/twilio/callback/status",
                    new TkFork(
                        new FkMethods(
                            "POST",
                            req -> {
                                System.out.println(new RqPrint(req).printHead());
                                System.out.println(new RqPrint(req).printBody());
                                return new RsWithStatus(201);
                            }
                        )
                    )
                )
            ),
            port
        ).start(Exit.NEVER);
    }
}