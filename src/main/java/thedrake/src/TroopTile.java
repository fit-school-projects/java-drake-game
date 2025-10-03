package thedrake.src;

import thedrake.src.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile, JSONSerializable {

    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;
    public TroopTile (Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    public PlayingSide side() {
        return side;
    }

    public TroopFace face() {
        return face;
    }
    public Troop troop() {
        return troop;
    }
    @Override
    public boolean canStepOn() {
        return false;
    }

    @Override
    public boolean hasTroop() {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> moves = new ArrayList<>();
        for (TroopAction action : troop.actions(face)){
            moves.addAll(action.movesFrom(pos, side, state));
        }
        return moves;
    }

    public TroopTile flipped () {
        TroopFace tmpFace = this.face == TroopFace.AVERS ? TroopFace.REVERS : TroopFace.AVERS;
        return new TroopTile(troop, side, tmpFace);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"troop\":\"" + this.troop.name() + "\",\"side\":\"" + this.side + "\",\"face\":\"" + this.face + "\"}");
    }
}
