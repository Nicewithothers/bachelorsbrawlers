package hu.szte.brawlers.controller;

import hu.szte.brawlers.model.AuthRequest;
import hu.szte.brawlers.model.RegistrationRequest;
import hu.szte.brawlers.model.dto.ProfileDto;
import hu.szte.brawlers.service.AuthenticationService;
import hu.szte.brawlers.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthenticateController {
    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;


    @PostMapping("/login")
    public ResponseEntity<ProfileDto> generateAuthenticationToken(@RequestBody AuthRequest authenticationRequest) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        String token = authenticationService.authenticate(authenticationRequest);
        headers.add("Authorization", "Bearer " + token);
        return ResponseEntity.ok().headers(headers).body(authenticationService.getUserDataFromToken(token));
    }

    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    public ResponseEntity<String> inValidateAuthenticationToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token)
            throws Exception {
        String[] splittedToken = token.split("Bearer ");
        authenticationService.inValidateToken(splittedToken[1]);
        return new ResponseEntity<>("Signed Out", HttpStatus.OK);
    }

    @GetMapping("/getUserByToken")
    public ResponseEntity<ProfileDto> getUserDataFromToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String[] splittedToken = token.split("Bearer ");
        return ResponseEntity.ok().body(authenticationService.getUserDataFromToken(splittedToken[1]));
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegistrationRequest registrationRequest) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        registrationService.registration(registrationRequest);
        return ResponseEntity.ok().headers(headers).body(registrationRequest.getUserName());
    }


}
