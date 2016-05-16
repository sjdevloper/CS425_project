/*Team member: Danna Liu, Shanshan Jiang, and Zhizheng Li
  Edit by Danna Liu*/
  
CREATE TABLE Users(
  username VARCHAR(25) NOT NULL,
  password VARCHAR(10) NOT NULL ,
  type VARCHAR(15) NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE Faculty(
username VARCHAR(25) NOT NULL,
name VARCHAR(25) NOT NULL,
email VARCHAR(30) NOT NULL,
position VARCHAR(25) NOT NULL ,
yearJoined NUMBER(10) NOT NULL,
experience VARCHAR(255) NOT NULL,
publicProfile VARCHAR(10) NOT NULL,
PRIMARY KEY (username),
FOREIGN KEY (username) REFERENCES Users (username)
);

CREATE TABLE Students(
username VARCHAR(25) NOT NULL,
name VARCHAR(25) NOT NULL,
email VARCHAR(30) NOT NULL,
yearBegan NUMBER(10) NOT NULL,
semesterBegan VARCHAR(10)NOT NULL,
GPA NUMBER(2,1) DEFAULT 0.0, 
totalCreditTaken NUMBER(4) DEFAULT 0,
totalCourseTaken NUMBER(4) DEFAULT 0,
degreeStatus VARCHAR(25) NOT NULL,
degreeType VARCHAR(25) NOT NULL,
publicProfile VARCHAR(10) NOT NULL,
PRIMARY KEY (username),
FOREIGN KEY (username) REFERENCES Users (username)
);

-- three type: courseGroup, otherGroup, Club
CREATE TABLE InterestGroupsClubs(
id VARCHAR(10) NOT NULL,
name VARCHAR(30) NOT NULL,
type VARCHAR(30) NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE Courses(
id VARCHAR(10)NOT NULL,
title VARCHAR(50) NOT NULL,
term VARCHAR(10)NOT NULL,
creditHour NUMBER(2) NOT NULL,
instructor VARCHAR(25) NOT NULL,
groupID VARCHAR(10),
PRIMARY KEY (id),
FOREIGN KEY (instructor) REFERENCES Faculty (username),
FOREIGN KEY (groupID) REFERENCES InterestGroupsClubs (id)
);

CREATE TABLE TAs(
id VARCHAR(10)NOT NULL,
courseID VARCHAR(10) NOT NULL,
TA VARCHAR(25) NOT NULL,
Primary key (courseID, TA),
FOREIGN KEY (courseID) REFERENCES Courses (id),
FOREIGN KEY (TA) REFERENCES Students (username)
);

CREATE TABLE Take(
student VARCHAR(25) NOT NULL,
courseID VARCHAR(10)NOT NULL,
grade VARCHAR(5) NOT NULL,
approved VARCHAR(5) NOT NULL,
totalbouns NUMBER(10) DEFAULT 0,
PRIMARY KEY (student,courseID),
FOREIGN KEY (student) REFERENCES Students (username),
FOREIGN KEY (courseID) REFERENCES Courses (id)
);

CREATE TABLE GroupUsers(
groupUser VARCHAR(25) NOT NULL,
groupPwd NUMBER(10) NOT NULL,
groupID VARCHAR(10) NOT NULL,
username VARCHAR(25) NOT NULL,
isModerator VARCHAR(10) NOT NULL,
PRIMARY KEY (groupUser),
FOREIGN KEY (groupID) REFERENCES InterestGroupsClubs(id),
FOREIGN KEY (username) REFERENCES Users (username)
);

CREATE TABLE DiscussionForums(
id VARCHAR(10) NOT NULL,
title VARCHAR(30) NOT NULL,
groupID VARCHAR(10) NOT NULL,
creator VARCHAR(25) NOT NULL,
bonus NUMBER(3) DEFAULT 0,
approved VARCHAR(5) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (groupID) REFERENCES InterestGroupsClubs(id),
FOREIGN KEY (creator) REFERENCES GroupUsers (groupUser)
);

-- points criteria:  courseGroup 5points, otherGroup 4points, Club 3points
CREATE TABLE Comments(
id VARCHAR(10) NOT NULL,
topic VARCHAR(100) NOT NULL,
message VARCHAR(255) NOT NULL,
author VARCHAR(25) NOT NULL,
forumID VARCHAR(10) NOT NULL,
datePosted DATE NOT NULL,
bonus NUMBER(3) DEFAULT 0,
PRIMARY KEY (id),
FOREIGN KEY (author) REFERENCES GroupUsers (groupUser),
FOREIGN KEY (forumID) REFERENCES DiscussionForums (id)
);

CREATE TABLE Alerts(
id VARCHAR(10) NOT NULL,
alert VARCHAR(255) NOT NULL,
moderator VARCHAR(25) NOT NULL,
groupID VARCHAR(10) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (moderator) REFERENCES GroupUsers (groupUser),
FOREIGN KEY (groupID) REFERENCES InterestGroupsClubs (id)
);

/*insert the data into table to test all procedures,function and triggers*/
INSERT INTO InterestGroupsClubs VALUES('G1', 'CS430_group', 'courseGroup');
INSERT INTO InterestGroupsClubs VALUES('G2', 'Hobby', 'otherGroup');
INSERT INTO InterestGroupsClubs VALUES('G3', 'Bookclub', 'club');

INSERT INTO Courses VALUES('C1','CS430','2016','3','Tom2011','G1');
INSERT INTO Courses VALUES('C2','BIO100','2016','3','Harry2012','');
INSERT INTO Courses VALUES('C3','MATH211','2016','4','Ann2013','');
INSERT INTO Courses VALUES('C4','PHY115','2016','5','Tom2011','');

INSERT INTO GroupUsers VALUES('Tom1','1234','G1','Tom2011', 'yes');
INSERT INTO GroupUsers VALUES('Tom2','1234','G2','Tom2011', 'no');
INSERT INTO GroupUsers VALUES('Ann1','123','G1','Ann2013', 'no');
INSERT INTO GroupUsers VALUES('Eva1','123','G1','Eva2014', 'no');
INSERT INTO GroupUsers VALUES('Eva2','123','G2','Eva2014', 'yes');

INSERT INTO DiscussionForums VALUES('F1','T1','G1','Tom1', 5,'yes');
INSERT INTO DiscussionForums VALUES('F2','T2','G2','Tom2', 4,'yes');
INSERT INTO DiscussionForums VALUES('F3','T3','G1','Ann1', 5,'yes');

INSERT INTO Comments VALUES('Com1','TOPIC1','M1','Tom1','F1',DATE'1983-5-21');
INSERT INTO Comments VALUES('Com2','TOPIC2','M2','Ann1','F1',DATE'2016-1-10');
INSERT INTO Comments VALUES('Com3','TOPIC1','M3','Eva1','F1',DATE'2014-9-23');
INSERT INTO Comments VALUES('Com4','TOPIC3','M4','Eva2','F2',DATE'2014-9-22');
INSERT INTO Comments VALUES('Com5','TOPIC5','M5','Eva2','F1',DATE'2015-9-22');


INSERT INTO Take VALUES('Eva2014','C1','I','yes',DEFAULT);
INSERT INTO Take VALUES('Jeff2015','C2','I','yes',DEFAULT);
INSERT INTO Take VALUES('Eva2014','C2','I','yes',DEFAULT);
INSERT INTO Take VALUES('Tina2016','C3','I','yes',DEFAULT);

