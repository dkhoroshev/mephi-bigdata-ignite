package bigdata.ignite.dao;

import bigdata.ignite.model.VisitLogEntity;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class VisitLogServiceRepository {
    private Ignite ignite;
    CacheConfiguration<UUID, VisitLogEntity> visitLogEntityCacheConfiguration;

    public VisitLogServiceRepository(Ignite ignite, CacheConfiguration<UUID, VisitLogEntity> visitLogEntityCacheConfiguration){
        this.ignite = ignite;
        this.visitLogEntityCacheConfiguration = visitLogEntityCacheConfiguration;
    }

    public void save(VisitLogEntity visitLogEntity){
        ignite.getOrCreateCache(visitLogEntityCacheConfiguration).put(visitLogEntity.getId(), visitLogEntity);
    }

    public VisitLogEntity get(UUID id) { return  ignite.getOrCreateCache(visitLogEntityCacheConfiguration).get(id); }

    public List<VisitLogEntity> getAll(){
        Iterable<Cache.Entry<UUID,VisitLogEntity>> iterable = () -> ignite.getOrCreateCache(visitLogEntityCacheConfiguration).iterator();

        List<VisitLogEntity> visitLogs = StreamSupport
                .stream(iterable.spliterator(), false)
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList());

        return visitLogs;
    }
}
