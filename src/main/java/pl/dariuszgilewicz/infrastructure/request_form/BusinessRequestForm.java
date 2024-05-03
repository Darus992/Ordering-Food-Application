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
public class BusinessRequestForm extends RequestForm{

    @NotEmpty(message = "Name is required.")
    private String ownerName;

    @NotEmpty(message = "Surname is required.")
    private String ownerSurname;

    @NotEmpty(message = "Pesel is required.")
    @Size(message = "Pesel number should have 11 numbers.", min = 11, max = 11)
    @Pattern(regexp = "^\\d+$", message = "Pesel must contain only digits.")
    private String ownerPesel;
}
