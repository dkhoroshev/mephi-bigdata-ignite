package bigdata.ignite.controller;

import bigdata.ignite.controller.model.Publication;
import bigdata.ignite.model.PublicationEntity;
import bigdata.ignite.service.PublicationBusinessLogicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("publication")
public class PublicationServiceController {

    @Autowired
    private PublicationBusinessLogicService publicationBusinessLogicService;

    public PublicationServiceController(PublicationBusinessLogicService publicationBusinessLogicService) {
        this.publicationBusinessLogicService = publicationBusinessLogicService;
    }

//    @PostMapping(path = {"/create"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<PublicationEntity> createPublication(@RequestBody Publication publication) {
//        PublicationEntity publicationEntity = publicationBusinessLogicService.processCreate(publication);
//        return new ResponseEntity<>(publicationEntity, HttpStatus.OK);
//    }
    @PostMapping("/create")
    public ResponseEntity<PublicationEntity> createPublication(@RequestBody Publication publication) {
        PublicationEntity publicationEntity = publicationBusinessLogicService.processCreate(publication);
        return new ResponseEntity<>(publicationEntity, HttpStatus.OK);
    }

    @GetMapping(path = {"/get/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicationEntity> getPublication(@PathVariable String id) {
        PublicationEntity publicationEntity = publicationBusinessLogicService.processGet(id);
        return new ResponseEntity<>(publicationEntity, HttpStatus.OK);
    }

    @GetMapping(path = {"/get/all"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PublicationEntity>> getAll() {
        List<PublicationEntity> personEntities = publicationBusinessLogicService.processGetAll();
        return new ResponseEntity<>(personEntities, HttpStatus.OK);
    }
}
