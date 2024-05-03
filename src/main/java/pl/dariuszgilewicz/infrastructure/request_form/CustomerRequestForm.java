package pl.dariuszgilewicz.infrastructure.request_form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestForm extends RequestForm{

    @NotEmpty(message = "Name is required.")
    private String customerName;

    @NotEmpty(message = "Surname is required.")
    private String customerSurname;

    @NotEmpty(message = "Phone is required.")
    @Pattern(regexp = "^\\d+$", message = "Phone number must contain only digits.")
    @Size(message = "The phone number size cannot be larger or smaller than 9.", min = 9, max = 9)
    private String customerPhone;

    @NotEmpty(message = "City is required.")
    private String customerAddressCity;

    @NotEmpty(message = "District is required.")
    private String customerAddressDistrict;

    @NotEmpty(message = "Postal code is required.")
    private String customerAddressPostalCode;

    @NotEmpty(message = "Street is required.")
    private String customerAddressStreet;
}
