/**
 * Copyright 2013 Andrei Savu (asavu@apache.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.axemblr.yab.cli;

import com.axemblr.yab.YaB;
import com.google.common.collect.ImmutableList;
import io.airlift.command.Cli;
import io.airlift.command.Command;
import io.airlift.command.Help;

/**
 * Entry point for command line tool
 */
public class Main {

    @Command(name = "version")
    public static class VersionCommand implements Runnable {

        @Override
        public void run() {
            System.out.println("YaB " + YaB.Version.INSTANCE);
            System.out.println("Java " + System.getProperty("java.version"));
        }
    }

    public static void main(String[] args) {
        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("yab")
            .withDescription("YaB: Yet (another) AMI Baker")
            .withDefaultCommand(Help.class)
            .withCommands(ImmutableList.of(Help.class, ListCommand.class,
                CreateCommand.class, CopyCommand.class, DropCommand.class,
                VersionCommand.class));

        Cli<Runnable> parser = builder.build();
        try {
            parser.parse(args).run();

        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}
