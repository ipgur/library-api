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
package library.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics
public class MetricsReporterConfig extends MetricsConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(MetricsReporterConfig.class);

    @Autowired
    private ApplicationContext context;

    @Value("${monitoring.metrics.reporter.updatePeriodSeconds: 60}")
    private long updatePeriodSeconds;

    @Value("${monitoring.metrics.reporter.name:none}")
    private String reporterName;

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        LOG.info("Metrics reporter configured to {} with update interval of {} seconds", reporterName, updatePeriodSeconds);
        switch (reporterName.toLowerCase()) {
            case "console":
                registerConsoleReporter(metricRegistry);
                break;
            case "cloudwatch":
                registerCloudWatchReporter(metricRegistry);
                break;
            case "none":
                LOG.info("No reporter used; either configured or defaulted.");
                break;
            default:
                throw new IllegalArgumentException("Reporter" + this.reporterName + " not known");
        }
    }

    /**
     * registers a cloud watch reporter
     * @param metricRegistry
     */
    private void registerCloudWatchReporter(MetricRegistry metricRegistry) {
        LOG.info("Registering cloud watch reporter");
        final MetricsReporterProvider provider = context.getBean(MetricsCloudWatchReporterProvider.class);
        registerReporter(provider.createReporter(metricRegistry)).start(updatePeriodSeconds, TimeUnit.SECONDS);
    }

    /**
     * registers a console reporter
     * @param metricRegistry
     */
    private void registerConsoleReporter(MetricRegistry metricRegistry) {
        LOG.info("Registering console reporter");
        registerReporter(ConsoleReporter.forRegistry(metricRegistry).build()).start(updatePeriodSeconds, TimeUnit.SECONDS);
    }
}
