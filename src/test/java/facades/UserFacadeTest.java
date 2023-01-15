package facades;

import dtos.UserDTO;
import entities.Role;
import entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserFacadeTest {
    private static EntityManagerFactory emf;
    private static UserFacade facade;

    @BeforeAll
    static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from User ").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            Role role1 = new Role("admin");
            Role role2 = new Role("user");
            User user1 = new User("admin1", "test1");
            User user2 = new User("admin2", "test2");
            User user3 = new User("user1", "test3");
            user1.addRole(role1);
            user2.addRole(role1);
            user3.addRole(role2);
            em.persist(role1);
            em.persist(role2);
            em.persist(user1);
            em.persist(user2);
            em.persist(user3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllAdminUsers() {
        List<UserDTO> users = facade.getAllUsers("admin");
        assertEquals("admin1", users.get(0).getUsername());
        assertEquals("admin2", users.get(1).getUsername());
        assertEquals(2, users.size());
    }

    @Test
    void getSingleUser() {
        UserDTO user = facade.findUserFromUsername("user1");
        assertEquals("user", user.getRole().get(0));
        assertEquals("user1", user.getUsername());
    }

    @Test
    void createUser() {
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        UserDTO user = new UserDTO("Troels", "troels123", roles);
        try{
            facade.createUser(user);
        } catch (Exception e){
            System.out.println(e);
        }
        UserDTO userFromDB = facade.findUserFromUsername("Troels");
        assertEquals(user.getUsername(), userFromDB.getUsername());
        assertEquals(user.getRole(), userFromDB.getRole());
    }

    @Test
    void updateUser() {
        User user1;
        User user2;
        try {
            user1 = facade.getVeryfiedUser("user1", "test3");
            facade.updateUserPassword("user1", "newPass");
            user2 = facade.getVeryfiedUser("user1", "newPass");
            assertEquals(true, user1.verifyPassword("test3"));
            assertEquals(true, user2.verifyPassword("newPass"));
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    void deleteUser() {
        UserDTO user = facade.findUserFromUsername("admin2");
        assertEquals("admin2", user.getUsername());
        try{
            facade.deleteUser("admin2");
        } catch (Exception e){
            System.out.println(e);
        }

        assertThrows(NullPointerException.class, () -> facade.findUserFromUsername("admin2"));
    }
}