package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "location")
public class Location {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "conditions")
    private Boolean condition;

    @OneToMany(mappedBy = "location")
    private Set<Match> matches = new LinkedHashSet<>();

    private boolean cond(){
        if(matches != null && matches.size() > 0){
            return true;
        } else {
            return false;
        }
    }

    public Location() {}

    public Location(String address, String city) {
        this.address = address;
        this.city = city;
        this.condition = cond();
    }

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public boolean getCondition() {return condition;}

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public Set<Match> getMatches() {
        return matches;
    }

}
