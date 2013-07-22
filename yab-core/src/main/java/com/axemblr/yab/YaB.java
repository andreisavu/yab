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

package com.axemblr.yab;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DeleteSnapshotRequest;
import com.amazonaws.services.ec2.model.DeregisterImageRequest;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.DescribeSnapshotsRequest;
import com.amazonaws.services.ec2.model.DescribeSnapshotsResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.Snapshot;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Closeable;
import java.util.List;

public final class YaB implements Closeable {

    /**
     * Use the same default region as the AWS SDK
     */
    public static final String DEFAULT_REGION = Regions.DEFAULT_REGION.getName();

    public static enum Version {
        INSTANCE;

        private final String version;

        private Version() {
            this.version = Version.class.getPackage().getSpecificationVersion();
        }


        @Override
        public String toString() {
            return version;
        }
    }

    /**
     * Constructs a new YaB client by fetching credentials in this order:
     * <p>
     * - Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_KEY
     * - Java System Properties - aws.accessKeyId and aws.secretKey
     * - Instance profile credentials delivered through the Amazon EC2 metadata service
     * </p>
     */
    public static YaB createWithEnvironmentCredentials(String region) {
        AmazonEC2Client client = new AmazonEC2Client();

        boolean regionFound = false;
        DescribeRegionsResult result = client.describeRegions();
        for (Region candidate : result.getRegions()) {
            if (candidate.getRegionName().equals(region)) {
                client.setEndpoint(candidate.getEndpoint());
                regionFound = true;
                break;
            }
        }

        if (!regionFound) {
            throw new IllegalArgumentException("No region found with this name: " + region);
        }

        return new YaB(client);
    }

    private final AmazonEC2 client;

    YaB(AmazonEC2 client) {
        this.client = checkNotNull(client, "client is null");
    }

    /**
     * @return a list of (probably) backed images
     */
    public List<Image> describeBackedImages() {
        DescribeImagesRequest request = new DescribeImagesRequest().withOwners("self");
        DescribeImagesResult result = client.describeImages(request);

        return result.getImages();
    }

    /**
     * De-register AMI and delete related snapshot
     */
    public void deleteImageAndRelatedSnapshot(String imageId) {
        client.deregisterImage(new DeregisterImageRequest().withImageId(imageId));

        final String pattern = "for " + imageId + " from vol-";
        DescribeSnapshotsResult result = client.describeSnapshots(new DescribeSnapshotsRequest());

        for (Snapshot candidate : result.getSnapshots()) {
            if (candidate.getDescription().contains(pattern)) {
                client.deleteSnapshot(new DeleteSnapshotRequest()
                    .withSnapshotId(candidate.getSnapshotId()));
            }
        }
    }

    /**
     * Close the underlying Amazon EC2 client
     */
    @Override
    public void close() {
        client.shutdown();
    }
}
