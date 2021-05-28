package bigdata.ignite.model;

import java.util.Date;
import java.util.UUID;

public class VisitLogEntity {

    private UUID id;
    private Integer collage;
    private Integer uid;
    private Date eventdate;
    private Integer inout;

    public VisitLogEntity(Integer collage, Integer uid, Date eventdate, Integer inout) {
        this.id = UUID.randomUUID();
        this.collage = collage;
        this.uid = uid;
        this.eventdate = eventdate;
        this.inout = inout;
    }

    public UUID getId() {
        return this.id;
    }

    public Integer getCollage() {
        return this.collage;
    }

    public Integer getUid() {
        return this.uid;
    }

    public Date getEventdate() {
        return this.eventdate;
    }

    public Integer getInout() {
        return this.inout;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCollage(Integer collage) {
        this.collage = collage;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public void setEventdate(Date eventdate) {
        this.eventdate = eventdate;
    }

    public void setInout(Integer inout) {
        this.inout = inout;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof VisitLogEntity)) return false;
        final VisitLogEntity other = (VisitLogEntity) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$collage = this.getCollage();
        final Object other$collage = other.getCollage();
        if (this$collage == null ? other$collage != null : !this$collage.equals(other$collage)) return false;
        final Object this$uid = this.getUid();
        final Object other$uid = other.getUid();
        if (this$uid == null ? other$uid != null : !this$uid.equals(other$uid)) return false;
        final Object this$eventdate = this.getEventdate();
        final Object other$eventdate = other.getEventdate();
        if (this$eventdate == null ? other$eventdate != null : !this$eventdate.equals(other$eventdate)) return false;
        final Object this$inout = this.getInout();
        final Object other$inout = other.getInout();
        if (this$inout == null ? other$inout != null : !this$inout.equals(other$inout)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof VisitLogEntity;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $collage = this.getCollage();
        result = result * PRIME + ($collage == null ? 43 : $collage.hashCode());
        final Object $uid = this.getUid();
        result = result * PRIME + ($uid == null ? 43 : $uid.hashCode());
        final Object $eventdate = this.getEventdate();
        result = result * PRIME + ($eventdate == null ? 43 : $eventdate.hashCode());
        final Object $inout = this.getInout();
        result = result * PRIME + ($inout == null ? 43 : $inout.hashCode());
        return result;
    }

    public String toString() {
        return "VisitLogEntity(id=" + this.getId() + ", collage=" + this.getCollage() + ", uid=" + this.getUid() + ", eventdate=" + this.getEventdate() + ", inout=" + this.getInout() + ")";
    }
}
