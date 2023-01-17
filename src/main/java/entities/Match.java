package entities;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
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

    @JoinTable(name = "user_matches", joinColumns = {
            @JoinColumn(name = "match_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "user_name", referencedColumnName = "user_name")})
    @ManyToMany
    private List<User> userList = new ArrayList<>();

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

    public Match(Long id, String opponentTeam, String judge, String type, boolean inDoors) {
        this.id = id;
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

    public void setUserList(List<User> userList) {
        if(this.userList != null){
            for(User u : userList){
                u.getMatchList().remove(this);
            }
        }
        this.userList = userList;
        if(userList != null){
            for(User u : userList){
                u.getMatchList().add(this);
            }
        }
    }

    public void addUser(User user){
        user.setStatus(true);
        userList.add(user);
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
