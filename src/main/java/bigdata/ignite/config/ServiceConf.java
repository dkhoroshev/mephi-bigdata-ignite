package bigdata.ignite.config;

import bigdata.ignite.controller.PublicationServiceController;
import bigdata.ignite.controller.VisitLogServiceController;
import bigdata.ignite.dao.PublicationServiceRepository;
import bigdata.ignite.dao.VisitLogServiceRepository;
import bigdata.ignite.model.PublicationEntity;
import bigdata.ignite.model.VisitLogEntity;
import bigdata.ignite.service.PublicationBusinessLogicService;
import bigdata.ignite.service.VisitLogBusinessLogicService;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@Configuration
@Import(IgniteConf.class)
public class ServiceConf {
    @Bean
    PublicationServiceRepository publicationServiceRepository(Ignite ignite, CacheConfiguration<UUID, PublicationEntity> publicationCacheConf){
        return new PublicationServiceRepository(ignite, publicationCacheConf);
    }

    @Bean
    PublicationBusinessLogicService publicationBusinessLogicService(PublicationServiceRepository publicationServiceRepository){
        return new PublicationBusinessLogicService(publicationServiceRepository);
    }

    @Bean
    PublicationServiceController publicationServiceController(PublicationBusinessLogicService publicationBusinessLogicService){
        return new PublicationServiceController(publicationBusinessLogicService);
    }

    @Bean
    VisitLogServiceRepository visitLogServiceRepository(Ignite ignite, CacheConfiguration<UUID, VisitLogEntity> visitLogCacheConf){
        return new VisitLogServiceRepository(ignite, visitLogCacheConf);
    }

    @Bean
    VisitLogBusinessLogicService visitLogBusinessLogicService(VisitLogServiceRepository visitLogServiceRepository){
        return new VisitLogBusinessLogicService(visitLogServiceRepository);
    }

    @Bean
    VisitLogServiceController visitLogServiceController(VisitLogBusinessLogicService visitLogBusinessLogicService){
        return new VisitLogServiceController(visitLogBusinessLogicService);
    }
}
