package thedrake.src;

import thedrake.src.*;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public BoardTroops(PlayingSide playingSide) {
        this(playingSide, Collections.emptyMap(), TilePos.OFF_BOARD, 0);
    }

    public Optional<TroopTile> at(TilePos pos) {
        TroopTile tile = troopMap.get((BoardPos) pos);
        return Optional.ofNullable(tile);
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        return leaderPosition;
    }

    public int guards() {
        return guards;
    }

    public boolean isLeaderPlaced() {
        return leaderPosition != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        return leaderPosition != TilePos.OFF_BOARD && guards < 2;
    }

    public Set<BoardPos> troopPositions() {
        Set<BoardPos> troops = new HashSet<>();
        for (Map.Entry<BoardPos, TroopTile> entry : this.troopMap.entrySet()){
            if (entry.getValue() != null) {
                troops.add(entry.getKey());
            }
        }
        return troops;
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (at(target).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);

        newTroopMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));
        if (leaderPosition == TilePos.OFF_BOARD){
            return new BoardTroops(playingSide, newTroopMap, (TilePos) target, guards);
        } else if (guards < 2){
            int guardsTmp = guards + 1;
            return new BoardTroops(playingSide, newTroopMap, leaderPosition, guardsTmp);
        } else {
            return new BoardTroops(playingSide, newTroopMap, leaderPosition, guards);
        }
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (at(origin).isEmpty() || at(target).isPresent())
            throw new IllegalArgumentException();

        TilePos leadPos = leaderPosition;
        if (origin.equals(leaderPosition))
            leadPos = target;

        TroopTile tile = troopMap.remove(origin);
        troopMap.put(target, tile.flipped());

        return new BoardTroops(playingSide, troopMap, leadPos, guards);
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }
        if (at(target).isEmpty())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);
        if (target.equals(leaderPosition))
            return new BoardTroops(playingSide, newTroops, TilePos.OFF_BOARD, guards);
        return new BoardTroops(playingSide, newTroops, leaderPosition, guards);
    }

    @Override
    public void toJSON(PrintWriter writer) {

        writer.print("{\"side\":\"" + this.playingSide + "\",\"leaderPosition\":");
        this.leaderPosition.toJSON(writer);
        writer.print(",\"guards\":" + this.guards + ",");

        writer.print("\"troopMap\":{");

        Set<BoardPos> positions = new TreeSet<>(Comparator.comparing(BoardPos::toString));
        positions.addAll(troopMap.keySet());
        boolean isFirst = true;
        for (BoardPos pos : positions) {
            TroopTile tile = troopMap.get(pos);
            if (tile != null){
                if (isFirst) {
                    pos.toJSON(writer);
                    isFirst = false;
                }
                else {
                    writer.print(",");
                    pos.toJSON(writer);
                }
                writer.print(":");
                tile.toJSON(writer);
            }
        }
        writer.print("}}");
    }
}
