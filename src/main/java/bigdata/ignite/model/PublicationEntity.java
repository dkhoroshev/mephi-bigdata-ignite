package bigdata.ignite.model;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
public class PublicationEntity {
    private UUID id;
    private String collage;
    private String uid;
    private String pubdate;
    private String pubname;

    public PublicationEntity(String collage, String uid, String pubdate, String pubname) {
        this.id = UUID.randomUUID();
        this.collage = collage;
        this.uid = uid;
        this.pubdate = pubdate;
        this.pubname = pubname;
        System.out.println(collage + ',' + uid + ',' + pubdate + ',' + pubname);
    }

}
