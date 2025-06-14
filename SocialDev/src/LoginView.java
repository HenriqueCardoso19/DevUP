// LoginView.java

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Constrói e controla a interface da tela de login.
 * Estende GridPane para organizar os elementos em uma grade.
 */
public class LoginView extends GridPane {

    private GuiApplication app;

    public LoginView(GuiApplication app) {
        this.app = app;

        // Configuração do layout da grade
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        // Criação dos componentes visuais
        Text sceneTitle = new Text("Bem-vindo");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        add(sceneTitle, 0, 0, 2, 1);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label passwordLabel = new Label("Senha:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Não tem conta? Registre-se");

        // Adiciona os componentes à grade
        add(emailLabel, 0, 1);
        add(emailField, 1, 1);
        add(passwordLabel, 0, 2);
        add(passwordField, 1, 2);
        add(loginButton, 1, 3);
        add(registerButton, 1, 4);

        // Define as ações (eventos) dos botões
        loginButton.setOnAction(event -> {
            // Chama a lógica de login na classe principal
            DevUser user = app.handleLogin(emailField.getText(), passwordField.getText());
            if (user != null) {
                // Se o login for bem-sucedido, mostra o painel principal
                app.showDashboard(user);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Falhou", "Email ou senha incorretos.");
            }
        });

        registerButton.setOnAction(event -> {
            // Troca a tela atual pela tela de registro
            app.showRegisterScreen();
        });
    }

    // Método auxiliar para exibir janelas de alerta
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
