package bigdata.ignitecompute;

import bigdata.ignitecompute.model.Publication;
import bigdata.ignitecompute.model.VisitLog;
import org.apache.ignite.*;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Compute {
    public static void main(String[] args) throws IgniteException {
        // файл target/cache
        String fileCache = "taget/ignitecache";

        String filePublicarions = args[0];
        String fileVisitLog = args[1];

        // Подготовка IgniteConfiguration для использования Java APIs
        IgniteConfiguration cfg = new IgniteConfiguration();

        // Setting up an IP Finder to ensure the client can locate the servers.
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

        DataStorageConfiguration storageCnf = new DataStorageConfiguration();

        storageCnf.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
        //"/root/lab2/cache"
        storageCnf.setStoragePath(fileCache);

        //   // Запуск узла, как клиента.
        cfg.setClientMode(true);

        // Classes of custom Java logic will be transferred over the wire from this app.
        cfg.setPeerClassLoadingEnabled(true);



        // Запуск узла
        Ignite ignite = Ignition.start();

        // Создание IgniteCache и присвоение ему значений.
        IgniteCache<Integer, Publication> publicationIgniteCache = ignite.getOrCreateCache("publication");
        IgniteCache<Integer, VisitLog> visitLogIgniteCache = ignite.getOrCreateCache("visitlog");

        String publicationCount = null;
        String visitLogCount = null;
        String countItog;
//        Date date;
        //Записываем в кэш задание
        try {
            int i = 1;
            //"/root/lab2/input"
            File file = new File(filePublicarions);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line !=null){
                String[] col = line.split(",",0);
//                date = new SimpleDateFormat("YYYY-MM-dd").parse(col[2].substring(0,4));
                publicationIgniteCache.put(i, new Publication(Integer.parseInt(col[0]),Integer.parseInt(col[1]), new SimpleDateFormat("YYYY-MM-dd").parse(col[2]),col[3]));
                line = reader.readLine();
                i++;
            }

            publicationCount = Integer.toString(i);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException|ParseException e) {
            e.printStackTrace();
        }

        //Записываем в кэш справочник аэропорт-страна
        try {
            int i = 1;
            //"/root/lab2/ttttt.txt"
            File file1 = new File(fileVisitLog);
            FileReader fr1 = new FileReader(file1);
            BufferedReader reader1 = new BufferedReader(fr1);
            String line1 = reader1.readLine();
            while (line1 !=null){
                String[] col = line1.split(",");
//                date = new SimpleDateFormat("YYYY-MM-dd H:m:s").parse(col[2]);
                visitLogIgniteCache.put(i, new VisitLog(Integer.parseInt(col[0]),Integer.parseInt(col[1]),new SimpleDateFormat("YYYY-MM-dd H:m:s").parse(col[2]),Boolean.parseBoolean(col[3])));
                line1 = reader1.readLine();
                i++;
            }
            visitLogCount = Integer.toString(i);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException|ParseException e) {
            e.printStackTrace();
        }
        //Записали количество строк в обоих кэшах
        countItog = publicationCount + "," + visitLogCount;

        IgniteCompute compute = ignite.compute();

        // Запускаем задачу
        Map<String, String> mapPiblications = compute.execute(PublicationCountTask.class, countItog);
        Map<String, String> mapVisitLog = compute.execute(VisitLogCountTask.class, countItog);

        System.out.println("Result of compute Publication task" + mapPiblications);
        System.out.println("Result of compute Visits task" + mapVisitLog);

        HashMap<String, String> mapResult = new HashMap(mapPiblications);
        mapVisitLog.forEach(
                (key, value) -> mapResult.merge( key, value, (v1, v2) -> v1.equalsIgnoreCase(v2) ? v1 : v1 + "," + v2)
        );
        System.out.println("Result of compute Publication task" + mapPiblications);
        System.out.println("Result of compute Visits task" + mapVisitLog);
        System.out.println(">> Compute task is executed, check for output on the server nodes.");

        // Сбрасываем кэш
        publicationIgniteCache.destroy();
        visitLogIgniteCache.destroy();
        ignite.close();
    }
}
