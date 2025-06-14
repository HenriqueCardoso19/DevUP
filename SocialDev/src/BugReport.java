public class BugReport extends Artifact {
    private String title;
    private String description;
    private String severity;

    public BugReport(DevUser author, String title, String description, String severity) {
        super(author);
        this.title = title;
        this.description = description;
        this.severity = severity;
    }

    public String getTitle() { return title; }

    @Override
    public String getDetails() {
        return "Relatório de Bug: " + title + " [Severidade: " + severity + "]\nAutor: " + getAuthor().getName();
    }
}