package bigdata.ignite.controller.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class VisitLog {

    private Integer collage;
    private Integer uid;
    private Date eventdate;
    private Integer inout;

    public VisitLog() {}

    public VisitLog(Integer collage, Integer uid, Date eventdate, Integer inout) {
        this.collage = collage;
        this.uid = uid;
        this.eventdate = eventdate;
        this.inout = inout;
    }
}
