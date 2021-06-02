package bigdata.ignitecompute.config;

import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Collections;

public class IgniteConf {
    public static IgniteConfiguration conf(){
        // Подготовка IgniteConfiguration для использования Java APIs
        IgniteConfiguration cfg = new IgniteConfiguration();

        // Setting up an IP Finder to ensure the client can locate the servers.
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

        DataStorageConfiguration storageCnf = new DataStorageConfiguration();

        storageCnf.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
        storageCnf.setStoragePath("/tmp/ignitecache");

        // Classes of custom Java logic will be transferred over the wire from this app.
        cfg.setPeerClassLoadingEnabled(true);
        cfg.setGridLogger(new Slf4jLogger());
        return cfg;
    }
}
