import java.util.Date;

public abstract class DevUser {
    private String name;
    private String email;
    private String password;
    private String role;
    private Project project;
    private int xp;
    private int level;

    public DevUser(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.project = null;
        this.xp = 0;
        this.level = 1;
    }

    // --- Getters e Setters ---
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public int getXp() { return xp; }
    public int getLevel() { return level; }

    // --- Lógica de Gamificação ---
    public int getXpToNextLevel() { return 100 * this.level; }
    public void addXp(int amount) {
        this.xp += amount;
        System.out.println("XP adicionado: " + amount + " para " + this.name);
        while (this.xp >= getXpToNextLevel()) {
            this.xp -= getXpToNextLevel();
            this.level++;
            System.out.println("LEVEL UP! " + this.name + " alcançou o Nível " + this.level);
        }
    }

    // --- Lógica de Ações ---
    public void createAndSubmitReport(Mission mission, String content) {
        if (this.project != null) {
            project.addArtifact(new Report(this, mission, content));
        }
    }
    public void createAndSubmitCodeCommit(String hash, String branch) {
        if (this.project != null) {
            project.addArtifact(new CodeCommit(this, hash, branch));
        }
    }

    @Override
    public String toString() {
        return this.getName() + " (" + this.getRole() + ")";
    }
}