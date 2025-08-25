package com.example.javachess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.IOException;
import java.util.Objects;
import java.awt.Taskbar;
import javax.imageio.ImageIO;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Java Chess");
        stage.setScene(scene);
        stage.setResizable(false);

        Image appIcon = new Image(Objects.requireNonNull(getClass().getResource("/com/example/javachess/images/icon.png")).toExternalForm());
        stage.getIcons().add(appIcon);
        Taskbar.getTaskbar().setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("/com/example/javachess/images/icon.png"))));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
