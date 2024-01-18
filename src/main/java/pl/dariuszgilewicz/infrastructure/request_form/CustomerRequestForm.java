package pl.dariuszgilewicz.infrastructure.request_form;

import lombok.*;
import pl.dariuszgilewicz.infrastructure.security.UserRole;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestForm {

    private String username;
    private String userEmail;
    private String userPassword;
    private String customerName;
    private String customerSurname;
    private String customerPhone;
    private String customerAddressCity;
    private String customerAddressDistrict;
    private String customerAddressPostalCode;
    private String customerAddressStreet;
}
