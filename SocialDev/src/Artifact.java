import java.util.Date;
import java.util.UUID;

public abstract class Artifact {
    private String id;
    private DevUser author;
    private Date creationDate;

    public Artifact(DevUser author) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.creationDate = new Date();
    }

    public DevUser getAuthor() { return author; }
    public abstract String getDetails();
}