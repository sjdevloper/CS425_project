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
publicEmail VARCHAR(10) NOT NULL,
publicPosition VARCHAR(10) NOT NULL,
publicYearJoined VARCHAR(10) NOT NULL,
publicExperience VARCHAR(10) NOT NULL,
PRIMARY KEY (username),
FOREIGN KEY (username) REFERENCES Users (username)
);

CREATE TABLE researchProjects(
username VARCHAR(25) NOT NULL,
projectID VARCHAR(100) NOT NULL ,
project VARCHAR(255) NOT NULL,
publicProject VARCHAR(10) NOT NULL,
PRIMARY KEY (username, projectID),
FOREIGN KEY (username) REFERENCES Users (username)
);

CREATE TABLE Students(
username VARCHAR(25) NOT NULL,
name VARCHAR(25) NOT NULL,
email VARCHAR(30) NOT NULL,
yearBegan NUMBER(10) NOT NULL,
semesterBegan VARCHAR(10)NOT NULL,
degreeStatus VARCHAR(25) NOT NULL,
degreeType VARCHAR(25) NOT NULL,
publicEmail VARCHAR(10) NOT NULL,
publicYearBegan VARCHAR(10) NOT NULL,
publicSemesterBegan VARCHAR(10) NOT NULL,
publicGPA VARCHAR(10) NOT NULL,
publicDegreeStatus VARCHAR(10) NOT NULL,
publicDegreeType VARCHAR(10) NOT NULL,
PRIMARY KEY (username),
FOREIGN KEY (username) REFERENCES Users (username)
);

CREATE TABLE internships(
username VARCHAR(25) NOT NULL,
internshipID VARCHAR(100) NOT NULL ,
internship VARCHAR(255) NOT NULL,
publicInternship VARCHAR(10) NOT NULL,
PRIMARY KEY (username, internshipID),
FOREIGN KEY (username) REFERENCES Users (username)
);

-- three type: courseGroup, otherGroup, club
CREATE TABLE InterestGroupsClubs(
id VARCHAR(50) NOT NULL,
name VARCHAR(30) NOT NULL,
type VARCHAR(30) NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE GroupUsers(
groupUser VARCHAR(25) NOT NULL,
groupPwd NUMBER(10) NOT NULL,
groupID VARCHAR(50) NOT NULL,
username VARCHAR(25) NOT NULL,
approved VARCHAR(25) NOT NULL,
isModerator VARCHAR(10) NOT NULL,
PRIMARY KEY (groupUser),
FOREIGN KEY (groupID) REFERENCES InterestGroupsClubs(id),
FOREIGN KEY (username) REFERENCES Users (username)
);

CREATE TABLE Courses(
id VARCHAR(50)NOT NULL,
title VARCHAR(50) NOT NULL,
term VARCHAR(10)NOT NULL,
creditHour NUMBER(2) NOT NULL,
instructor VARCHAR(25) NOT NULL,
groupID VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (instructor) REFERENCES Faculty (username),
FOREIGN KEY (groupID) REFERENCES InterestGroupsClubs (id)
);

CREATE TABLE Take(
student VARCHAR(25) NOT NULL,
courseID VARCHAR(50)NOT NULL,
grade VARCHAR(5) NOT NULL,
approved VARCHAR(25) NOT NULL,
totalBonus NUMBER(10) DEFAULT 0,
PRIMARY KEY (student,courseID),
FOREIGN KEY (student) REFERENCES Students (username),
FOREIGN KEY (courseID) REFERENCES Courses (id)
);

CREATE TABLE TAs(
id VARCHAR(50)NOT NULL,
courseID VARCHAR(50) NOT NULL,
courseTA VARCHAR(25) NOT NULL,
Primary key (courseID, courseTA),
FOREIGN KEY (courseID) REFERENCES Courses (id),
FOREIGN KEY (courseTA) REFERENCES Students (username)
);

CREATE TABLE DiscussionForums(
id VARCHAR(50) NOT NULL,
title VARCHAR(30) NOT NULL,
groupID VARCHAR(50) NOT NULL,
creator VARCHAR(25) NOT NULL,
approved VARCHAR(25) NOT NULL,
bonus NUMBER(5) DEFAULT 0,
PRIMARY KEY (id),
FOREIGN KEY (groupID) REFERENCES InterestGroupsClubs(id),
FOREIGN KEY (creator) REFERENCES GroupUsers (groupUser)
);

-- points criteria:  courseGroup 5points, otherGroup 4points, Club 3points
CREATE TABLE Comments(
id INT NOT NULL,
topic VARCHAR(100) NOT NULL,
message VARCHAR(255) NOT NULL,
author VARCHAR(25) NOT NULL,
forumID VARCHAR(50) NOT NULL,
bonus NUMBER(5) DEFAULT 0,
PRIMARY KEY (id),
FOREIGN KEY (author) REFERENCES GroupUsers (groupUser),
FOREIGN KEY (forumID) REFERENCES DiscussionForums (id)
);

CREATE TABLE Alerts(
id VARCHAR(50) NOT NULL,
alert VARCHAR(255) NOT NULL,
moderator VARCHAR(25) NOT NULL,
groupID VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (moderator) REFERENCES GroupUsers (groupUser),
FOREIGN KEY (groupID) REFERENCES InterestGroupsClubs (id)
);

CREATE SEQUENCE comment_id
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

/*insert the data into table to test all procedures,function and triggers*/
INSERT INTO InterestGroupsClubs VALUES('G1', 'CS430_group', 'courseGroup');
INSERT INTO InterestGroupsClubs VALUES('G2', 'Hobby', 'otherGroup');
INSERT INTO InterestGroupsClubs VALUES('G3', 'Bookclub', 'club');

INSERT INTO Courses VALUES('C1','CS430','2016','3','Eva2012','G1');
INSERT INTO Courses VALUES('C2','CS430','2015','3','herry2013','G2');

INSERT INTO Courses VALUES('C2','BIO100','2016','3','Harry2012','');
INSERT INTO Courses VALUES('C3','MATH211','2016','4','Ann2013','');
INSERT INTO Courses VALUES('C4','PHY115','2016','5','Tom2011','');

INSERT INTO GroupUsers VALUES('Tom1','1234','G1','Tom2011', 'no');
INSERT INTO GroupUsers VALUES('anderson1','1234','G1','anderson2011', 'no');
INSERT INTO GroupUsers VALUES('herry1','123','G1','herry2013','no');


INSERT INTO DiscussionForums VALUES('F1','T1','G1','Tom1', 'no', 5);
INSERT INTO DiscussionForums VALUES('F2','T2','G2','Tom1', 'no', 4);
INSERT INTO DiscussionForums VALUES('F3','T3','G1','anderson1', 'no', 5);

INSERT INTO Comments VALUES(comment_id.nextval,'TOPIC1','M1','Tom1','F1',0);
INSERT INTO Comments VALUES(comment_id.nextval,'TOPIC2','M2','Tom1','F1',0);
INSERT INTO Comments VALUES(comment_id.nextval,'TOPIC1','M3','Tom1','F1',0);
INSERT INTO Comments VALUES(comment_id.nextval,'TOPIC1','M3','herry1','F1',0);
INSERT INTO Comments VALUES(comment_id.nextval,'TOPIC3','M4','herry1','F2',0);
INSERT INTO Comments VALUES(comment_id.nextval,'TOPIC5','M5','Tom1','F1',0);
INSERT INTO Comments VALUES(comment_id.nextval,'TOPIC6','M5','Tom1','F1',0);
INSERT INTO Comments VALUES(comment_id.nextval,'TOPIC7','M6','Tom1','F1',0);

INSERT INTO Take VALUES('Tom2011','C1','A','yes',DEFAULT);
INSERT INTO Take VALUES('jeff2012','C1','B','yes',DEFAULT);
INSERT INTO Take VALUES('anderson2011','C1','C','yes',DEFAULT);
INSERT INTO Take VALUES('yiyi2015','C2','C','yes',DEFAULT);
INSERT INTO Take VALUES('Jeff2015','C2','C','yes',DEFAULT);
INSERT INTO Take VALUES('Eva2014','C2','I','yes',DEFAULT);
INSERT INTO Take VALUES('Tina2016','C3','I','yes',DEFAULT);

INSERT INTO TAs VALUES('TA1','C1','Tom2011');

SELECT * FROM comments JOIN DiscussionForums
ON comments.forumID = DiscussionForums.id
where groupid='G1';

/* Queries 2*/
SELECT id, topic, message, author 
FROM comments JOIN groupUsers
ON comments.author = groupUsers.groupUser
WHERE username = 'Tom2011' AND ROWNUM <= 5
ORDER BY id DESC;

/* Queries 3*/
SELECT student, courseID, grade
FROM take JOIN courses
ON take.courseID = courses.id
WHERE courses.instructor = 'Eva2012'


/* Queries 4*/
SELECT instructor, MAX(grade), MIN(grade)
FROM take JOIN courses
ON take.courseID = courses.id
ORDER BY instructor
WHERE title = 'CS430' AND instructor = 'Eva2012';

SELECT instructor, MAX(grade) AS max, MIN(grade) AS min
FROM take JOIN courses
ON take.courseID = courses.id
WHERE title = 'CS430' 
GROUP BY instructor;

/*Queries 6*/
SELECT topic
FROM comments 
WHERE ROWNUM <= 1
GROUP BY topic
ORDER BY count(*) desc;

SELECT * FROM comments JOIN discussionForums
ON ForumID = discussionForums.id
WHERE topic = 'TOPIC1';

/*max comments */
SELECT groupID FROM comments JOIN discussionForums
ON ForumID = discussionForums.id
WHERE ROWNUM <= 1
GROUP BY groupID
ORDER BY count(*) desc;

SELECT * FROM comments JOIN discussionForums
ON ForumID = discussionForums.id
WHERE groupID = 'G1'
ORDER BY comments.id;

/*General processes4*/
SELECT * FROM discussionForums
WHERE groupID = (SELECT groupID FROM groupUsers
                 WHERE groupUser = 'Tom1');
                 
/*General processes2*/
SELECT * FROM discussionForums
WHERE groupID = (SELECT groupID FROM groupUsers
                 WHERE groupUser = 'Tom1');
                 
SELECT comments.id
FROM comments, groupUsers, DiscussionForums
WHERE comments.author = groupUsers.groupUser
AND comments.forumID = DiscussionForums.id
AND username='Tom2011'
ORDER BY ID DESC;
