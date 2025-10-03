package thedrake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import thedrake.src.GameResult;
import thedrake.src.PlayingSide;
import thedrake.ui.GameScreen;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class TheDrakeApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        GameResult.gameScreen = new GameScreen(GameResult.createSampleGameState());
        GameResult.gameScene = new Scene(GameResult.gameScreen, 800, 800);

        Scene MainMenu = new Scene(FXMLLoader.load(Objects.requireNonNull(TheDrakeApplication.class.getResource("intro-page.fxml"))), 800, 500);
        stage.setTitle("The Drake!");
        stage.setScene(MainMenu);
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (GameResult.changed) {
                    switch (GameResult.result) {
                        case IN_PLAY -> stage.setScene(GameResult.gameScene);
                        case VICTORY -> showWinningDialog(GameResult.gameScreen.getWinner(), MainMenu, false, stage);
                        case DRAW -> showWinningDialog(null, MainMenu, true, stage);
                    }
                    stage.show();
                    GameResult.changed = false;
                }
            }
        }.start();
    }

    private void showWinningDialog(PlayingSide side, Scene mainMenu, boolean draw, Stage stage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (draw) {
                alert.setTitle("Remíza!");
                alert.setContentText("Hra skončila remízou!");
            } else {
                alert.setTitle("Víťazstvo!");
                alert.setContentText(side == PlayingSide.BLUE ? "Modrý hráč vyhral!" : "Oranžový hráč vyhral!");
            }
            alert.setHeaderText(null);

            ButtonType buttonTypeOne = new ButtonType("Vrátiť sa do menu");
            ButtonType buttonTypeTwo = new ButtonType("Začať novú hru");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == buttonTypeOne){
                stage.setScene(mainMenu);
                GameResult.gameScreen = new GameScreen(GameResult.createSampleGameState());
                GameResult.gameScene = new Scene(GameResult.gameScreen, 800, 800);
            } else if (result.isPresent() && result.get() == buttonTypeTwo) {
                GameResult.gameScreen = new GameScreen(GameResult.createSampleGameState());
                GameResult.gameScene = new Scene(GameResult.gameScreen, 800, 800);
                GameResult.setResult(GameResult.IN_PLAY);
            } else {
                System.exit(0);
            }
        });
    }
}