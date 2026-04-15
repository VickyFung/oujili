package com.mars.app.vo;

import com.mars.common.enums.user.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInfoRequest {
    private String nickname;
    private String avatar;
    private String bio;
    private String occupation;
    private Integer gender;
    private LocalDate birthDate;
    private String height;
    private String graduationSchool;
    private HighestEducationEnum highestDegree;
    private EducationalTypeEnum degreeType;
    private String hobbies;
    private String preferences;
    private String currentLocation;
    private MeetingExpectationEnum meetingExpectation;
    private RelationshipGoalEnum relationshipGoal;
    private MaritalStatusEnum maritalStatus;
    private String hometown;
    private String imgList;
    private String idCard;
}
