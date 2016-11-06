package sharedlib.logic.game;

import java.util.*;
import sharedlib.logic.chat.*;
import sharedlib.logic.player.*;

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
