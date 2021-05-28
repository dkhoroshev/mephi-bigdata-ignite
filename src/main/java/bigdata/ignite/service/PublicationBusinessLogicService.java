package bigdata.ignite.service;

import bigdata.ignite.controller.model.Publication;
import bigdata.ignite.dao.PublicationServiceRepository;
import bigdata.ignite.model.PublicationEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PublicationBusinessLogicService {
    private PublicationServiceRepository publicationServiceRepository;

    public PublicationBusinessLogicService(PublicationServiceRepository publicationServiceRepository){
        this.publicationServiceRepository = publicationServiceRepository;
    }

    public PublicationEntity processCreate(Publication publication){
        PublicationEntity publicationEntity = new PublicationEntity(publication.getCollage(),publication.getUid(),publication.getPubdate(),publication.getPubname());
        publicationServiceRepository.save(publicationEntity);
        return publicationEntity;
    }

    public PublicationEntity processGet(String id) { return publicationServiceRepository.get(UUID.fromString(id)); }

    public List<PublicationEntity> processGetAll() { return publicationServiceRepository.getAll(); }
}
