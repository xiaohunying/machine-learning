package com.ayawala.machinelearningalgorithms.service;

import com.ayawala.machinelearningalgorithms.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlgorithmCatalogLoader {

    // 30 Minutes
    private static final int REFRESH_SCHEDULED_DELAY_IN_MILLS = 1800000;

    private static final Logger LOGGER = LoggerFactory.getLogger(AlgorithmCatalogLoader.class);

    private final AlgorithmCatalog algorithmCatalog;

    private final ApplicationProperties applicationProperties;

    public AlgorithmCatalogLoader(final AlgorithmCatalog algorithmCatalog,
                                  final ApplicationProperties applicationProperties) {
        this.algorithmCatalog = algorithmCatalog;
        this.applicationProperties = applicationProperties;
    }

    private void load() {
        if(Boolean.TRUE.equals(applicationProperties.getAsyncLoad())){
            algorithmCatalog.loadAsync();
        } else {
            algorithmCatalog.load();
        }
    }

    @EventListener
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        LOGGER.info("Config Refreshed - Reloading Algorithm Catalog");
        load();
    }

    @Scheduled(fixedDelay = REFRESH_SCHEDULED_DELAY_IN_MILLS)
    public void refreshOnSchedule() {
        LOGGER.info("Scheduled Refresh - reloading Algorithm Catalog");
        load();
    }
}
