package bigdata.ignite.dao;

import bigdata.ignite.model.PublicationEntity;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PublicationServiceRepository {
    private Ignite ignite;
    CacheConfiguration<UUID, PublicationEntity> publicationEntityCacheConfiguration;

    public PublicationServiceRepository(Ignite ignite, CacheConfiguration<UUID, PublicationEntity> publicationEntityCacheConfiguration){
        this.ignite = ignite;
        this.publicationEntityCacheConfiguration = publicationEntityCacheConfiguration;
    }

    public void save(PublicationEntity publicationEntity){
        ignite.getOrCreateCache(publicationEntityCacheConfiguration).put(publicationEntity.getId(), publicationEntity);
    }

    public PublicationEntity get(UUID id) { return  ignite.getOrCreateCache(publicationEntityCacheConfiguration).get(id); }

    public List<PublicationEntity> getAll(){
        Iterable<Cache.Entry<UUID,PublicationEntity>> iterable = () -> ignite.getOrCreateCache(publicationEntityCacheConfiguration).iterator();

        List<PublicationEntity> publications = StreamSupport
                .stream(iterable.spliterator(), false)
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList());

        return publications;
    }
}
