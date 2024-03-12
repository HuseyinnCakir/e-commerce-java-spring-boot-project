package com.ecommerce.admin.user;

import com.ecommerce.common.entity.Role;
import com.ecommerce.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public List<User> listAll(){
        return (List<User>) userRepository.findAll();

    }
    public List<Role> listRoles(){
        return (List<Role>) roleRepository.findAll();
    }
    public void save(User user){
        encodePassword(user);

        userRepository.save(user);
    }

    public void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(String email){
        User user = userRepository.getUserByEmail(email);
        return user == null;
    }

}
