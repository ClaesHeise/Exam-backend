package facades;

import dtos.MatchDTO;
import dtos.UserDTO;
import entities.Match;
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
            TypedQuery<User> query =  em.createQuery("SELECT u FROM User u WHERE u.userName = :username", User.class)
                    .setParameter("username", username);
            user = query.getSingleResult();
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
        User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getPhone(), userDTO.getEmail());
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
    public List<UserDTO> getAllUsers(String roleName) {
        EntityManager em = getEntityManager();
        TypedQuery<User> query =  em.createQuery("SELECT u FROM User u JOIN u.roleList rl WHERE rl.roleName = :rolename", User.class)
                .setParameter("rolename", roleName);
        List<User> users = query.getResultList();
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User u : users){
            userDTOS.add(new UserDTO(u));
        }
        em.close();
        return userDTOS;
    }

    //Read User Entity
    public UserDTO findUserFromId(Long id) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, id);
        em.close();
        return new UserDTO(user);
    }

    public UserDTO findUserFromName(String name ) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query =  em.createQuery("SELECT u FROM User u WHERE u.userName = :name", User.class)
                .setParameter("name", name);
        User user = query.getSingleResult();
        em.close();
        return new UserDTO(user);
    }
    public User returnPureUser(String name ) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query =  em.createQuery("SELECT u FROM User u WHERE u.userName = :name", User.class)
                .setParameter("name", name);
        User user = query.getSingleResult();
        em.close();
        return user;
    }

    //Update User Entity
    public UserDTO updateUserPassword(String username, String password)  {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query =  em.createQuery("SELECT u FROM User u WHERE u.userName = :name", User.class)
                .setParameter("name", username);
        User user = query.getSingleResult();
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
    public UserDTO deleteUser(String name) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query =  em.createQuery("SELECT u FROM User u WHERE u.userName = :name", User.class)
                .setParameter("name", name);
        User user = query.getSingleResult();
        System.out.println(user.toString());
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
