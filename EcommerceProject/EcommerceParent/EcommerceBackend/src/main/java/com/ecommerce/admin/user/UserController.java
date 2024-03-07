package com.ecommerce.admin.user;

import com.ecommerce.common.entity.Role;
import com.ecommerce.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public List<User> listAll(Model model){
        List<User> listUsers = service.listAll();
        //model.addAttribute("listUsers",listUsers);
        //return "users
        return listUsers;

    }

    /*@GetMapping("/users")
    public String newUser(Model model){
        List<Role> listRoles = service.listRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user",user);
        model.addAttribute("listRoles",listRoles);
        return "user_form"; //redirect user_form.html. this is example.

    }*/

    @PostMapping("/users")
    public String saveUser(@RequestBody User user){
        System.out.println(user);
        service.save(user);
        return "User Created";

    }
}
