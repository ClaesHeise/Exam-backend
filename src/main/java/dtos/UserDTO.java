package dtos;

import entities.Match;
import entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String password;

    private String phone;

    private String email;
    private List<String> role;

    private List<MatchDTO> match;
    //Long id, String userName, String userPass, String phone, String email

//    public UserDTO(String username, String password, String phone, String email, List<String> role, List<MatchDTO> match) {
//        this.username = username;
//        this.password = password;
//        this.phone = phone;
//        this.email = email;
//        this.role = role;
//        this.match = match;
//    }

    public UserDTO(String username, String password, String phone, String email, List<String> role) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }

    public UserDTO(User user) {
        if(user.getUserName() != null) {
            this.id = user.getId();
            this.username = user.getUserName();
            this.password = user.getUserPass();
            this.phone = user.getPhone();
            this.email = user.getEmail();
            this.role = user.getRolesAsStrings();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<MatchDTO> getMatch() {
        return match;
    }

    public void setMatch(List<MatchDTO> match) {
        this.match = match;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                '}';
    }
}
