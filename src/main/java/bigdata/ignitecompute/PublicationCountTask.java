package bigdata.ignitecompute;

import bigdata.ignitecompute.model.Publication;
import bigdata.ignitecompute.model.VisitLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.*;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.*;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class PublicationCountTask extends ComputeTaskAdapter<String, Map> {
    @IgniteInstanceResource
    Ignite ignite;

    @NotNull
    @Override
    public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> nodes, String arg) {
        String[] count = arg.split(",");
        // Set date format
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy");
        log.info("Создаем таблицф для публикаций");
        IgniteCache<Integer, Publication> publicationIgniteCache = ignite.getOrCreateCache("publication");

        Map<ComputeJob,ClusterNode> mapPublications = new HashMap<>();
//        Map<ComputeJob,ClusterNode> mapVisitLogs = new HashMap<>();
        Iterator<ClusterNode> it = nodes.iterator();
//        Iterable<Cache.Entry<Integer, Publication>> iterable = () -> publicationIgniteCache.iterator();
        int sizePub = publicationIgniteCache.size();

//        List<Publication> publications = StreamSupport
//                .stream(iterable.spliterator(), false)
//                .map(Cache.Entry::getValue)
//                .collect(Collectors.toList());
//        for (Publication publicationCacheString : publications) {
        for (int i =1; i < sizePub;i++) {
            Publication publicationCacheString = publicationIgniteCache.get(i);


            if (!it.hasNext())
                it = nodes.iterator();
            ClusterNode node = it.next();

//            int finalI = i;
            mapPublications.put(new ComputeJobAdapter() {
                @Nullable
                @Override
                public Object execute() throws IgniteException {
                    String uid = publicationCacheString.getUid().toString();
                    String date = DateFormat.format(publicationCacheString.getPubdate());
                    return date + ',' + uid;
                }
            }, node);
        }

//        int sizeVisits = visitLogIgniteCache.size();
//        for (int i = 1; i < sizeVisits; i++) {
//            VisitLog visitLogCacheString = visitLogIgniteCache.get(i);
//            if (!it.hasNext())
//                it = nodes.iterator();
//            ClusterNode node = it.next();
//            mapVisitLogs.put(new ComputeJobAdapter() {
//                @Nullable
//                @Override
//                public Object execute() throws IgniteException {
//                    String uid = visitLogCacheString.getUid().toString();
//                    String date = visitLogCacheString.getEventdate().toString().substring(0, 4);
//                    Boolean inout = visitLogCacheString.getInout();
//                    Long time = visitLogCacheString.getEventdate().getTime();
//                    if (inout){
//                        time = -time;
//                    }
//
//
//
//                    return date + ',' + uid + ',' + time;
//                }
//            },node);
//        }

        return mapPublications;
    }

    @Override
    public Map<String, String> reduce(List<ComputeJobResult> results) {

        Map<String,Integer> counter = new HashMap<>();
        for (ComputeJobResult res : results) {
//            String[] grouped = res.getData().toString().split(",");
            int grouped = counter.getOrDefault(res.getData(),0) +1;
            counter.put(res.getData(),grouped);
        }

        Map<String,String> mapOut = new HashMap(counter);
//        System.out.println(">>> results on from compute job" + mapOut);
        return mapOut;
    }
}
