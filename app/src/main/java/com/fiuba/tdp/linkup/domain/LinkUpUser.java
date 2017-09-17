package com.fiuba.tdp.linkup.domain;

import java.util.Calendar;

/**
 * Created by alejandro on 9/9/17.
 */

public class LinkUpUser {
    private String id;
    private String name;
    private String gender;
    private String birthday;
    private String picture;

    private String description;

    private LinkUpEducation[] education;
    private LinkUpLike[] likes;
    private LinkUpPicture[] pictures;
    public LinkUpUser(String id, String name, String gender, String birthday, String picture, String description, LinkUpEducation[] education, LinkUpLike[] likes, LinkUpPicture[] pictures) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.picture = picture;
        this.description = description;
        this.education = education;
        this.likes = likes;
        this.pictures = pictures;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAge() {
        String[] dob = birthday.split("/");
        return getAgeFromDoB(Integer.parseInt(dob[2]), Integer.parseInt(dob[0]), Integer.parseInt(dob[1]));  // yankee style
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkUpEducation[] getEducation() {
        return education;
    }

    public LinkUpLike[] getLikes() {
        return likes;
    }

    public LinkUpPicture[] getPictures() {
        return pictures;
    }

    public void setPictures(LinkUpPicture[] pictures) {
        this.pictures = pictures;
    }

    /**
     * Method to extract the user's age from the entered Date of Birth.
     *
     * @param year, month and day String The user's date of birth.
     * @return ageS String The user's age in years based on the supplied DoB.
     */
    private String getAgeFromDoB(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = age;

        return ageInt.toString();
    }

    public class LinkUpEducation {
        String name;
        String type;

        public LinkUpEducation(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

    public class LinkUpLike {
        String name;

        public LinkUpLike(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
