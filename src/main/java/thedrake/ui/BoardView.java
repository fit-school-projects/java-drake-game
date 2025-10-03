package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import thedrake.src.*;

import java.util.List;

public class BoardView extends GridPane implements TileViewContext {
    private GameState gameState;
    private ValidMoves validMoves;
    private TileView tileViewSelected;
    private StackView stackViewSelected;
    private StackView BlueStack, OrangeStack;
    private CapturedView CapturedBlue, CapturedOrange;

    public BoardView(GameState gameState) {
        this.gameState = gameState;
        validMoves = new ValidMoves(gameState);
        initializeBoard();
        configureLayout();
        initializeStacksAndCapturedViews();
        updateSideOnTurn();
    }

    private void initializeBoard() {
        PositionFactory positionFactory = gameState.board().positionFactory();
        for (int y = 0; y < positionFactory.dimension(); y++){
            for (int x = 0; x < positionFactory.dimension(); x++){
                BoardPos boardPos = positionFactory.pos(x,positionFactory.dimension() - y - 1);
                add(new TileView(gameState.tileAt(boardPos), boardPos, this), x, y);
            }
        }
    }

    private void configureLayout() {
        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15, 100, 15, 100));
        setAlignment(Pos.CENTER);
    }

    private void initializeStacksAndCapturedViews() {
        BlueStack = createStackView(PlayingSide.BLUE);
        OrangeStack = createStackView(PlayingSide.ORANGE);

        CapturedBlue = createCapturedView(PlayingSide.BLUE);
        CapturedOrange = createCapturedView(PlayingSide.ORANGE);
    }

    private StackView createStackView(PlayingSide side) {
        StackView stackView = new StackView(this, side);
        stackView.setMaxSize(100, 100);
        return stackView;
    }

    private CapturedView createCapturedView(PlayingSide side) {
        CapturedView capturedView = new CapturedView();
        capturedView.SetSide(side);
        return capturedView;
    }

    private void updateTiles() {
        for (Node node : getChildren()) {
            if (node instanceof TileView tileView) {
                tileView.setTile(gameState.tileAt(tileView.pos()));
                tileView.update();
            }
        }
        BlueStack.update();
        OrangeStack.update();
        updateSideOnTurn();
    }

    private void updateSideOnTurn() {
        if (gameState.sideOnTurn() == PlayingSide.BLUE) {
            setStyle("-fx-background-color: #72bff5;");
        } else {
            setStyle("-fx-background-color: #f5c055;");
        }
    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (tileView != tileViewSelected) {
            unselectPrevious();
            tileViewSelected = tileView;
            stackViewSelected = null;
            removeMoves();
            showMoves(validMoves.boardMoves(tileView.pos()));
        }
    }

    @Override
    public void stackViewSelected(StackView stackView) {
        if (stackView != stackViewSelected) {
            unselectPrevious();
            stackViewSelected = stackView;
            tileViewSelected = null;
            removeMoves();
            if (gameState.sideOnTurn() == stackView.side()) {
                showMoves(validMoves.movesFromStack());
            }
        }
    }

    private void unselectPrevious() {
        if (tileViewSelected != null) {
            tileViewSelected.unselect();
            tileViewSelected = null;
        }
        if (stackViewSelected != null) {
            stackViewSelected.unselect();
            stackViewSelected = null;
        }
    }

    private void updateBoardState() {
        updateTiles();
//        BlueStack.update();
//        OrangeStack.update();
        CapturedBlue.stateChanged(gameState);
        CapturedOrange.stateChanged(gameState);
        GameResult.setResult(gameState.result());
    }

    @Override
    public void executeMove(Move move) {
        unselectPrevious();
        tileViewSelected = null;
        stackViewSelected = null;
        removeMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        updateBoardState();
    }

    @Override
    public GameState getState() {
        return gameState;
    }

    private void showMoves(List<Move> moves){
        moves.forEach(move -> tileViewAt(move.target()).setMove(move));
    }

    private void removeMoves(){
        getChildren().stream()
                .filter(node -> node instanceof TileView)
                .forEach(node -> ((TileView) node).removeMove());
    }

    private TileView tileViewAt(BoardPos pos){
        int index = (gameState.board().dimension() - 1 - pos.j()) * 4 + pos.i();
        return (TileView) getChildren().get(index);
    }
    public StackView getBlueStack() { return BlueStack; }
    public StackView getOrangeStack() { return OrangeStack; }
    public CapturedView getCapturedBlue() { return CapturedBlue; }
    public CapturedView getCapturedOrange() { return CapturedOrange; }
}
