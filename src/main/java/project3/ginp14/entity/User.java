package project3.ginp14.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    @NotBlank(message = "This field cannot be blank")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "This field cannot be blank")
    @Size(min = 5, message = "Password is too short")
    private String password;

    @NotBlank(message = "This field cannot be blank")
    @Column(name = "full_name")
    private String fullname;


    @Column(name = "gender")
    @NotNull(message = "Please select gender")
    private int gender;

    @Column(name = "address")
    private String address;


    @Column(name = "email")
    @NotBlank(message = "This field cannot be blank")
    @Email(message = "This is not a valid email")
    private String email;

    @Column(name = "dob")
    private String DOB;


    @Column(name = "telephone")
    @NotBlank(message = "This field cannot be blank")
    @Size(max = 11,message = "Phone number must be less than 11 digits")
    private String telephone;

    @OneToOne
    @JoinColumn(name="role_id")
    private Role role;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp created_at;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updated_at;

    public User(@NotBlank(message = "This field cannot be blank") String username, @NotBlank(message = "This field cannot be blank") @Size(min = 5, message = "Password is too short") String password, @NotBlank(message = "This field cannot be blank") String fullname, @NotNull(message = "Please select gender") int gender, String address, @NotBlank(message = "This field cannot be blank") @Email(message = "This is not a valid email") String email, String DOB, @NotBlank(message = "This field cannot be blank") @Size(max = 11, message = "Phone number must be less than 11 digits") String telephone, Role role) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.DOB = DOB;
        this.telephone = telephone;
        this.role = role;
    }

    public User(@NotBlank(message = "This field cannot be blank") String username, @NotBlank(message = "This field cannot be blank") @Size(min = 5, message = "Password is too short") String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
