package de.roamingthings.workbench.resilience4j.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

import static org.testcontainers.Testcontainers.exposeHostPorts;

@Component
@Profile("metrics-infrastructure")
public class TestContainersConfiguration implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TestContainersConfiguration.class);

    @Override
    public void run(String... args) {
        Network network = Network.newNetwork();
        GenericContainer prometheus = new GenericContainer<>("prom/prometheus")
                .withNetwork(network)
                .withNetworkAliases("prometheus")
                .withExposedPorts(9090)
                .withFileSystemBind("./services/prometheus/prometheus.yml", "/etc/prometheus/prometheus.yml");
        GenericContainer grafana = new GenericContainer<>("grafana/grafana")
                .dependsOn(prometheus)
                .withNetwork(network)
                .withExposedPorts(3000)
                .withFileSystemBind("./services/grafana/provisioning", "/etc/grafana/provisioning/")
                .withFileSystemBind("./volumes/grafana/data", "/var/lib/grafana")
                .withEnv("TZ", "Europe/Berlin")
                .withEnv("GF_PATHS_DATA", "/var/lib/grafana")
                .withEnv("GF_PATHS_LOGS", "/var/log/grafana");

        exposeHostPorts(8080);
        prometheus.start();
        grafana.start();
        Integer prometheusPort = prometheus.getFirstMappedPort();
        String prometheusIp = prometheus.getContainerIpAddress();
        Integer grafanaPort = grafana.getFirstMappedPort();
        String grafanaIp = grafana.getContainerIpAddress();
        log.info("🐳 Prometheus is listening on http://{}:{}", prometheusIp, prometheusPort);
        log.info("🐳 Grafana is listening on http://{}:{}", grafanaIp, grafanaPort);
    }
}
