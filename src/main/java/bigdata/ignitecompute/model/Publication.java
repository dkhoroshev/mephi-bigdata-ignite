package bigdata.ignitecompute.model;

import lombok.Data;

import java.util.Date;

@Data
public class Publication {
    private Integer collage;
    private Integer uid;
    private Date pubdate;
    private String pubname;

    public Publication() {}

    public Publication(Integer collage, Integer uid, Date pubdate, String pubname) {
        this.collage = collage;
        this.uid = uid;
        this.pubdate = pubdate;
        this.pubname = pubname;
    }

}
