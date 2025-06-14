// GuiApplication.java

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A classe principal que orquestra toda a aplicação gráfica.
 */
public class GuiApplication extends Application {

    private Stage primaryStage;
    private static Map<String, DevUser> userDatabase = new HashMap<>();
    private static Map<String, Project> projectDatabase = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistema de Projetos Gamificado");
        showLoginScreen();
    }

    // ========== MÉTODOS PARA TROCAR DE TELA ==========
    public void showLoginScreen() {
        LoginView loginView = new LoginView(this);
        Scene scene = new Scene(loginView, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showRegisterScreen() {
        RegisterView registerView = new RegisterView(this);
        Scene scene = new Scene(registerView, 400, 300);
        primaryStage.setScene(scene);
    }

    public void showDashboard(DevUser user) {
        DashboardView dashboardView = new DashboardView(this, user);
        Scene scene = new Scene(dashboardView, 800, 600);
        primaryStage.setScene(scene);
    }

    // ========== MÉTODOS DE LÓGICA (HANDLERS) ==========
    public DevUser handleLogin(String email, String password) {
        DevUser user = userDatabase.get(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean handleRegister(String name, String email, String password, String role) {
        if (userDatabase.containsKey(email) || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return false;
        }
        DevUser newUser = null;
        switch (role) {
            case "Manager": newUser = new ProjManager(name, email, password); break;
            case "Analyst": newUser = new ProjAnalyst(name, email, password); break;
            case "Dev": newUser = new ProjDev(name, email, password); break;
            case "Tester": newUser = new ProjTester(name, email, password); break;
        }
        if (newUser != null) {
            userDatabase.put(email, newUser);
            return true;
        }
        return false;
    }

    // --- Handlers do Manager ---
    public Project handleCreateProject(ProjManager manager, String projName) {
        Project newProject = manager.createProject(projName, "Descrição Padrão", new Date());
        projectDatabase.put(projName, newProject);
        manager.addMemberToProject(manager, newProject);
        return newProject;
    }
    public void handleAddMember(ProjManager manager, DevUser user, Project project) {
        manager.addMemberToProject(user, project);
    }
    public void handleRemoveMember(ProjManager manager, DevUser user, Project project) {
        manager.removeMemberFromProject(user, project);
    }
    public void handleCreateMission(Project project, Mission mission) {
        project.addMission(mission);
    }

    // --- Handlers do Analyst ---
    public void handleCreateUserStory(Project project, UserStory story) {
        project.addArtifact(story);
        System.out.println("User Story '" + story.getTitle() + "' criada no projeto '" + project.getProjName() + "'.");
    }

    // --- Handlers do Tester ---
    public void handleReportBug(Project project, BugReport bug) {
        project.addArtifact(bug);
        System.out.println("Bug '" + bug.getTitle() + "' reportado no projeto '" + project.getProjName() + "'.");
    }

    public void handleValidateStory(ProjTester tester, UserStory story) {
        tester.validateStory(story);
        // Opcional: Adicionar XP ao tester por validar uma história
        tester.addXp(10); // Pequena recompensa
    }

    // --- Handlers do Dev (CORRIGIDOS E COMPLETOS) ---
    public void handleDevSubmitCode(ProjDev dev, String hash, String branch) {
        dev.createAndSubmitCodeCommit(hash, branch);
        System.out.println("Dev " + dev.getName() + " submeteu o commit " + hash);
    }

    public void handleDevSubmitReport(ProjDev dev, Mission mission, String content) {
        dev.createAndSubmitReport(mission, content);
        System.out.println("Dev " + dev.getName() + " submeteu um relatório para a missão " + mission.getGoalName());
    }

    public void handleDevCompleteMission(ProjDev dev, Mission mission) {
        if (mission.getStatus() != MissionStatus.CONCLUIDA) {
            mission.setStatus(MissionStatus.CONCLUIDA);
            dev.addXp(mission.getGoalReward());
            System.out.println("Dev " + dev.getName() + " completou a missão " + mission.getGoalName() + " e ganhou " + mission.getGoalReward() + " XP.");
        } else {
            System.out.println("Missão já estava concluída.");
        }
    }

    // --- Métodos de Apoio (CORRIGIDOS E COMPLETOS) ---
    public List<DevUser> getAvailableUsers() {
        return userDatabase.values().stream()
                .filter(user -> user.getProject() == null)
                .collect(Collectors.toList());
    }

    public List<UserStory> getStoriesFromProject(Project project) {
        if (project == null || project.getArtifacts() == null) {
            return new java.util.ArrayList<>(); // Retorna lista vazia se não houver projeto ou artefatos
        }
        return project.getArtifacts().stream()
                .filter(artifact -> artifact instanceof UserStory)
                .map(artifact -> (UserStory) artifact)
                .collect(Collectors.toList());
    }
}
