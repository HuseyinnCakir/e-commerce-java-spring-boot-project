package com.ecommerce.admin.user;

import com.ecommerce.common.entity.Role;
import com.ecommerce.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public List<User> listAll(Model model) {
        List<User> listUsers = service.listAll();
        //model.addAttribute("listUsers",listUsers);
        //return "users
        return listUsers;

    }

    /*@GetMapping("/users/new")
    public String newUser(Model model){
        List<Role> listRoles = service.listRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user",user);
        model.addAttribute("listRoles",listRoles);
        return "user_form"; //redirect user_form.html. this is example.

    }*/

    @PostMapping("/users/save")
    public String saveUser(User user) {
        System.out.println(user);
        service.save(user);
        return "User Created";

    }

    @GetMapping("/users/edit/{id}")
    public User editUser(@PathVariable(name = "id") Integer id) {
        try {
            User user = service.get(id);
            return user;
        } catch (UserNotFoundException ex) {

        }
        return null;

    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id) {
        try {
            service.delete(id);
            return "Deleted";
        } catch (UserNotFoundException ex) {
            return "User not found";
        }

    }
@GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,@PathVariable("status") boolean status) {

        updateUserEnabledStatus(id,status);

        return "User status updated";


}
}
