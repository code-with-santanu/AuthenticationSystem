package com.santanu.gossipZ.service;

import com.santanu.gossipZ.entity.UserEntity;
import com.santanu.gossipZ.model.User;
import com.santanu.gossipZ.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

   private UserRepository userRepository;
   private PasswordEncoder passwordEncoder;

   @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUser() {
        // retrieve the list of user-entity
        List<UserEntity> userEntityList = userRepository.findAll();

        // convert the user-entity list to a user list
        List<User> userList = userEntityList
                .stream()
                .map(userEntity -> new User(
                        userEntity.getId(),
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        userEntity.getEmail(),
                        userEntity.getPassword()
                )).collect(Collectors.toList());

        return userList;
    }

    @Override
    public User getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).get(); // without get() method , we will get the instance only

        //convert the user-entity to user model data
        User user = new User();
        BeanUtils.copyProperties(userEntity,user);

        return user;
    }

    @Override
    @Transactional
    public User addUser(User newUser) {

        // create a user-entity object
        UserEntity userEntity = new UserEntity();

        // encoding the password using bcrypt
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // copy the User properties to the user-entity
        BeanUtils.copyProperties(newUser,userEntity);

        //In case they pass an id in JSON... set id to 0; this is to force a save of new item instead of update
        userEntity.setId(0);

        // save the new user using repository and user-entity
        userEntity=userRepository.save(userEntity);

        BeanUtils.copyProperties(userEntity,newUser);

        return newUser;
    }


    @Override
    @Transactional
    public User updateUser(Long userId,User newUser)
    {
        UserEntity userEntity = userRepository.findById(userId).get();

        userRepository.save(userEntity);
        return newUser;
    }


    @Override
    @Transactional
    public boolean deleteUser(Long userId) {

       UserEntity theUser = userRepository.findById(userId).get();

       if(theUser==null)
           throw new RuntimeException("User not found ---> "+userId);
        userRepository.deleteById(userId);

        return true;
    }
}
