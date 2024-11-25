package com.example.projectfyp.Files;

import java.util.ArrayList;

public class CategorizedAnnouncements {
    private String subject;
    private ArrayList<Announcement> announcements;

    public CategorizedAnnouncements(String subject) {
        this.subject = subject;
        this.announcements = new ArrayList<>();
    }

    public String getSubject() {
        return subject;
    }

    public ArrayList<Announcement> getAnnouncements() {
        return announcements;
    }

    public void addAnnouncement(Announcement announcement) {
        announcements.add(announcement);
    }
}