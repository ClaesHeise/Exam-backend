package facades;

import dtos.MatchDTO;
import dtos.UserDTO;
import entities.Location;
import entities.Match;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

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

    // User story 4 - add new: Match, User & Location
    public MatchDTO createMatch(MatchDTO matchDTO){
        EntityManager em = getEntityManager();
        Match match = new Match(matchDTO.getOpponentTeam(), matchDTO.getJudge(), matchDTO.getType(), matchDTO.isInDoors());
        try {
            if(matchDTO.getUsers() != null){
                for(UserDTO u : matchDTO.getUsers()){
                    User user = em.find(User.class, u.getId());
                    user.addMatch(match);
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

    // User story 1 - get all matches
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

    // User story 2 - get all matches, as a player
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

    // User story 3 - get all matches, on a specific location
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

    // User story 5 & 6 - Change match / Aswell as add relations between match and User + Location
    public MatchDTO updateMatch(MatchDTO matchDTO)  {
        EntityManager em = emf.createEntityManager();
        Match match = em.find(Match.class, matchDTO.getId());
        match.setOpponentTeam(matchDTO.getOpponentTeam());
        match.setJudge(matchDTO.getJudge());
        match.setType(matchDTO.getType());
        match.setInDoors(matchDTO.isInDoors());
        try{
            if(matchDTO.getUserIds() != null){
                for(Long l : matchDTO.getUserIds()){
                    User user = em.find(User.class, l);
                    user.addMatch(match);
                }
            }
            if(matchDTO.getLocationId() != null){
                Location location = em.find(Location.class, matchDTO.getLocationId());
                match.assingLocation(location);
            }
            em.getTransaction().begin();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new MatchDTO(match);
    }

}
