/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import javax.persistence.EntityManagerFactory;

import dtos.UserDTO;
import entities.Role;
import utils.EMF_Creator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        UserFacade uf = UserFacade.getUserFacade(emf);
        Role role = new Role("admin");
        uf.createRole(role);
        List<String> list = new ArrayList<>();
        list.add("admin");
        uf.createUser(new UserDTO("Claes Heise", "claes123", list));
        uf.createUser(new UserDTO("Oliver R.", "oliver123", list));
    }
    
    public static void main(String[] args) {
        populate();
    }
}
