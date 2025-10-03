package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import thedrake.src.GameState;
import thedrake.src.PlayingSide;

public class GameScreen extends BorderPane {
    BoardView boardView;
    public GameScreen(GameState gameState) {
        boardView = new BoardView(gameState);
        boardView.setPadding(new Insets(10, 10, 10, 10));
        this.setCenter(boardView);

        StackPane topStack = new StackPane(boardView.getOrangeStack());
        topStack.setAlignment(Pos.CENTER);
        topStack.setPadding(new Insets(50, 10, 10, 10));
        this.setTop(topStack);

        StackPane bottomStack = new StackPane(boardView.getBlueStack());
        bottomStack.setAlignment(Pos.CENTER);
        bottomStack.setPadding(new Insets(10, 10, 50, 10));
        this.setBottom(bottomStack);


        CapturedView capturedBlue = boardView.getCapturedBlue();
        capturedBlue.setMinWidth(100);
        this.setRight(capturedBlue);

        CapturedView capturedOrange = boardView.getCapturedOrange();
        capturedOrange.setMinWidth(100);
        this.setLeft(capturedOrange);
        setStyle("-fx-background-color: #ffe4ac;");
    }

    public PlayingSide getWinner() {
        return boardView.getState().armyNotOnTurn().side();
    }
}
