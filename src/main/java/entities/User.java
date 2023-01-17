package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_name", length = 25)
  private String userName;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "user_pass")
  private String userPass;

  //Phone, Email, Status
  @Column(name = "phone")
  private String phone;

  @Column(name = "email")
  private String email;

  @Column(name = "status")
  private boolean status;

  @JoinTable(name = "user_roles", joinColumns = {
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
    @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
  @ManyToMany
  private List<Role> roleList = new ArrayList<>();

  @JoinTable(name = "user_matches", joinColumns = {
          @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
          @JoinColumn(name = "match_id", referencedColumnName = "id")})
  @ManyToMany
  private List<Match> matchList = new ArrayList<>();

  public List<String> getRolesAsStrings() {
    if (roleList.isEmpty()) {
      return null;
    }
    List<String> rolesAsStrings = new ArrayList<>();
    roleList.forEach((role) -> {
        rolesAsStrings.add(role.getRoleName());
      });
    return rolesAsStrings;
  }

  private void stat(){
    if(matchList != null && matchList.size() > 0){
      this.status = true;
    } else {
      this.status = false;
    }
  }

  public User() {}

  // Encrypts password with Bcrypt and a personal salt
  public User(String userName, String userPass, String phone, String email) {
    this.userName = userName;
    if(userPass.charAt(0) != '$'){
      this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());
    }
    else {
      this.userPass = userPass;
    }
    this.phone = phone;
    this.email = email;
    stat();
  }

  public User(Long id, String userName, String userPass, String phone, String email) {
    this.id = id;
    this.userName = userName;
    this.userPass = userPass;
    this.phone = phone;
    this.email = email;
    stat();
  }

  // Decrypts and checks given password against database/entity password, using B-crypt
  public boolean verifyPassword(String pw){
    return(BCrypt.checkpw(pw, userPass));
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPass() {
    return this.userPass;
  }

  // Encrypts password with salt here
  public void setUserPass(String password) {
    this.userPass = BCrypt.hashpw(password, BCrypt.gensalt());
  }

  public List<Role> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<Role> roleList) {
    this.roleList = roleList;
  }

  public void addRole(Role userRole) {
    roleList.add(userRole);
  }

  public Long getId() {
    return id;
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

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public List<Match> getMatchList() {
    return matchList;
  }

  public void setMatchList(List<Match> matchList) {
    this.matchList = matchList;
  }

  public void addMatch(Match match){
    this.status = true;
    matchList.add(match);
    System.out.println(this.userName+" : "+this.userPass);
  }
  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", userPass='" + userPass + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", status=" + status +
            ", roleList=" + roleList +
            '}';
  }
}
