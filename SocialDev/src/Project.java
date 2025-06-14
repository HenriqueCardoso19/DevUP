import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
    private String projName;
    private List<DevUser> teamMembers;
    private List<Mission> missions;
    private List<Artifact> artifacts;

    public Project(String projName, String projDescription, Date projTime) {
        this.projName = projName;
        this.teamMembers = new ArrayList<>();
        this.missions = new ArrayList<>();
        this.artifacts = new ArrayList<>();
    }

    public String getProjName() { return projName; }
    public List<DevUser> getTeamMembers() { return teamMembers; }
    public List<Mission> getMissions() { return missions; }
    public List<Artifact> getArtifacts() { return artifacts; }

    public void addMission(Mission mission) {
        this.missions.add(mission);
        System.out.println("Missão '" + mission.getGoalName() + "' adicionada ao projeto.");
    }

    public void addArtifact(Artifact artifact) {
        this.artifacts.add(artifact);
        System.out.println("Artefato '" + artifact.getClass().getSimpleName() + "' adicionado ao projeto.");
    }
}