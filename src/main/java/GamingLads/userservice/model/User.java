package GamingLads.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor @NoArgsConstructor
@Entity(name = "User")
@Table(name = "user")
public class User {

    @Getter @Setter @GeneratedValue @Id
    private int userId;

    @Getter @Setter @Column
    private String username;

    @Getter @Setter @Column
    private String password;

    @Getter @Setter
    private String token;

}
