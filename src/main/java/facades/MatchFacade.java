package facades;

import dtos.LocationDTO;
import dtos.MatchDTO;
import dtos.UserDTO;
import entities.Location;
import entities.Match;
import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchFacade {
    private static EntityManagerFactory emf;
    private static MatchFacade instance;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private MatchFacade() {
    }

    public static MatchFacade getMatchFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MatchFacade();
        }
        return instance;
    }

    public MatchDTO createMatch(MatchDTO matchDTO){
        EntityManager em = getEntityManager();
        Match match = new Match(matchDTO.getOpponentTeam(), matchDTO.getJudge(), matchDTO.getType(), matchDTO.isInDoors());
        try {
            if(matchDTO.getUsers() != null){
//                System.out.println(matchDTO.getUsers().get(0).toString());
//                TypedQuery<User> query =  em.createQuery("SELECT u FROM User u JOIN u.matchList ml WHERE ml.id = :id", User.class)
//                        .setParameter("id", match.getId());
//                List<User> users = query.getResultList();
//                System.out.println(users.get(0).toString());
                for(UserDTO u : matchDTO.getUsers()){
                    User user = em.find(User.class, u.getId());
                    user.addMatch(match);
//                    match.addUser(user);
                }
            }
            if(matchDTO.getLocation() != null){
                Location location = em.find(Location.class, matchDTO.getLocation().getId());
                match.assingLocation(location);
            }
            em.getTransaction().begin();
            em.persist(match);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new MatchDTO(match);
    }

    public MatchDTO findMatchFromId(Long id) {
        EntityManager em = emf.createEntityManager();
        Match match = em.find(Match.class, id);
        em.close();
        return new MatchDTO(match);
    }

    public List<MatchDTO> getAllMatches() {
        EntityManager em = getEntityManager();
        TypedQuery<Match> query =  em.createQuery("SELECT m FROM Match m", Match.class);
        List<Match> matches = query.getResultList();
        List<MatchDTO> matchDTOS = new ArrayList<>();
        for(Match m : matches){
            matchDTOS.add(new MatchDTO(m));
        }
        em.close();
        return matchDTOS;
    }
    public List<MatchDTO> getAllMatchesForPlayer(String userName) {
        EntityManager em = getEntityManager();
        TypedQuery<Match> query =  em.createQuery("SELECT m FROM Match m JOIN m.userList ul WHERE ul.userName = :un", Match.class)
                .setParameter("un", userName);
        List<Match> matches = query.getResultList();
        List<MatchDTO> matchDTOS = new ArrayList<>();
        for(Match m : matches){
            matchDTOS.add(new MatchDTO(m));
        }
        em.close();
        return matchDTOS;
    }

    public List<MatchDTO> getAllMatchesForLocation(String locationName) {
        EntityManager em = getEntityManager();
        TypedQuery<Match> query =  em.createQuery("SELECT m FROM Match m JOIN m.location l WHERE l.address = :ln", Match.class)
                .setParameter("ln", locationName);
        List<Match> matches = query.getResultList();
        List<MatchDTO> matchDTOS = new ArrayList<>();
        for(Match m : matches){
            matchDTOS.add(new MatchDTO(m));
        }
        em.close();
        return matchDTOS;
    }

}
