package in.codingstreams.etuserauthservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import in.codingstreams.etuserauthservice.enums.UpdateRequestType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private UpdateRequestType type;
}
