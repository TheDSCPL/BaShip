/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sharedlib.logic.chat;

import java.util.*;

/**
 *
 * @author Alex
 */
public class Message {
    
    public final Date date;
    public final String message;
    
    public Message(Date date, String message) {
        this.date = date;
        this.message = message;
    }
    
}
