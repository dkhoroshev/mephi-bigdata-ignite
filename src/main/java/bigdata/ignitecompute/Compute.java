package bigdata.ignitecompute;

import bigdata.ignitecompute.config.IgniteConf;
import bigdata.ignitecompute.dao.VisitLogRepository;
import bigdata.ignitecompute.dao.PublicationRepository;
import bigdata.ignitecompute.service.PublicationCountTask;
import bigdata.ignitecompute.service.VisitLogCountTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.*;
import org.apache.ignite.configuration.IgniteConfiguration;

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

        IgniteConfiguration conf = IgniteConf.conf();
        // Запуск узла
        log.warn("===================================Запуск узла Ignite");
        Ignite ignite = Ignition.start(conf);

        log.info("Создание IgniteCache и присвоение ему значений.");
        // Создание IgniteCache и присвоение ему значений.
        PublicationRepository pubRepo = new PublicationRepository(ignite,"publication");
        VisitLogRepository vislogRepo = new VisitLogRepository(ignite,"visitlog");

        //Записываем в кэш Публикации
        log.info("Чтение файла с публикациями и запись его в базу");
        String publicationCount = pubRepo.add(filePublicarions);
        //Записываем в кэш историю посещений
        log.info("Чтение файла с историей посещений и запись его в базу");
        String visitLogCount = vislogRepo.add(fileVisitLog);

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

        log.info("Result of compute task=" + mapResult.size());
        System.out.println("Sorted results\nYear,UserID\t\tResult");
        for (Map.Entry<String, String> entry: mapResult.entrySet()) {
            System.out.println(entry.getKey() + "  \t=>\t" + entry.getValue());
        }

        log.info(">> Compute task is executed, check for output on the server nodes.");

        // Сбрасываем кэш
        pubRepo.destroy();
        vislogRepo.destroy();
        ignite.close();
    }
}
