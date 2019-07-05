/*
 * Copyright 2019 igur.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.strumski.library.metrics;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsync;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClientBuilder;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import io.github.azagniotov.metrics.reporter.cloudwatch.CloudWatchReporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Provides an AWS cloud watch provider
 */
@Component
public class MetricsCloudWatchReporterProvider implements MetricsReporterProvider {

    @Value("${monitoring.metrics.reporter.cloudwatch.namespace:strumski}")
    private String cloudWatchNamespace;

    /**
     *
     * @param metricRegistry the default metrics registry
     * @return a scheduled reported that can be started
     */
    @Override
    public ScheduledReporter createReporter(MetricRegistry metricRegistry) {
        final AmazonCloudWatchAsync amazonCloudWatchAsync = AmazonCloudWatchAsyncClientBuilder
                .standard()
                .build();

        return CloudWatchReporter.forRegistry(metricRegistry, amazonCloudWatchAsync, cloudWatchNamespace)
                .build();

    }
}
