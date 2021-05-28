package bigdata.ignite.controller;

import bigdata.ignite.controller.model.VisitLog;
import bigdata.ignite.model.VisitLogEntity;
import bigdata.ignite.service.VisitLogBusinessLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("visitlog")
public class VisitLogServiceController {

    @Autowired
    private VisitLogBusinessLogicService visitLogBusinessLogicService;

    public VisitLogServiceController(VisitLogBusinessLogicService visitLogBusinessLogicService) {
        this.visitLogBusinessLogicService = visitLogBusinessLogicService;
    }

    @PostMapping(path = {"/create"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VisitLogEntity> createPublication(@RequestBody VisitLog visitLog) {
        VisitLogEntity publicationEntity = visitLogBusinessLogicService.processCreate(visitLog);
        return new ResponseEntity<>(publicationEntity, HttpStatus.OK);
    }

    @GetMapping(path = {"/get/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VisitLogEntity> getPublication(@PathVariable String id) {
        VisitLogEntity publicationEntity = visitLogBusinessLogicService.processGet(id);
        return new ResponseEntity<>(publicationEntity, HttpStatus.OK);
    }

    @GetMapping(path = {"/get/all"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VisitLogEntity>> getAll() {
        List<VisitLogEntity> visitLogEntities = visitLogBusinessLogicService.processGetAll();
        return new ResponseEntity<>(visitLogEntities, HttpStatus.OK);
    }
}
