package dtos;

import entities.Match;
import entities.User;

import java.util.ArrayList;
import java.util.List;

public class MatchDTO {
    private Long id;

    private String opponentTeam;
    private String judge;

    private String type;

    private boolean inDoors;
    private List<UserDTO> users;

    private LocationDTO location;

    public MatchDTO(Long id, String opponentTeam, String judge, String type, boolean inDoors, List<UserDTO> users, LocationDTO location) {
        this.id = id;
        this.opponentTeam = opponentTeam;
        this.judge = judge;
        this.type = type;
        this.inDoors = inDoors;
        if(users != null) {
            this.users = users;
        }
        if(location != null) {
            this.location = location;
        }
    }

    public MatchDTO(Match match) {
        if(match.getLocation() != null){
            this.id = match.getId();
            this.opponentTeam = match.getOpponentTeam();
            this.judge = match.getJudge();
            this.type = match.getType();
            this.inDoors = match.isInDoors();
            if(match.getUserList() != null) {
                this.users = new ArrayList<>();
                for (User u : match.getUserList()) {
                    this.users.add(new UserDTO(u));
                }
            }
            System.out.println(match.getLocation().getId());
            if(match.getLocation() != null) {
                this.location = new LocationDTO(match.getLocation());
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "MatchDTO{" +
                "judge='" + judge + '\'' +
                ", location=" + location.toString() +
                '}';
    }
}
