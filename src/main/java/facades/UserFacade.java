package facades;

import dtos.UserDTO;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import security.errorhandling.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }
    //Create User entity
    public UserDTO createUser(UserDTO userDTO){
        EntityManager em = getEntityManager();
        User user = new User(userDTO.getUsername(), userDTO.getPassword());
        try {
            for(String s : userDTO.getRole()){
                Role role = em.find(Role.class, s);
                user.addRole(role);
            }
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(user);
    }

    //Read all users
    public List<UserDTO> getAllUsers() {
        EntityManager em = getEntityManager();
        TypedQuery<User> query =  em.createQuery("SELECT u FROM User u", User.class);
        List<User> users = query.getResultList();
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User u : users){
            userDTOS.add(new UserDTO(u));
        }
        em.close();
        return userDTOS;
    }

    //Read User Entity
    public UserDTO findUserFromUsername(String username) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, username);
        em.close();
        return new UserDTO(user);
    }

    //Update User Entity
    public UserDTO updateUserPassword(String username, String password)  {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, username);
        user.setUserPass(password);
        try{
            em.getTransaction().begin();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(user);
    }

    // Delete User Entity
    public UserDTO deleteUser(String username) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, username);
        try {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(user);
    }

    public void createRole(Role role){ //Only for populator (so backend devs)
         EntityManager em = getEntityManager();
         try{
             em.getTransaction().begin();
             em.persist(role);
             em.getTransaction().commit();
         } finally {
             em.close();
         }
    }
}
