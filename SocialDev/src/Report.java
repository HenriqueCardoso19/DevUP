public class Report extends Artifact {
    private String content;
    private Mission relatedMission;

    public Report(DevUser author, Mission relatedMission, String content) {
        super(author);
        this.relatedMission = relatedMission;
        this.content = content;
    }

    @Override
    public String getDetails() {
        return "Relatório da Missão: '" + relatedMission.getGoalName() + "'\nAutor: " + getAuthor().getName() + "\nConteúdo: " + content;
    }
}