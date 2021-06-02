package bigdata.ignitecompute;

import bigdata.ignitecompute.config.IgniteConf;
import bigdata.ignitecompute.dao.VisitLogRepository;
import bigdata.ignitecompute.model.Publication;
import bigdata.ignitecompute.model.VisitLog;
import bigdata.ignitecompute.service.PublicationCountTask;
import bigdata.ignitecompute.service.VisitLogCountTask;
import bigdata.ignitecompute.dao.PublicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.*;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class Compute {
    /**
     * Start up node with compute configuration.
     *
     * @param args Command line arguments, none required.
     * @throws IgniteException If failed.
     */
    public static void main(String[] args) throws IgniteException {
        if (args.length < 2) {
            throw new RuntimeException("You should specify Publications and VisitLog initial data!");
        }
        // файл с данными о публикации
        String filePublicarions = args[0];

        // файл с данными о посещениях
        String fileVisitLog = args[1];

//        // Подготовка IgniteConfiguration для использования Java APIs
//        IgniteConfiguration cfg = new IgniteConfiguration();
//
//        // Setting up an IP Finder to ensure the client can locate the servers.
//        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
//        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
//        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
//
//        DataStorageConfiguration storageCnf = new DataStorageConfiguration();
//
//        storageCnf.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
//        storageCnf.setStoragePath("/tmp/ignitecache");
//
//        // Classes of custom Java logic will be transferred over the wire from this app.
//        cfg.setPeerClassLoadingEnabled(true);
//        cfg.setGridLogger(new Slf4jLogger());
        IgniteConfiguration conf = IgniteConf.conf();
        // Запуск узла
//        log.info("Запуск узла Ignite");
        Ignite ignite = Ignition.start(conf);
        IgniteLogger log = ignite.log();

        log.info("Создание IgniteCache и присвоение ему значений.");
        // Создание IgniteCache и присвоение ему значений.
//        IgniteCache<Integer, Publication> publicationIgniteCache = ignite.getOrCreateCache("publication");
//        IgniteCache<Integer, VisitLog> visitLogIgniteCache = ignite.getOrCreateCache("visitlog");
        PublicationRepository pubRepo = new PublicationRepository(ignite,"publication");
        VisitLogRepository vislogRepo = new VisitLogRepository(ignite,"visitlog");

        String publicationCount = pubRepo.add(filePublicarions);
        System.out.println(publicationCount);
        String visitLogCount = vislogRepo.add(fileVisitLog);
        System.out.println(visitLogCount);
//        String publicationCount = null;
//        String visitLogCount = null;
//        String countItog;
        //Записываем в кэш Публикации
        log.info("Чтение файла с публикациями и запись его в базу");
//        try {
//            int i = 1;
//            File file = new File(filePublicarions);
//            FileReader fr = new FileReader(file);
//            BufferedReader reader = new BufferedReader(fr);
//            String line = reader.readLine();
//            while (line !=null){
//                String[] col = line.split(",",0);
//                publicationIgniteCache.put(i, new Publication(Integer.parseInt(col[0]),Integer.parseInt(col[1]), new SimpleDateFormat("YYYY-MM-dd").parse(col[2]),col[3]));
//                line = reader.readLine();
//                i++;
//            }
//
//            publicationCount = Integer.toString(i);
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException|ParseException e) {
//            e.printStackTrace();
//        }

        //Записываем в кэш историю посещений
        log.info("Чтение файла с историей посещений и запись его в базу");
//        try {
//            int i = 1;
//            File file1 = new File(fileVisitLog);
//            FileReader fr1 = new FileReader(file1);
//            BufferedReader reader1 = new BufferedReader(fr1);
//            String line1 = reader1.readLine();
//            while (line1 !=null){
//                String[] col = line1.split(",");
//                visitLogIgniteCache.put(i, new VisitLog(Integer.parseInt(col[0]),Integer.parseInt(col[1]),new SimpleDateFormat("YYYY-MM-dd H:m:s").parse(col[2]),Boolean.parseBoolean(col[3])));
//                line1 = reader1.readLine();
//                i++;
//            }
//            visitLogCount = Integer.toString(i);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException|ParseException e) {
//            e.printStackTrace();
//        }
        //Записали количество строк в обоих кэшах
        String countItog = publicationCount + "," + visitLogCount;


        IgniteCompute compute = ignite.compute();

        // Запускаем задачи
        log.info("=> Запуск задачи подсчета публикаций");
        Map<String, String> mapPiblications = compute.execute(PublicationCountTask.class, countItog);
        log.info("==> Задача подсчета количества публикаций завершена");
        log.info("=> Запуск задачи подсчета часов посещений");
        Map<String, String> mapVisitLog = compute.execute(VisitLogCountTask.class, countItog);
        log.info("==> Задача подсчета количества часов посещений завершена");

        log.info("++ Сводим результаты в одну таблицу");
        SortedMap<String, String> mapResult = new TreeMap(mapPiblications);
        mapVisitLog.forEach(
                (key, value) -> mapResult.merge( key, value, (v1, v2) -> v1.equalsIgnoreCase(v2) ? v1 : v1 + "," + v2)
        );

        System.out.println("Result of compute task=" + mapResult.size());
        System.out.println("Sorted results\nYear,UserID\t\tResult");
        for (Map.Entry<String, String> entry: mapResult.entrySet()) {
            System.out.println(entry.getKey() + "  \t=>\t" + entry.getValue());
        }

        System.out.println(">> Compute task is executed, check for output on the server nodes.");

        // Сбрасываем кэш
//        publicationIgniteCache.destroy();
//        visitLogIgniteCache.destroy();
        ignite.close();
    }
}
