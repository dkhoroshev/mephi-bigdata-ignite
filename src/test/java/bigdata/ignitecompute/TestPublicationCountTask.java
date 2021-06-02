package bigdata.ignitecompute;

import bigdata.ignitecompute.model.Publication;
import bigdata.ignitecompute.model.VisitLog;
import bigdata.ignitecompute.service.PublicationCountTask;
import bigdata.ignitecompute.service.VisitLogCountTask;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.cache.Cache;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestPublicationCountTask {
    private Ignite ignite;
    CacheConfiguration<UUID, Publication> publicationIgniteCache;
    CacheConfiguration<UUID, VisitLog> visitLogIgniteCache;



    @Before
    public void TestPublicationCountTaskConfiguration() {

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
    }
}
