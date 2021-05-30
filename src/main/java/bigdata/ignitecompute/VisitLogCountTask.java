package bigdata.ignitecompute;

import bigdata.ignitecompute.model.VisitLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.*;
import org.apache.ignite.compute.*;
import org.apache.ignite.resources.IgniteInstanceResource;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class VisitLogCountTask extends ComputeTaskSplitAdapter<String, Map> {
    @IgniteInstanceResource
    Ignite ignite;

    @Override
    public List<ComputeJob> split(int gridSize, String arg) {
        String[] count = arg.split(",");
        // Set date format
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy");
        log.info("Создаем таблицф для посещений");
        IgniteCache<Integer, VisitLog> visitLogIgniteCache = ignite.getOrCreateCache("visitlog");

        List<ComputeJob> listVisitLogs = new ArrayList<>(count.length);

        int sizeVisits = visitLogIgniteCache.size();
        for (int i = 1; i < sizeVisits; i++) {
            VisitLog visitLogCacheString = visitLogIgniteCache.get(i);
            listVisitLogs.add(new ComputeJobAdapter() {
                @Override
                public Object execute() throws IgniteException {
                    String uid = visitLogCacheString.getUid().toString();
                    String date = DateFormat.format(visitLogCacheString.getEventdate());
                    Boolean inout = visitLogCacheString.getInout();
                    Long time = visitLogCacheString.getEventdate().getTime();
                    if (!inout) {
                        time = -time;
                    }

                    return date + ',' + uid + ',' + time;
                }
            });
        }

        return listVisitLogs;
    }

    @Override
    public Map<String, String> reduce(List<ComputeJobResult> results) {

        Map<String,Long> counter = new HashMap<>();
        for (ComputeJobResult res : results) {
            String[] grouped = res.getData().toString().split(",");
            counter.merge(grouped[0]+','+grouped[1], Long.valueOf(grouped[2]), (v1, v2) -> v1.equals(v2) ? v1 : v1 + v2);
        }

        Map<String,String> mapOut = new HashMap<>();
        for (String key: counter.keySet()) {
            Integer val = (Integer) Math.round(counter.get(key)/(1000 * 1000 * 60 * 60));
            mapOut.put(key,val.toString());
        }
//        System.out.println(">>> results on from compute job" + counter);
        return mapOut;
    }
}
