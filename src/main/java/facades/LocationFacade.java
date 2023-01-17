package facades;

import dtos.LocationDTO;
import dtos.MatchDTO;
import dtos.UserDTO;
import entities.Location;
import entities.Match;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocationFacade {
    private static EntityManagerFactory emf;
    private static LocationFacade instance;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private LocationFacade() {
    }

    public static LocationFacade getLocationFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new LocationFacade();
        }
        return instance;
    }

    public LocationDTO createLocation(LocationDTO locationDTO) {
        EntityManager em = getEntityManager();
//        Set<Match> matches = new HashSet<>();
//        Location location;
//        if(locationDTO.getMatches() != null) {
//            for (MatchDTO matchDTO : locationDTO.getMatches()) {
//                Match match = new Match(
//                        matchDTO.getId(),
//                        matchDTO.getOpponentTeam(),
//                        matchDTO.getJudge(),
//                        matchDTO.getType(),
//                        matchDTO.isInDoors()
//                );
//                matches.add(match);
//            }
//
//        }
        Location location = new Location(locationDTO.getAddress(), locationDTO.getCity());
        try {
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new LocationDTO(location);
    }

    public List<LocationDTO> getAllLocations() {
        EntityManager em = getEntityManager();
        TypedQuery<Location> query =  em.createQuery("SELECT l FROM Location l", Location.class);
        List<Location> locations = query.getResultList();
        List<LocationDTO> locationDTOS = new ArrayList<>();
        for(Location l : locations){
            locationDTOS.add(new LocationDTO(l));
        }
        em.close();
        return locationDTOS;
    }

    public List<LocationDTO> getLocationById(String id) {
        EntityManager em = getEntityManager();
        TypedQuery<Location> query =  em.createQuery("SELECT l FROM Location l WHERE l.id = :id", Location.class)
                .setParameter("id", id);
        List<Location> locations = query.getResultList();
        List<LocationDTO> locationDTOS = new ArrayList<>();
        for(Location l : locations){
            locationDTOS.add(new LocationDTO(l));
        }
        em.close();
        return locationDTOS;
    }
    public LocationDTO getLocationById(Long id) {
        EntityManager em = emf.createEntityManager();
        Location location = em.find(Location.class, id);
        em.close();
        return new LocationDTO(location);
    }
}
