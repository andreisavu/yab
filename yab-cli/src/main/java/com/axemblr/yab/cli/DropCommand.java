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
import io.airlift.command.Command;
import io.airlift.command.Option;

@Command(name = "drop", description = "Drop  an existing AMI")
public class DropCommand extends BaseCommand {

    @Option(name = "--id", description = "Amazon Image ID")
    private String imageId;

    @Override
    public void run() {
        YaB yab = YaB.createWithEnvironmentCredentials(getRegion());
        try {
            System.out.println(";; using region " + getRegion());
            yab.deleteImageAndRelatedSnapshot(imageId);

        } finally {
            yab.close();
        }
    }
}
