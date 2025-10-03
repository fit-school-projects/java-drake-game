package thedrake;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import thedrake.src.GameResult;

public class TheDrakeController {
    @FXML
    protected void onExitButtonClicked() {
        System.exit(0);
    }

    public void onPlayerVsPlayerClicked(ActionEvent actionEvent) {
        GameResult.setResult(GameResult.IN_PLAY);
    }
}