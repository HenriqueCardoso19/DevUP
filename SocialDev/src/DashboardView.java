// DashboardView.java

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class DashboardView extends BorderPane {

    private GuiApplication app;
    private DevUser loggedInUser;
    private TextArea infoArea = new TextArea();

    public DashboardView(GuiApplication app, DevUser loggedInUser) {
        this.app = app;
        this.loggedInUser = loggedInUser;

        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));

        Text title = new Text("Painel de " + loggedInUser.getRole());
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Text userInfo = new Text("Usuário: " + loggedInUser.getName());
        Text userStats = new Text("Nível: " + loggedInUser.getLevel() + " | XP: " + loggedInUser.getXp());

        menu.getChildren().addAll(title, userInfo, userStats, new Separator());

        if (loggedInUser instanceof ProjManager) setupManagerMenu(menu);
        else if (loggedInUser instanceof ProjAnalyst) setupAnalystMenu(menu);
        else if (loggedInUser instanceof ProjDev) setupDevMenu(menu);
        else if (loggedInUser instanceof ProjTester) setupTesterMenu(menu);

        menu.getChildren().add(new Separator());
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> app.showLoginScreen());
        menu.getChildren().add(logoutButton);

        setLeft(menu);

        infoArea.setEditable(false);
        infoArea.setWrapText(true);
        infoArea.setText("Bem-vindo! Selecione uma opção no menu.");
        setCenter(infoArea);
    }

    // ========== MENUS ESPECÍFICOS POR CARGO ==========

    private void setupManagerMenu(VBox menu) {
        Button createProjectBtn = new Button("Criar Novo Projeto");
        createProjectBtn.setOnAction(e -> handleCreateProject());
        Button addMemberBtn = new Button("Adicionar Membro");
        addMemberBtn.setOnAction(e -> handleAddMember());
        Button removeMemberBtn = new Button("Remover Membro");
        removeMemberBtn.setOnAction(e -> handleRemoveMember());
        Button createMissionBtn = new Button("Criar Nova Missão");
        createMissionBtn.setOnAction(e -> handleCreateMission());
        Button viewTeamBtn = new Button("Ver Equipe do Projeto");
        viewTeamBtn.setOnAction(e -> handleViewTeam());
        menu.getChildren().addAll(createProjectBtn, addMemberBtn, removeMemberBtn, createMissionBtn, viewTeamBtn);
    }

    private void setupAnalystMenu(VBox menu) {
        Button createStoryBtn = new Button("Criar User Story");
        createStoryBtn.setOnAction(e -> handleCreateUserStory());
        Button updateStoryBtn = new Button("Atualizar User Story");
        updateStoryBtn.setOnAction(e -> handleUpdateUserStory());
        menu.getChildren().addAll(createStoryBtn, updateStoryBtn);
    }

    private void setupDevMenu(VBox menu) {
        Button viewMissionsBtn = new Button("Ver e Trabalhar em Missões");
        viewMissionsBtn.setOnAction(e -> handleDevViewMissions());
        menu.getChildren().add(viewMissionsBtn);
    }

    private void setupTesterMenu(VBox menu) {
        Button reportBugBtn = new Button("Reportar Bug");
        reportBugBtn.setOnAction(e -> handleReportBug());
        Button validateStoryBtn = new Button("Validar User Story");
        validateStoryBtn.setOnAction(e -> handleValidateStory());
        menu.getChildren().addAll(reportBugBtn, validateStoryBtn);
    }

    // ========== LÓGICA DAS AÇÕES DOS BOTÕES (HANDLERS) ==========

    private boolean checkProject() {
        if (loggedInUser.getProject() == null) {
            infoArea.setText("ERRO: Você precisa estar em um projeto para realizar esta ação.");
            return false;
        }
        return true;
    }

    // --- Ações do Manager ---
    private void handleCreateProject() {
        TextInputDialog dialog = new TextInputDialog("Meu Projeto");
        dialog.setTitle("Criação de Projeto");
        dialog.setHeaderText("Digite o nome para o novo projeto:");
        dialog.setContentText("Nome:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            Project newProject = app.handleCreateProject((ProjManager) loggedInUser, name);
            infoArea.setText("Projeto '" + name + "' criado com sucesso!");
        });
    }

    private void handleAddMember() {
        Project project = loggedInUser.getProject();
        if (project == null) { infoArea.setText("ERRO: Você precisa estar em um projeto."); return; }
        List<DevUser> available = app.getAvailableUsers();
        if (available.isEmpty()) { infoArea.setText("Nenhum usuário disponível."); return; }

        ChoiceDialog<DevUser> dialog = new ChoiceDialog<>(available.get(0), available);
        dialog.setTitle("Adicionar Membro");
        dialog.setHeaderText("Escolha um usuário para adicionar ao projeto:");
        Optional<DevUser> result = dialog.showAndWait();
        result.ifPresent(user -> {
            app.handleAddMember((ProjManager) loggedInUser, user, project);
            infoArea.setText("Usuário '" + user.getName() + "' adicionado.");
        });
    }

    private void handleRemoveMember() {
        Project project = loggedInUser.getProject();
        if (project == null) { infoArea.setText("ERRO: Você não está em um projeto."); return; }
        List<DevUser> removable = project.getTeamMembers().stream().filter(m -> !m.equals(loggedInUser)).collect(Collectors.toList());
        if (removable.isEmpty()) { infoArea.setText("Não há membros para remover."); return; }

        ChoiceDialog<DevUser> dialog = new ChoiceDialog<>(removable.get(0), removable);
        dialog.setTitle("Remover Membro");
        dialog.setHeaderText("Escolha um membro para remover:");

        Optional<DevUser> result = dialog.showAndWait();
        result.ifPresent(userToRemove -> {
            app.handleRemoveMember((ProjManager) loggedInUser, userToRemove, project);
            infoArea.setText("Usuário '" + userToRemove.getName() + "' removido.");
        });
    }

    private void handleCreateMission() {
        if (!checkProject()) return;
        Dialog<Mission> dialog = new CreateMissionDialog();
        Optional<Mission> result = dialog.showAndWait();
        result.ifPresent(mission -> {
            app.handleCreateMission(loggedInUser.getProject(), mission);
            infoArea.setText("Missão '" + mission.getGoalName() + "' criada.");
        });
    }

    private void handleViewTeam() {
        if (!checkProject()) return;
        StringBuilder teamInfo = new StringBuilder("Equipe do Projeto: " + loggedInUser.getProject().getProjName() + "\n\n");
        for (DevUser member : loggedInUser.getProject().getTeamMembers()) {
            teamInfo.append("- ").append(member.toString()).append("\n");
        }
        infoArea.setText(teamInfo.toString());
    }

    // --- Ações do Analyst ---
    private void handleCreateUserStory() {
        if (!checkProject()) return;
        Dialog<UserStory> dialog = new CreateUserStoryDialog((ProjAnalyst) loggedInUser);
        Optional<UserStory> result = dialog.showAndWait();
        result.ifPresent(story -> {
            app.handleCreateUserStory(loggedInUser.getProject(), story);
            infoArea.setText("User Story '" + story.getTitle() + "' criada.");
        });
    }

    private void handleUpdateUserStory() {
        if (!checkProject()) return;
        List<UserStory> stories = app.getStoriesFromProject(loggedInUser.getProject());
        if (stories.isEmpty()) { infoArea.setText("Nenhuma User Story para atualizar."); return; }

        ChoiceDialog<UserStory> dialog = new ChoiceDialog<>(stories.get(0), stories);
        dialog.setTitle("Atualizar User Story");
        dialog.setHeaderText("Escolha a User Story para editar:");
        Optional<UserStory> result = dialog.showAndWait();

        result.ifPresent(storyToUpdate -> {
            Dialog<String> editDialog = new UpdateUserStoryDialog(storyToUpdate);
            editDialog.showAndWait();
            infoArea.setText("User Story '" + storyToUpdate.getTitle() + "' atualizada.");
        });
    }

    // --- Ações do Dev ---
    private void handleDevViewMissions() {
        if (!checkProject()) return;
        Project project = loggedInUser.getProject();
        List<Mission> missions = project.getMissions();
        if (missions.isEmpty()) {
            infoArea.setText("Nenhuma missão disponível no projeto.");
            return;
        }

        ChoiceDialog<Mission> dialog = new ChoiceDialog<>(missions.get(0), missions);
        dialog.setTitle("Missões do Projeto");
        dialog.setHeaderText("Escolha uma missão para trabalhar:");
        Optional<Mission> result = dialog.showAndWait();

        result.ifPresent(mission -> {
            Dialog<Void> workDialog = new MissionWorkDialog((ProjDev) loggedInUser, mission, app);
            workDialog.showAndWait();
            Text userStats = (Text) ((VBox)getLeft()).getChildren().get(2);
            userStats.setText("Nível: " + loggedInUser.getLevel() + " | XP: " + loggedInUser.getXp());
            infoArea.setText("Trabalho na missão '" + mission.getGoalName() + "' finalizado.");
        });
    }

    // --- Ações do Tester ---
    private void handleReportBug() {
        if (!checkProject()) return;
        Dialog<BugReport> dialog = new CreateBugReportDialog((ProjTester) loggedInUser);
        Optional<BugReport> result = dialog.showAndWait();
        result.ifPresent(bug -> {
            app.handleReportBug(loggedInUser.getProject(), bug);
            infoArea.setText("Bug '" + bug.getTitle() + "' reportado com sucesso.");
        });
    }

    private void handleValidateStory() {
        if (!checkProject()) return;
        List<UserStory> stories = app.getStoriesFromProject(loggedInUser.getProject());
        if (stories.isEmpty()) { infoArea.setText("Nenhuma User Story para validar."); return; }

        ChoiceDialog<UserStory> dialog = new ChoiceDialog<>(stories.get(0), stories);
        dialog.setTitle("Validar User Story");
        dialog.setHeaderText("Escolha a User Story para marcar como validada:");
        Optional<UserStory> result = dialog.showAndWait();

        result.ifPresent(story -> {
            app.handleValidateStory((ProjTester) loggedInUser, story);
            infoArea.setText("User Story '" + story.getTitle() + "' foi VALIDADA!");
        });
    }
}

// ========== CLASSES DE DIÁLOGO CUSTOMIZADAS ==========
class CreateMissionDialog extends Dialog<Mission> {
    public CreateMissionDialog() {
        setTitle("Nova Missão");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Nome da Missão");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Descrição");
        TextField xpField = new TextField();
        xpField.setPromptText("XP");
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrição:"), 0, 1);
        grid.add(descArea, 1, 1);
        grid.add(new Label("XP:"), 0, 2);
        grid.add(xpField, 1, 2);
        getDialogPane().setContent(grid);
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Mission(nameField.getText(), descArea.getText(), new Date(), Integer.parseInt(xpField.getText()));
            }
            return null;
        });
    }
}

class CreateUserStoryDialog extends Dialog<UserStory> {
    public CreateUserStoryDialog(DevUser author) {
        setTitle("Nova User Story");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField titleField = new TextField();
        titleField.setPromptText("Como um..., eu quero..., para que...");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Detalhes e notas");
        grid.add(new Label("Título:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Descrição:"), 0, 1);
        grid.add(descArea, 1, 1);
        getDialogPane().setContent(grid);
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new UserStory(author, titleField.getText(), descArea.getText());
            }
            return null;
        });
    }
}

class UpdateUserStoryDialog extends Dialog<String> {
    public UpdateUserStoryDialog(UserStory story) {
        setTitle("Editando: " + story.getTitle());
        getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        VBox vbox = new VBox(10);
        TextField newCriterionField = new TextField();
        Button addCriterionBtn = new Button("Adicionar Critério");
        addCriterionBtn.setOnAction(e -> {
            String newCriterion = newCriterionField.getText();
            if(newCriterion != null && !newCriterion.isEmpty()){
                story.addAcceptanceCriterion(newCriterion);
                newCriterionField.clear();
            }
        });
        vbox.getChildren().addAll(
                new Label("Adicionar novo critério de aceite:"),
                newCriterionField,
                addCriterionBtn
        );
        getDialogPane().setContent(vbox);
    }
}

class CreateBugReportDialog extends Dialog<BugReport> {
    public CreateBugReportDialog(DevUser author) {
        setTitle("Reportar Novo Bug");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField titleField = new TextField();
        titleField.setPromptText("Ex: Botão de login não funciona");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Passos para reproduzir o erro...");
        ComboBox<String> severityCombo = new ComboBox<>();
        severityCombo.getItems().addAll("Baixa", "Média", "Crítica");
        severityCombo.setValue("Média");
        grid.add(new Label("Título:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Descrição:"), 0, 1);
        grid.add(descArea, 1, 1);
        grid.add(new Label("Severidade:"), 0, 2);
        grid.add(severityCombo, 1, 2);
        getDialogPane().setContent(grid);
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new BugReport(author, titleField.getText(), descArea.getText(), severityCombo.getValue());
            }
            return null;
        });
    }
}

class MissionWorkDialog extends Dialog<Void> {
    public MissionWorkDialog(ProjDev dev, Mission mission, GuiApplication app) {
        setTitle("Trabalhando em: " + mission.getGoalName());
        setHeaderText("Status: " + mission.getStatus() + " | Recompensa: " + mission.getGoalReward() + " XP");
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        GridPane codeGrid = new GridPane();
        codeGrid.setHgap(10);
        TextField hashField = new TextField();
        hashField.setPromptText("a1b2c3d4");
        Button submitCodeBtn = new Button("Lançar Código");
        codeGrid.add(new Label("Commit Hash:"), 0, 0);
        codeGrid.add(hashField, 1, 0);
        codeGrid.add(submitCodeBtn, 2, 0);
        submitCodeBtn.setOnAction(e -> {
            app.handleDevSubmitCode(dev, hashField.getText(), "main");
            hashField.clear();
        });
        TextArea reportArea = new TextArea();
        reportArea.setPromptText("Descreva o que foi feito...");
        Button submitReportBtn = new Button("Submeter Relatório");
        submitReportBtn.setOnAction(e -> {
            app.handleDevSubmitReport(dev, mission, reportArea.getText());
            reportArea.clear();
        });
        Button completeMissionBtn = new Button("Marcar Missão como Concluída");
        completeMissionBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        completeMissionBtn.setOnAction(e -> {
            app.handleDevCompleteMission(dev, mission);
            getDialogPane().getScene().getWindow().hide();
        });
        if (mission.getStatus() == MissionStatus.CONCLUIDA) {
            completeMissionBtn.setDisable(true);
        }
        content.getChildren().addAll(codeGrid, new Separator(), reportArea, submitReportBtn, new Separator(), completeMissionBtn);
        getDialogPane().setContent(content);
    }
}
