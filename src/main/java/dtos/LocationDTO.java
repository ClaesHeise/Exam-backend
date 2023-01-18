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

    public LocationDTO(String address, String city) {
        this.address = address;
        this.city = city;
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

    public String getCity() {
        return city;
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
