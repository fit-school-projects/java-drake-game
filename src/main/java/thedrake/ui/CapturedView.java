package thedrake.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import thedrake.src.GameState;
import thedrake.src.PlayingSide;
import thedrake.src.Troop;


public class CapturedView extends VBox
{
    private PlayingSide side;
    public void SetSide(PlayingSide playingSide) {
        this.side = playingSide;
    }

    public void stateChanged(GameState state) {
        getChildren().clear();

        for (Troop capturedTroop : state.army(side).captured()) {
            CapturedTileView newCapturedTile = new CapturedTileView(capturedTroop, side == PlayingSide.BLUE ? PlayingSide.ORANGE : PlayingSide.BLUE);
            newCapturedTile.setPadding(new Insets(10, 10, 0, 0));
            getChildren().add(newCapturedTile);
        }
    }
}


