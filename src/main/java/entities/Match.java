package entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "matches")
public class Match {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "opponentTeam")
    private String opponentTeam;

    @Column(name = "judge")
    private String judge;

    @Column(name = "type")
    private String type;

    @Column(name = "inDoors")
    private boolean inDoors;

    @ManyToMany(mappedBy = "matchList")
    private List<User> userList;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public Match() {}

    public Match(String opponentTeam, String judge, String type, boolean inDoors) {
        this.opponentTeam = opponentTeam;
        this.judge = judge;
        this.type = type;
        this.inDoors = inDoors;
    }

    public void assingLocation(Location _location){
        if(_location != null){
            this.location = _location;
            _location.getMatches().add(this);
            _location.setCondition(true);
        }
    }

    public Long getId() {
        return id;
    }

    public String getOpponentTeam() {
        return opponentTeam;
    }

    public void setOpponentTeam(String opponentTeam) {
        this.opponentTeam = opponentTeam;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isInDoors() {
        return inDoors;
    }

    public void setInDoors(boolean inDoors) {
        this.inDoors = inDoors;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (this.location != null){
            this.location.getMatches().remove(this);
        }
        this.location = location;
        if(location != null){
            location.getMatches().add(this);
        }
    }

    public List<User> getUserList() {
        return userList;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", opponentTeam='" + opponentTeam + '\'' +
                ", judge='" + judge + '\'' +
                ", type='" + type + '\'' +
                ", inDoors=" + inDoors +
                ", location=" + location.toString() +
                '}';
    }
}
