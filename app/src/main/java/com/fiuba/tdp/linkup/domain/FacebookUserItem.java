package com.fiuba.tdp.linkup.domain;

import java.util.Calendar;

/**
 * Created by alejandro on 9/10/17.
 */

public class FacebookUserItem {
    String id;
    String birthday;
    String gender;
    FacebookEducationItem[] education;
    FacebookLikesItem likes;

    public FacebookUserItem(String id, String birthday, String gender, FacebookEducationItem[] education, FacebookLikesItem likes) {
        this.id = id;
        this.birthday = birthday;
        this.gender = gender;
        this.education = education;
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAge() {
        String[] dob = birthday.split("/");
        return getAgeFromDoB(Integer.parseInt(dob[2]), Integer.parseInt(dob[1]), Integer.parseInt(dob[0]));
    }

    public String getGender() {
        return gender;
    }

    public FacebookEducationItem[] getEducation() {
        return education;
    }

    public FacebookLikesItem getLikes() {
        return likes;
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
}
