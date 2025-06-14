import java.util.ArrayList;
import java.util.List;

enum StoryStatus { REFINANDO, PRONTA_PARA_DEV, EM_TESTE, VALIDADA }

public class UserStory extends Artifact {
    private String title;
    private String description;
    private StoryStatus status;
    private List<String> acceptanceCriteria;

    public UserStory(DevUser author, String title, String description) {
        super(author);
        this.title = title;
        this.description = description;
        this.status = StoryStatus.REFINANDO;
        this.acceptanceCriteria = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public StoryStatus getStatus() { return status; }
    public void setStatus(StoryStatus status) { this.status = status; }

    public void addAcceptanceCriterion(String criterion) {
        this.acceptanceCriteria.add(criterion);
    }

    @Override
    public String getDetails() { /* ... como antes ... */ return "User Story: " + title; }

    @Override
    public String toString() {
        return this.getTitle() + " [" + this.getStatus() + "]";
    }
}