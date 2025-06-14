import java.util.Date;

enum MissionStatus { PENDENTE, EM_ANDAMENTO, CONCLUIDA }

public class Mission {
    private String goalName;
    private String goalDescription;
    private Date goalTime;
    private int goalReward;
    private MissionStatus status;

    public Mission(String goalName, String goalDescription, Date goalTime, int goalReward) {
        this.goalName = goalName;
        this.goalDescription = goalDescription;
        this.goalTime = goalTime;
        this.goalReward = goalReward;
        this.status = MissionStatus.PENDENTE;
    }

    public String getGoalName() { return goalName; }
    public int getGoalReward() { return goalReward; }
    public MissionStatus getStatus() { return status; }
    public void setStatus(MissionStatus status) { this.status = status; }

    @Override
    public String toString() {
        return this.getGoalName() + " [" + this.getStatus() + "]";
    }
}