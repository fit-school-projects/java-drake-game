package thedrake.ui;

import thedrake.src.GameState;
import thedrake.src.Move;

public interface TileViewContext {
    void tileViewSelected(TileView tileView);
    void stackViewSelected(StackView stackView);

    void executeMove(Move move);

    GameState getState();
}
