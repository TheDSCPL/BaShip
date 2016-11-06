/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sharedlib.logic.game;

import sharedlib.logic.player.*;

/**
 *
 * @author Alex
 */
public class Move {
    
    public final Player player;
    public final Coordinate coord;
    
    Move(Player player, Coordinate coord) {
        this.player = player;
        this.coord = coord;
    }
}
