package server.logic.game;

import java.util.*;
import server.logic.chat.Chat;
import server.logic.player.Player;

/**
 *
 * @author Alex
 */
public class Game {
    
    public final Chat chat;
    public final Player p1, p2;
    public final Board b1, b2;
    public List<Move> moves;
    
    public Game() {
        this.chat = new Chat();
        p1 = p2 = null;
        b1 = b2 = null;
    }
    
}
