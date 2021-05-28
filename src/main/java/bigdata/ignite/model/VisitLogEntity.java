package bigdata.ignite.model;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
public class VisitLogEntity {

    private UUID id;
    private Integer collage;
    private Integer uid;
    private Date eventdate;
    private Integer inout;

    public VisitLogEntity() {}

    public VisitLogEntity(Integer collage, Integer uid, Date eventdate, Integer inout) {
        this.id = UUID.randomUUID();
        this.collage = collage;
        this.uid = uid;
        this.eventdate = eventdate;
        this.inout = inout;
    }
}
