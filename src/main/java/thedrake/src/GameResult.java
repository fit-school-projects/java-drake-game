package thedrake.src;

import javafx.scene.Scene;
import thedrake.ui.GameScreen;

import java.io.PrintWriter;
import java.util.Random;

public enum GameResult implements JSONSerializable {
    VICTORY, DRAW, IN_PLAY;

    public static GameScreen gameScreen;
    public static Scene gameScene;
    public static boolean changed;
    public static GameResult result;

    public static void setResult(GameResult gameResult) {
        GameResult.result = gameResult;
        GameResult.changed = true;
    }

    public static GameState createSampleGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        Random random = new Random();
        int numberOfMountains = random.nextInt(3);

        for (int i = 0; i < numberOfMountains + 1; i++){
            board = board.withTiles(new Board.TileAt(positionFactory.pos(random.nextInt(4), random.nextInt(4)), BoardTile.MOUNTAIN));
        }
        return new StandardDrakeSetup().startState(board);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("\"" + this.name() + "\"");
    }
}
