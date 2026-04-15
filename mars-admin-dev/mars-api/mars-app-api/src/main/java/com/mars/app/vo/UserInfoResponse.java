package com.mars.app.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInfoResponse {
    private String nickname;
    private String avatar;
    private String bio;
    private String occupation;
    private String gender;
    private LocalDate birthDate;
    private String height;
    private String graduationSchool;
    private String highestDegree;
    private String degreeType;
    private String hobbies;
    private String preferences;
    private String currentLocation;
    private String meetingExpectation;
    private String relationshipGoal;
    private String maritalStatus;
    private String hometown;
    private String imgList;
    private String phone;
    private String authStatus;
    private String status;
}
