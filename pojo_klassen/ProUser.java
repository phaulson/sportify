/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgData;

import java.util.TreeSet;

/**
 *
 * @author Simon
 */
public class ProUser extends User {
    private TreeSet<Location> locations = new TreeSet<Location>();
    private TreeSet<Plan> plans = new TreeSet<Plan>();
    public ProUser(int idUser, String username, String password, String biographie) {
        super(idUser, username, password, biographie, true);
    }
    
}
