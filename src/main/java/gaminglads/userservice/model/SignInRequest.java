package gaminglads.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;

}
