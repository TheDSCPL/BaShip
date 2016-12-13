package sharedlib.structs;

import sharedlib.utils.Coord;

public class Move {
    
    public final Coord coord;
    public final int playerN;

    public Move(Coord coord, int playerN) {
        this.coord = coord;
        this.playerN = playerN;
    }
}
