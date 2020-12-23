package gaminglads.userservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity(name = "Role")
@Table(name = "role")
public class Role {

    public Role(int id, String name)
    {
        this.roleId = id;
        this.name = name;
    }

    @Getter @Setter @Id
    private int roleId;

    @Getter @Setter @Column
    private String name;

    @ManyToMany(mappedBy = "roles")
    @Getter @Setter
    private List<User> users;
}
