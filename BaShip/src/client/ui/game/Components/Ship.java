/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game.Components;

import java.util.*;
import pt.up.fe.lpro1613.sharedlib.utils.Coord;
import static pt.up.fe.lpro1613.sharedlib.constants.BoardK.*;

@Deprecated
public class Ship {
    
    public Ship(PossibleShips shipType)
    {
        this.shipType = shipType;
    }
    
    List<Coord> coords = new ArrayList<>();
    public final PossibleShips shipType;
    
    @Deprecated
    //TODO: This should concatenate 2 Ships if the new Coordinate wa between them and return the new Ship or a struct containing the Ships removed and the Ships created
    public boolean addCoord(Coord c)
    {
        if(c == null || c.x < 0 || c.y < 0 || c.x >= BOARD_SIZE || c.y >= BOARD_SIZE)
            return false;
        if(coords.size() <= 0)
        {
            coords.add(c);
            return true;
        }
        Coord first = coords.get(0), last = coords.get(coords.size()-1);
        Coord[] _coords = new Coord[] {first, last};
        for(int i = 0 ; i < 2 ; i++)
        {
            Coord _c = _coords[i];
            int dx, dy;
            dx = Math.abs(c.x - _c.x);
            dy = Math.abs(c.y - _c.y);
            if( (dx == 1 && dy == 0) || (dx == 0 && dy == 1))  //the new coordinate in near one edge
            {
                this.coords.add( i*this.coords.size(), _c); //if it is to be inserted in the front. Otherwise (i==1) insert at size()
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    /**
     * Removes the Coordinate c from this Ship and returns the Ships resultant after this removal.
     * Examples:
     * <p>  -c doesn't belong to this Ship -&gt do nothing</p>
     * <p>  -c is at an edge -&gt returns the same ship</p>
     * <p>  -c is in the middle of this ship -&gt returns the two Ships resultant from this removal</p>
     */
    public Ship[] removeCoord(Coord c)
    {
        coords.stream().filter( (_c) -> {return c.equals(_c);} ).forEach( (_c) -> {this.coords.remove(_c);} );
        return null;
    }
    
    public enum PossibleShips {
        Carrier(5,0),
        Battleship(4,1),
        //Cruiser(3,0),
        Submarine(3,2),
        Destroyer(2,3),
        Raft(1,4),  //inventados pelo Alex. lel (e nomeados por mim)
        /**
         * Represents a ship that shouldn't exit
         */
        Invalid(-1,0)
        ;
        
        public final int length;
        public final int nominalCount;
        
        /**
         * @param length Length of such ship
         * @param nominalCount Number of ships of this type that must be in the game
         */
        private PossibleShips(int length, int nominalCount)
        {
            this.length = length;
            this.nominalCount = nominalCount;
        }
    }
}
