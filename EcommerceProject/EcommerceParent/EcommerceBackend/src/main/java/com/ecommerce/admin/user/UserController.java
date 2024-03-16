package com.ecommerce.admin.user;

import com.ecommerce.admin.FileUploadUtil;

import com.ecommerce.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;

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
        return listByPage(1,"firstName","asc",null);

    }
    @GetMapping("/users/page/{pageNum}")
    public List<User> listByPage(@PathVariable(name = "pageNum") int pageNum,
                                 @Param("sortField") String sortField,
                                 @Param("sortDir")String sortDir,
                                 @Param("keyword")String keyword){

        Page<User> pageUser = service.listByPage(pageNum,sortField,sortDir,keyword);
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

        return getRedirectUrlToAffectedUser(user);


    }

    private static String getRedirectUrlToAffectedUser(User user) {
        String firstParfOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id?sortDir=asc&keyword=" + firstParfOfEmail;
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

@GetMapping("/users/export/csv")
        public void exportToCSV(HttpServletResponse httpServletResponse) throws IOException {
        List<User> listUsers = service.listAll();
            UserCsvExporter exporter = new UserCsvExporter();
            exporter.export(listUsers,httpServletResponse);
    }
    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<User> listUsers = service.listAll();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers,httpServletResponse);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse httpServletResponse) throws IOException {
        List<User> listUsers = service.listAll();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(listUsers,httpServletResponse);
    }
}
