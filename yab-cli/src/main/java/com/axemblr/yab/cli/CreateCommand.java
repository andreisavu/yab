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
import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.Option;
import java.util.Collections;
import java.util.List;

@Command(name = "create", description = "Bake a new AMI from a list of templates")
public class CreateCommand extends BaseCommand {

    @Option(name = "-b", description = "Override base AMI ID from templates")
    private String baseImageId;

    @Arguments(description = "Paths to one or multiple template files")
    private List<String> templates = Collections.emptyList();

    @Override
    public void run() {
        YaB yab = YaB.createWithEnvironmentCredentials(getRegion());

        try {
            System.out.println(";; using region " + getRegion());
            // TODO: load all templates, merge & validate


        } finally {
            yab.close();
        }
    }
}
