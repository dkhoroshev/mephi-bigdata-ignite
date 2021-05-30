package bigdata.ignitecompute.model;

import lombok.Data;

import java.util.Date;

@Data
public class VisitLog {

    private Integer collage;
    private Integer uid;
    private Date eventdate;
    private Boolean inout;

    public VisitLog() {}

    public VisitLog(Integer collage, Integer uid, Date eventdate, Boolean inout) {
        this.collage = collage;
        this.uid = uid;
        this.eventdate = eventdate;
        this.inout = inout;
    }
}
