package org.kie.server.services.ping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.kie.server.services.api.KieContainerInstance;
import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.KieServerExtension;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.api.SupportedTransports;
import org.kie.server.services.impl.KieServerImpl;

public class PingExtension implements KieServerExtension {

    public static final String EXTENSION_NAME = "Ping";

    private PingService pingService;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void init(KieServerImpl kieServer, KieServerRegistry registry) {
        pingService = new PingService();
    }

    @Override
    public void destroy(KieServerImpl kieServer, KieServerRegistry registry) {
        pingService = null;
    }

    @Override
    public void createContainer(String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters) {
        kieContainerInstance.addService(pingService);
    }

    @Override
    public void disposeContainer(String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters) {
        kieContainerInstance.removeService(PingService.class);
    }

    @Override
    public List<Object> getAppComponents(SupportedTransports type) {
        ServiceLoader<KieServerApplicationComponentsService> appComponentsServices
            = ServiceLoader.load(KieServerApplicationComponentsService.class);
        List<Object> appComponentsList = new ArrayList<Object>();
        Object [] services = {pingService};

        for( KieServerApplicationComponentsService appComponentsService : appComponentsServices ) {
           appComponentsList.addAll(appComponentsService.getAppComponents(EXTENSION_NAME, type, services));
        }
        return appComponentsList;
    }

    @Override
    public <T> T getAppComponents(Class<T> serviceType) {
        if (serviceType.isAssignableFrom(pingService.getClass())) {
            return (T) pingService;
        }

        return null;
    }

    @Override
    public String getImplementedCapability() {
        // Hello service.
        return "HS";
    }

    @Override
    public String toString() {
        return EXTENSION_NAME + " extension";
    }
}
