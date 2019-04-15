/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Martin
 */
@XmlRootElement
public class ProUser extends User implements Serializable{
    
    public ProUser(int idUser, String username, String password, String biographie) {
        super(idUser, username, password, biographie);
    }
    public ProUser(){
        super();
    }
}
