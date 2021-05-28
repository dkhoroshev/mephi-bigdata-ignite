package bigdata.ignite.service;

import bigdata.ignite.controller.model.VisitLog;
import bigdata.ignite.dao.VisitLogServiceRepository;
import bigdata.ignite.model.VisitLogEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VisitLogBusinessLogicService {
    private VisitLogServiceRepository visitLogServiceRepository;

    public VisitLogBusinessLogicService(VisitLogServiceRepository visitLogServiceRepository){
        this.visitLogServiceRepository = visitLogServiceRepository;
    }

    public VisitLogEntity processCreate(VisitLog visitLog){
        VisitLogEntity visitLogEntity = new VisitLogEntity(visitLog.getCollage(),visitLog.getUid(),visitLog.getEventdate(),visitLog.getInout());
        visitLogServiceRepository.save(visitLogEntity);
        return visitLogEntity;
    }

    public VisitLogEntity processGet(String id) { return visitLogServiceRepository.get(UUID.fromString(id)); }

    public List<VisitLogEntity> processGetAll() { return visitLogServiceRepository.getAll(); }
}
