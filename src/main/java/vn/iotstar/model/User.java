package vn.iotstar.model;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users", schema = "dbo")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "fullname", nullable = false, length = 255)
    private String fullname;

    // Dấu ngoặc kép giúp Hibernate quote tên cột "password" cho SQL Server.
    @Column(name = "\"password\"", nullable = false, length = 255)
    private String password;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "roleid", nullable = false)
    private int roleid;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "createdDate", nullable = false)
    private Timestamp createdDate;

    @Column(name = "isActive", nullable = false)
    private boolean active = true;

    @Column(name = "activationCodeHash", length = 255)
    private String activationCodeHash;

    @Column(name = "activationExpiresAt")
    private Timestamp activationExpiresAt;

    @Column(name = "resetCodeHash", length = 255)
    private String resetCodeHash;

    @Column(name = "resetExpiresAt")
    private Timestamp resetExpiresAt;

    @Column(name = "profilePendingEmail", length = 255)
    private String profilePendingEmail;

    @Column(name = "profileEmailOtpHash", length = 255)
    private String profileEmailOtpHash;

    @Column(name = "profileEmailOtpExpiresAt")
    private Timestamp profileEmailOtpExpiresAt;

    @OneToMany(mappedBy = "user")
    private List<Video> videos = new ArrayList<>();

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActivationCodeHash() {
        return activationCodeHash;
    }

    public void setActivationCodeHash(String activationCodeHash) {
        this.activationCodeHash = activationCodeHash;
    }

    public Timestamp getActivationExpiresAt() {
        return activationExpiresAt;
    }

    public void setActivationExpiresAt(Timestamp activationExpiresAt) {
        this.activationExpiresAt = activationExpiresAt;
    }

    public String getResetCodeHash() {
        return resetCodeHash;
    }

    public void setResetCodeHash(String resetCodeHash) {
        this.resetCodeHash = resetCodeHash;
    }

    public Timestamp getResetExpiresAt() {
        return resetExpiresAt;
    }

    public void setResetExpiresAt(Timestamp resetExpiresAt) {
        this.resetExpiresAt = resetExpiresAt;
    }

    public String getProfilePendingEmail() {
        return profilePendingEmail;
    }

    public void setProfilePendingEmail(String profilePendingEmail) {
        this.profilePendingEmail = profilePendingEmail;
    }

    public String getProfileEmailOtpHash() {
        return profileEmailOtpHash;
    }

    public void setProfileEmailOtpHash(String profileEmailOtpHash) {
        this.profileEmailOtpHash = profileEmailOtpHash;
    }

    public Timestamp getProfileEmailOtpExpiresAt() {
        return profileEmailOtpExpiresAt;
    }

    public void setProfileEmailOtpExpiresAt(Timestamp profileEmailOtpExpiresAt) {
        this.profileEmailOtpExpiresAt = profileEmailOtpExpiresAt;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
