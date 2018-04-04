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
public class proUser extends user {
    private TreeSet<location> locations = new TreeSet<location>();
    public proUser(int idUser, String username, String password, String biographie) {
        super(idUser, username, password, biographie, true);
    }
    
}
