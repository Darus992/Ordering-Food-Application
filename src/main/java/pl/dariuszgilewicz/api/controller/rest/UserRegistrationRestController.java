package pl.dariuszgilewicz.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dariuszgilewicz.api.dto.BusinessUserDTO;
import pl.dariuszgilewicz.api.dto.CustomerUserDTO;
import pl.dariuszgilewicz.api.dto.mapper.BusinessUserMapper;
import pl.dariuszgilewicz.api.dto.mapper.CustomerUserMapper;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.security.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Tag(name = "User Registration", description = "Endpoints for managing user registration")
public class UserRegistrationRestController {

    private final UserService userService;
    private final CustomerUserMapper customerUserMapper;
    private final BusinessUserMapper businessUserMapper;


    @Operation(summary = "Create a new customer user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/register-customer")
    public ResponseEntity<?> createCustomerUser(
            @Parameter(description = "Customer user form details")
            @RequestBody CustomerUserDTO customerUserDTO
    ) {
        CustomerRequestForm customerRequestForm = customerUserMapper.mapFromDTOToCustomerRequestForm(customerUserDTO);
        userService.createCustomerUser(customerRequestForm);
        return ResponseEntity.ok("Customer user register successfully.");
    }

    @Operation(
            summary = "Create a new business user",
            requestBody = @RequestBody(content = @Content(mediaType = "multipart/form-data",
                    schema = @Schema(implementation = BusinessUserDTO.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/register-business")
    public ResponseEntity<?> createBusinessUser(
            @Parameter(description = "Business user form details")
            @Valid @RequestBody BusinessUserDTO businessUserDTO
    ) {
        BusinessRequestForm businessRequestForm = businessUserMapper.mapFromDTOToBusinessRequestForm(businessUserDTO);
        userService.createBusinessUser(businessRequestForm);
        return ResponseEntity.ok("Business user register successfully.");
    }
}
