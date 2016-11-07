package client.conn;

import java.io.IOException;
import java.net.*;
import java.util.*;
import sharedlib.coms.Connection;
import sharedlib.coms.packet.*;
import sharedlib.logic.game.*;
import sharedlib.logic.player.*;

public class ServerConnection extends Connection {

    public ServerConnection(Socket socket) throws IOException {
        super(socket);
    }

    public boolean isUsernameAvailable(String username) throws IOException, ClassNotFoundException, InterruptedException {
        QueryPacket request = new QueryPacket("UsernameAvailable");        
        request.m.put("username", username);
        return ((BoolPacket)sendAndReceive(request)).b;
    }

    /**
     * Checks if a combination of username/password is valid or not
     *
     * @param username
     * @param password
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public boolean checkUsernamePasswordCombination(String username, String password) throws IOException, InterruptedException {
        QueryPacket request = new QueryPacket("UsernamePasswordPairExist");
        request.m.put("username", username);
        request.m.put("password", password);
        return ((BoolPacket)sendAndReceive(request)).b;
    }
    
    public void writeMessage(Game game, Player player, String message) {
        
    }
    
    public void writePublicMessage(Player player, String message) {
        
    }
    
    public Player login(String username, String password) {
        return null;
    }
    
    public void logout(Player player) {
        
    }
    
    public void fireShot(Game game, Player player, String message) {
        
    }
    
    public List<Game> getGameList() {
        return new ArrayList<>();
    }
    
    public List<Player> getPlayerList() {
        return new ArrayList<>();
    }
    
    
}