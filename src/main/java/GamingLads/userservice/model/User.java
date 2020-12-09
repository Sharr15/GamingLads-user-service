package GamingLads.userservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Entity(name = "User")
@Table(name = "user")
public class User {

    @Getter @Setter @GeneratedValue(strategy = GenerationType.AUTO) @Id
    private int userId;

    @Getter @Setter @Column
    private String username;

    @Getter @Setter @Column
    private String password;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    @Getter @Setter @Column
    private List<Role> roles;

    @Getter @Setter @Column
    private boolean active;

}
