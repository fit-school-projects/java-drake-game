package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.src.BoardPos;
import thedrake.src.Move;
import thedrake.src.Tile;

import java.util.Objects;

public class TileView extends Pane {
        private final TileBackgrounds tileBackgrounds = new TileBackgrounds();
        private final BoardPos boardPos;
        private final Border selectionBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

        private final ImageView moveImage;
        private final TileViewContext tileViewContext;
        private Tile tile;
        private Move move;
        public TileView(Tile tile, BoardPos boardpos, TileViewContext tileViewContext) {
            this.tile = tile;
            this.boardPos = boardpos;
            this.tileViewContext = tileViewContext;

            setPrefSize(100, 100);
            update();
            setOnMouseClicked(e -> onClick());
            moveImage = new ImageView(Objects.requireNonNull(getClass().getResource("/assets/move.png")).toString());
            moveImage.setVisible(false);
            getChildren().add(moveImage);
        }

        public void setTile(Tile tile) {
            this.tile = tile;
            update();
        }

        public void update() {
            setBackground(tileBackgrounds.get(tile));
        }

        public BoardPos pos() {
            return boardPos;
        }

        private void onClick() {
            if (move != null) {
                tileViewContext.executeMove(move);
            }
            else if(tile.hasTroop()){
                select();
            }

        }

        private void select() {
            setBorder(selectionBorder);
            tileViewContext.tileViewSelected(this);
        }

        public void unselect() {
            setBorder(null);
        }

        public void setMove(Move move) {
            this.move = move;
            moveImage.setVisible(true);
        }

        public void removeMove(){
            this.move = null;
            moveImage.setVisible(false);
        }
    }
