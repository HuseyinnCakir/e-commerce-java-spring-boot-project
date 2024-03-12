package com.ecommerce.admin.user;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    private  UserService service;

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(@Param("email") String email){
    return service.isEmailUnique(email) ? "OK" : "Duplicated";
    }
}
