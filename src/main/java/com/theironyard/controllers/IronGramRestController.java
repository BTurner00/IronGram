package com.theironyard.controllers;

import com.theironyard.entities.Photo;
import com.theironyard.entities.User;
import com.theironyard.services.PhotoRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.utils.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Ben on 6/28/16.
 */
@RestController
public class IronGramRestController {
    @Autowired
    UserRepository users;
    @Autowired
    PhotoRepository photos;

    @RequestMapping(path="/login", method= RequestMethod.POST)
    public User login(@RequestBody User user, HttpSession session) throws Exception {
        User userFromDB = users.findFirstByname(user.getName());
        if (userFromDB == null) {
            user.setPassword(PasswordStorage.createHash(user.getPassword()));
            users.save(user);
        }
        else if(!PasswordStorage.verifyPassword(user.getPassword(), userFromDB.getPassword())) {
            throw new Exception("Wrong Password!");
        }

        session.setAttribute("username", user.getName());
        return user;
    }

    @RequestMapping(path="logout", method=RequestMethod.POST)
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @RequestMapping(path="/photos", method=RequestMethod.GET)
    public Iterable<Photo> getPhotos(HttpSession session) {

        Iterable<Photo> delPhotos = photos.findAll();
        for (Photo photo : delPhotos) {
            if (photo.getTime().isBefore((LocalDateTime.now().plusSeconds(-photo.getDelTime())))) {
                photos.delete(photo);
                File file = new File("/public/" + photo.getFilename());
                file.delete();
            }
        }


        String username = (String) session.getAttribute("username");
        User user = users.findFirstByname(username);
        return photos.findByRecipient(user);
    }

    @RequestMapping(path="/public-photos", method=RequestMethod.GET)
    public Iterable<Photo> getPublicPhotos(String username) {
        Iterable<Photo> publicPhotos = photos.findByPublicPhotoTrue();
        ArrayList<Photo> userPublicPhotos = new ArrayList<>();
        for(Photo photo: publicPhotos) {
            if(photo.getSender().getName().equals(username)) {
                userPublicPhotos.add(photo);
            }
        }
        return userPublicPhotos;
    }
}
