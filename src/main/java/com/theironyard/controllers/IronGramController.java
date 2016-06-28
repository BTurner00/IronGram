package com.theironyard.controllers;

import com.theironyard.entities.Photo;
import com.theironyard.entities.User;
import com.theironyard.services.PhotoRepository;
import com.theironyard.services.UserRepository;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Created by Ben on 6/28/16.
 */
@Controller
public class IronGramController {

    @Autowired
    UserRepository users;
    @Autowired
    PhotoRepository photos;

    @PostConstruct
    public void init() throws SQLException {
        Server.createWebServer().start();
    }

    @RequestMapping(path="/upload", method= RequestMethod.POST)
    public String upload(MultipartFile file, String receiver, HttpSession session, Integer delTime) throws Exception {
        String username = (String) session.getAttribute("username");
        User sender = users.findFirstByname(username);
        User rec = users.findFirstByname(receiver);

        if (sender ==null|| receiver == null) {
            throw new Exception("Can't find sender or receiver!");
        }

        File dir = new File("public/photos");
        dir.mkdirs();

        File photoFile = File.createTempFile("photo", file.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(photoFile);

        if (file.getContentType().contains("image")) {
            fos.write(file.getBytes());
            Photo photo = new Photo(sender, rec, photoFile.getName(), LocalDateTime.now(), delTime);
            photos.save(photo);
        } else {
            throw new Exception("File selected is not an image!");
        }



        return "redirect:/";
    }


}
