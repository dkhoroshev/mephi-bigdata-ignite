package bigdata.ignite.controller.model;

import lombok.*;

import java.util.Date;

@Data
public class Publication {
    private String collage;
    private String uid;
    private String pubdate;
    private String pubname;

    public Publication() {}

    public Publication(String collage, String uid, String pubdate, String pubname) {
        this.collage = collage;
        this.uid = uid;
        this.pubdate = pubdate;
        this.pubname = pubname;
        System.out.println(collage + ',' + uid + ',' + pubdate + ',' + pubname);
    }

}
