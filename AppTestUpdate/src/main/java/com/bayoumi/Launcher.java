package com.bayoumi;

import com.bayoumi.util.Constants;
import com.bayoumi.util.Logger;
import com.bayoumi.util.Utility;
import com.bayoumi.util.update.UpdateHandler;
import com.install4j.api.launcher.StartupNotification;
import com.install4j.api.launcher.Variables;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        Utility.createDirectory(Constants.assetsPath + "/logs");
        Logger.init();
    }

    private String getVersionOfInstaller() {
        try {
            return Variables.getCompilerVariable("sys.version");
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private Scene createScene() {
        final String versionOfInstaller = getVersionOfInstaller();
        final String versionOfJar = Constants.VERSION;
        VBox root = new VBox(10);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        Label label = new Label("Version of Installer: " + versionOfInstaller + "\nVersion of Jar: " + versionOfJar);
        label.setStyle("-fx-font-size: 20px;fx-font-weight: bold;");


        HBox hBox = new HBox(10);
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        Button checkForUpdateButton = new Button("Check for Update");
        checkForUpdateButton.setOnAction(e -> checkForUpdate());

        hBox.getChildren().add(checkForUpdateButton);


        root.getChildren().addAll(label, hBox);
        return new Scene(root, 500, 400);
    }

    private void checkForUpdate() {
        new Thread(() -> {
            switch (UpdateHandler.getInstance().checkUpdate()) {
                case 0:
                    Logger.info(getClass().getName() + ".checkForUpdate(): " + "No Update Found");
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("No Update Found");
                        alert.setHeaderText(null);
                        alert.setContentText("No Update Found");
                        alert.showAndWait();
                    });
                    break;
                case 1:
                    UpdateHandler.getInstance().showInstallPrompt();
                    break;
                case -1:
                    Logger.info(getClass().getName() + ".checkForUpdate(): " + "error => only installers and single bundle archives on macOS are supported for background updates");
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Only installers and single bundle archives on macOS are supported for background updates");
                        alert.showAndWait();
                    });
                    break;
            }
        }).start();
    }

    public void start(Stage stage) {
        stage.setScene(createScene());
        stage.setTitle(Constants.APP_NAME);
        stage.getIcons().add(new Image("/com/bayoumi/images/icon.png"));
        stage.show();
        StartupNotification.registerStartupListener(s -> stage.show());
    }


}
