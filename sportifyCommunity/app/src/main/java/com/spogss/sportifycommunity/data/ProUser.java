/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spogss.sportifycommunity.data;

import java.io.Serializable;
import java.util.TreeSet;

/**
 *
 * @author Martin
 */
public class ProUser extends User implements Serializable{
    public ProUser(int id, String username, String password, String description) {
        super(id, username, password, description);
    }
}
