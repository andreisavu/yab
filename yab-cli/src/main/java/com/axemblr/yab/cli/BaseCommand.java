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

import com.amazonaws.regions.Regions;
import io.airlift.command.Option;

public abstract class BaseCommand implements Runnable {

    @Option(name = {"-r", "--region"}, description = "Region name (default AWS SDK region)")
    private String region = Regions.DEFAULT_REGION.getName();

    public String getRegion() {
        return region;
    }
}
