package dtos;

import entities.User;

import java.util.List;

public class UserDTO {
    String username;
    String password;
    List<String> role;

    public UserDTO(String username, String password, List<String> role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserDTO(User user) {
        if(user.getUserName() != null)
            this.username = user.getUserName();
        this.password = user.getUserPass();
        this.role = user.getRolesAsStrings();
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

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                '}';
    }
}
