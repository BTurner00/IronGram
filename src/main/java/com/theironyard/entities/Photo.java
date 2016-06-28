package com.theironyard.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Ben on 6/28/16.
 */
@Entity
@Table(name="photos")
public class Photo {
    @Id
    @GeneratedValue
    int id;

    @ManyToOne
    User sender;

    @ManyToOne
    User recipient;

    @Column(nullable = false)
    String filename;

    @Column(nullable = false)
    LocalDateTime time;

    @Column(nullable = false)
    int delTime;

    public Photo() {
    }

    public Photo(User sender, User recipient, String filename, LocalDateTime time, int delTime) {
        this.sender = sender;
        this.recipient = recipient;
        this.filename = filename;
        this.time = time;
        this.delTime = delTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getDelTime() {
        return delTime;
    }

    public void setDelTime(int delTime) {
        this.delTime = delTime;
    }
}
