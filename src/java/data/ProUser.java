/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.TreeSet;

/**
 *
 * @author Martin
 */
public class ProUser extends User {
    private TreeSet<Location> locations = new TreeSet<Location>();
    public ProUser(int idUser, String username, String password, String biographie) {
        super(idUser, username, password, biographie, true);
    }
    
}
