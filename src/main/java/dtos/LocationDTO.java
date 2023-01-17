package dtos;

import entities.Location;
import entities.Match;
import entities.User;

import java.util.List;

public class LocationDTO {
    private Long id;

    private String address;

    private String city;

    private Boolean condition;
    private List<MatchDTO> matches;

    public LocationDTO(Long id, String address, String city, boolean condition) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.condition = condition;
    }

    public LocationDTO(Long id, String address, String city, boolean condition, List<MatchDTO> matches) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.condition = condition;
        this.matches = matches;
    }

    public LocationDTO(Location location) {
        if(location.getAddress() != null){
            this.id = location.getId();
            this.address = location.getAddress();
            this.city = location.getCity();
            this.condition = location.getCondition();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean getCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public List<MatchDTO> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchDTO> matches) {
        this.matches = matches;
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", condition=" + condition +
                ", matches=" + matches +
                '}';
    }
}
