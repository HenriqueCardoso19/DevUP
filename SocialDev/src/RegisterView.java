// RegisterView.java

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * Constrói e controla a interface da tela de registro de novos usuários.
 */
public class RegisterView extends GridPane {

    private GuiApplication app;

    public RegisterView(GuiApplication app) {
        this.app = app;

        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        // Componentes da tela de registro
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Manager", "Analyst", "Dev", "Tester");
        roleComboBox.setValue("Dev"); // Valor padrão

        Button registerButton = new Button("Confirmar Registro");
        Button backButton = new Button("Voltar para Login");

        // Adiciona os componentes ao layout
        add(new Label("Nome Completo:"), 0, 0);
        add(nameField, 1, 0);
        add(new Label("Email:"), 0, 1);
        add(emailField, 1, 1);
        add(new Label("Senha:"), 0, 2);
        add(passwordField, 1, 2);
        add(new Label("Cargo:"), 0, 3);
        add(roleComboBox, 1, 3);
        add(registerButton, 1, 4);
        add(backButton, 0, 4);

        // Ações dos botões
        registerButton.setOnAction(event -> {
            boolean success = app.handleRegister(
                    nameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    roleComboBox.getValue()
            );
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário registrado! Agora você pode fazer o login.");
                app.showLoginScreen();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Este email já está em uso ou os campos estão vazios.");
            }
        });

        backButton.setOnAction(event -> app.showLoginScreen());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
