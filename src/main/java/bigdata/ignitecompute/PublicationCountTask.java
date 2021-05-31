package bigdata.ignitecompute;

import bigdata.ignitecompute.model.Publication;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.*;
import org.apache.ignite.compute.*;
import org.apache.ignite.resources.IgniteInstanceResource;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class PublicationCountTask extends ComputeTaskSplitAdapter<String, Map> {
    @IgniteInstanceResource
    Ignite ignite;

    @Override
    public List<ComputeJob> split(int gridSize, String arg) {
        String[] count = arg.split(",");
        // Set date format
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy");
        log.info("Создаем таблицф для публикаций");
        IgniteCache<Integer, Publication> publicationIgniteCache = ignite.getOrCreateCache("publication");

        List<ComputeJob> listPublications = new ArrayList<>(count.length);

        int sizePub = publicationIgniteCache.size();
        for (int i =1; i < sizePub;i++) {
            Publication publicationCacheString = publicationIgniteCache.get(i);
            listPublications.add(new ComputeJobAdapter() {
                @Override
                public Object execute() throws IgniteException {
                    String uid = publicationCacheString.getUid().toString();
                    String date = DateFormat.format(publicationCacheString.getPubdate());
                    return date + ',' + uid;
                }
            });
        }

        return listPublications;
    }

    @Override
    public Map<String, String> reduce(List<ComputeJobResult> results) {

        Map<String,Integer> counter = new HashMap<>();
        for (ComputeJobResult res : results) {
            int grouped = counter.getOrDefault(res.getData(),0) +1;
            counter.put(res.getData(),grouped);
        }

        Map<String,String> mapOut = new HashMap<String, String>();
        for (String key: counter.keySet()) {
            mapOut.put(key,"Публикаций:" + counter.get(key).toString());
        }

//        System.out.println(">>> results on from compute job" + mapOut);
        return mapOut;
    }
}
