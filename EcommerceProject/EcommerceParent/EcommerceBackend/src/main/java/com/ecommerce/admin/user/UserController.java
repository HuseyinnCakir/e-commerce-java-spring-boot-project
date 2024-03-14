package com.ecommerce.admin.user;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.common.entity.Role;
import com.ecommerce.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public List<User> listFirstPage() {
        return listByPage(1);

    }
    @GetMapping("/users/page/{pageNum}")
    public List<User> listByPage(@PathVariable(name = "pageNum") int pageNum){
        Page<User> pageUser = service.listByPage(pageNum);
        List<User> listUsers = pageUser.getContent();
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
    public String saveUser(User user, @RequestParam("image")MultipartFile multipartFile) throws IOException {
        System.out.println(user);
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = service.save(user);
            String uploadDir = "user-photos" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }
        else{
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            service.save(user);
        }


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
