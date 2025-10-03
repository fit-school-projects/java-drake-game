package thedrake.src;

import thedrake.src.*;

import java.io.PrintWriter;

public class Board implements JSONSerializable {
    private final BoardTile[][] boardTiles;
    int dimension;

    // Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
    public Board(int dimension) {
        boardTiles = new BoardTile[dimension][];
        for (int i = 0; i < dimension; i++) {
            boardTiles[i] = new BoardTile[dimension];
            for (int j = 0; j < dimension; j++) {
                boardTiles[i][j] = BoardTile.EMPTY;
            }
        }
        this.dimension = dimension;
    }

    // Rozměr hrací desky
    public int dimension() {
        return dimension;
    }

    // Vrací dlaždici na zvolené pozici.
    public BoardTile at(TilePos pos) {
        return boardTiles[pos.i()][pos.j()];
    }

    // Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
    public Board withTiles(TileAt... ats) {
        Board newBoard = new Board(dimension);
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(boardTiles[i], 0, newBoard.boardTiles[i], 0, dimension);
        }
        for (TileAt at : ats) {
            newBoard.boardTiles[at.pos.i()][at.pos.j()] = at.tile;
        }
        return newBoard;
    }

    // Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
    public PositionFactory positionFactory() {
        return new PositionFactory(dimension);
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }

    @Override
    public void toJSON(PrintWriter writer) {

        writer.print("{\"dimension\":" + dimension);
        writer.print(",\"tiles\":[");

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                boardTiles[j][i].toJSON(writer);
                if (j < dimension - 1 || i < dimension - 1) {
                    writer.print(",");
                }
            }
        }
        writer.print("]}");
    }
}

