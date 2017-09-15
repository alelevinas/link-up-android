package com.fiuba.tdp.linkup.domain.facebook;

import java.util.Calendar;

/**
 * Created by alejandro on 9/10/17.
 */

public class FacebookUserItem {
    String id;
    String name;
    String birthday;
    String gender;
    FacebookEducationItem[] education;
    FacebookLikesItem likes;
    FacebookPictureItem picture;

    public FacebookUserItem(String id, String name, String birthday, String gender, FacebookEducationItem[] education, FacebookLikesItem likes, FacebookPictureItem picture) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.education = education;
        this.likes = likes;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge() {
        String[] dob = birthday.split("/");
        return getAgeFromDoB(Integer.parseInt(dob[2]), Integer.parseInt(dob[0]), Integer.parseInt(dob[1]));
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public FacebookEducationItem[] getEducation() {
        return education;
    }

    public void setEducation(FacebookEducationItem[] education) {
        this.education = education;
    }

    public FacebookLikesItem getLikes() {
        return likes;
    }

    public void setLikes(FacebookLikesItem likes) {
        this.likes = likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FacebookPictureItem getPicture() {
        return picture;
    }

    public void setPicture(FacebookPictureItem picture) {
        this.picture = picture;
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

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public void setProfilePicture(String profilePicture) {
        this.picture.data.url = profilePicture;
    }

    public class FacebookEducationItem {
        String id;
        FacebookSchoolItem school;
        String type;

        public FacebookEducationItem(String id, FacebookSchoolItem school, String type) {
            this.id = id;
            this.school = school;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public FacebookSchoolItem getSchool() {
            return school;
        }

        public String getType() {
            return type;
        }
    }

    public class FacebookSchoolItem {
        String id;
        String name;

        public FacebookSchoolItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public class FacebookLikesItem {
        Like[] data;
        FacebookPaging paging;

        public FacebookLikesItem(Like[] data, FacebookPaging paging) {
            this.data = data;
            this.paging = paging;
        }

        public Like[] getData() {
            return data;
        }

        public FacebookPaging getPaging() {
            return paging;
        }
    }

    public class Like {
        String id;
        String name;

        public Like(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public class FacebookPictureItem {
        PictureData data;

        public FacebookPictureItem(PictureData data) {
            this.data = data;
        }

        public PictureData getData() {
            return data;
        }
    }

    public class PictureData {
        String url;

        public PictureData(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
