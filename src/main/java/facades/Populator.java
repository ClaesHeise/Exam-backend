/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import javax.persistence.EntityManagerFactory;

import dtos.LocationDTO;
import dtos.MatchDTO;
import dtos.UserDTO;
import entities.Location;
import entities.Match;
import entities.Role;
import entities.User;
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
        LocationFacade lf = LocationFacade.getLocationFacade(emf);
        MatchFacade mf = MatchFacade.getMatchFacade(emf);
        // Adds the locations to the db
        Location location1 = new Location("Faelledparken", "Copenhagen");
        Location location2 = new Location("Roskilde Gymnasium Idraethus", "Roskilde");
        LocationDTO locationDTO1 = new LocationDTO(location1);
        LocationDTO locationDTO2 = new LocationDTO(location2);
        lf.createLocation(locationDTO1);
        lf.createLocation(locationDTO2);

        //Adds the roles
        Role role = new Role("admin");
        uf.createRole(role);
        Role role2 = new Role("player");
        uf.createRole(role2);

        // // Adding admins
        User admin1 = new User("Claes Heise", "claes123", "12345678", "claes@gmail.com");
        admin1.addRole(role);
        uf.createUser(new UserDTO(admin1));

        User admin2 = new User("Oliver R.", "oliver123", "87654321", "oliver@gmail.com");
        admin2.addRole(role);
        uf.createUser(new UserDTO(admin2));

        // // Adding players
        User player1 = new User("John", "john123", "12345678", "john@gmail.com");
        player1.addRole(role2);
        System.out.println(player1.getUserName()+" : "+player1.getUserPass());

        User player2 = new User("Mia", "mia123", "87654321", "mia@gmail.com");
        player2.addRole(role2);

        uf.createUser(new UserDTO(player1));
        uf.createUser(new UserDTO(player2));


        // Adds the matches
        LocationDTO locationDTO3 = lf.getLocationById(1L);
        LocationDTO locationDTO4 = lf.getLocationById(2L);
//        UserDTO user1 = uf.findUserFromId(3L);
//        UserDTO user2 = uf.findUserFromId(4L);

//        Match match1 = new Match(1L, "Hvidovre Boldklub vs. Roskilde boldklub", "Lene Kristensen", "Turnering", false);
//        match1.assingLocation(new Location(locationDTO3.getId(), locationDTO3.getAddress(), locationDTO3.getCity()));
////        User userRepeat = new User(user1.getId(), user1.getUsername(), user1.getPassword(), user1.getPhone(), user1.getEmail());
//        match1.addUser(player1);
//        mf.createMatch(new MatchDTO(match1));
        List<UserDTO> users1 = new ArrayList<>();
        users1.add(uf.findUserFromName("John"));
        MatchDTO matchDTO = new MatchDTO("Hvidovre Boldklub vs. Roskilde boldklub", "Lene Kristensen", "Turnering", false, users1, locationDTO3);

        List<UserDTO> users2 = new ArrayList<>();
        users2.add(uf.findUserFromName("John"));
        users2.add(uf.findUserFromName("Mia"));
        MatchDTO matchDTO2 = new MatchDTO("Handelstandens boldklub vs. Heimdal", "Jan Stum", "Venskabskamp", true, users2, locationDTO4);

        mf.createMatch(matchDTO);
        mf.createMatch(matchDTO2);
//        Match match2 = new Match(2L, "Handelstandens boldklub vs. Heimdal", "Jan Stum", "Venskabskamp", true);
//        match2.assingLocation(new Location(locationDTO4.getId(), locationDTO4.getAddress(), locationDTO4.getCity()));
////        match2.addUser(new User(user2.getId(), user2.getUsername(), user2.getPassword(), user2.getPhone(), user2.getEmail()));
//        match2.addUser(player2);
//        System.out.println(userRepeat.getUserPass());

//        mf.createMatch(new MatchDTO(match2));

    }
    
    public static void main(String[] args) {
        populate();
    }
}
