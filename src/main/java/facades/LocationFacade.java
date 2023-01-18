package facades;

import dtos.LocationDTO;
import entities.Location;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

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

    // User story 4 - add new: Match, User & Location
    public LocationDTO createLocation(LocationDTO locationDTO) {
        EntityManager em = getEntityManager();
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

    public LocationDTO getLocationById(Long id) {
        EntityManager em = emf.createEntityManager();
        Location location = em.find(Location.class, id);
        em.close();
        return new LocationDTO(location);
    }
}
