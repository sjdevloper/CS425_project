/*Team member: Danna Liu, Shanshan Jiang, and Zhizheng Li
  Edit by Danna Liu*/

/* EXECUTE COMMAND FOR TESTING*/
EXECUTE FacultyReg('Tom', 'tom@gmail.com','prof',2011, 'n/a','yes');
EXECUTE FacultyReg('Harry', 'harry@gmail.com','prof',2012, 'n/a','yes');
EXECUTE FacultyReg('Ann', 'ann@gmail.com','prof',2013, 'n/a','no');

EXECUTE StudentReg('Eva', 'eva@gmail.com', 2014, 'spring','u2', 'bs','no');
EXECUTE StudentReg('Jeff', 'jeff@gmail.com', 2015, 'spring', 'u5', 'bs','yes');
EXECUTE StudentReg('Tina', 'tina@gmail.com', 2016, 'spring', 'master', 'bs','yes');

EXECUTE CreateCoursesGroup;

EXECUTE ViewStu('Jeff'); /*publicProfile:yes*/
EXECUTE ViewStu('Eva'); /*publicProfile:no*/

EXECUTE popTopic('T1');

UPDATE Take 
SET grade = 'A'
WHERE student = 'Eva2014' AND courseID = 'C1';

INSERT INTO Comments VALUES('Com6','TOPIC5','M6','Eva1','F1',DATE'2015-9-22');

EXECUTE addTA('C1','Eva2014');
EXECUTE addTA('C3','Eva2014');

EXECUTE POSTCOMMENT ('TOPIC6','M7','Ann1','F1');
EXECUTE POSTCOMMENT ('TOPIC6','M7','Ann1','F2'); /*Ann1 is not memeber of F2*/

/*The follwoing are all procedures, functions, and triggers*/
/*question 1*/
CREATE OR REPLACE FUNCTION FacultyUsername(
  name IN Faculty.name%TYPE,
  yearJoined IN Faculty.yearJoined%TYPE
)
RETURN string IS 
BEGIN
 RETURN name||TO_CHAR(yearJoined,'FM9999');
END;

CREATE OR REPLACE FUNCTION StudentUsername(
  name IN Students.name%TYPE,
  yearBegan IN Students.yearBegan%TYPE
)
RETURN string IS 
BEGIN
 RETURN name||TO_CHAR(yearBegan,'FM9999');
END;

CREATE OR REPLACE FUNCTION CreatePwd
RETURN string IS 
BEGIN
 RETURN dbms_random.string('U', 5);
END;

/*question 2*/
create or replace PROCEDURE FacultyReg(
  name IN Faculty.name%TYPE,
  email IN Faculty.email%TYPE,
  position IN Faculty.position%TYPE,
  year IN Faculty.yearJoined%TYPE,
  experience IN Faculty.experience%TYPE,
  publicProfile IN Faculty.publicProfile%TYPE 
)AS
 BEGIN 
    INSERT INTO Users
    VALUES(FacultyUsername(name,year),CreatePwd,'Faculty');
    INSERT INTO Faculty 
    VALUES(name||TO_CHAR(year,'FM9999'), name, email, position, year, experience, publicProfile);   
 END;
 
create or replace PROCEDURE StudentReg(
  name IN Students.name%TYPE,
  email IN Students.email%TYPE,
  yearBegan IN Students.yearBegan%TYPE,
  semesterBegan IN Students.semesterBegan%TYPE,
  degreeStatus IN Students.degreeStatus%TYPE,
  degreeType IN Students.degreeType%TYPE,
  publicProfile IN Students.publicProfile%TYPE 
)AS
    user Students.username%TYPE;
 BEGIN 
    user := StudentUsername(name, yearBegan);
    INSERT INTO Users
    VALUES(user,CreatePwd,'Student');
    INSERT INTO Students
    VALUES(user, name, email, yearBegan, semesterBegan,DEFAULT, DEFAULT, DEFAULT, 
    degreeStatus, degreeType, publicProfile);   
 END;
 
/*question 3*/
create or replace PROCEDURE CreateCoursesGroup
AS
  course_id Courses.id%TYPE;
  group_id Courses.groupID%TYPE;
  title Courses.title%TYPE;
  term Courses.term%TYPE;
  CURSOR c IS 
    SELECT id, title, term, groupID FROM courses;
BEGIN 
  OPEN c;
  LOOP
      FETCH c INTO course_id, title, term, group_id;
      EXIT WHEN c%NOTFOUND;
      IF group_id is null THEN
        group_id := dbms_random.string('U', 3);
        INSERT INTO InterestGroupsClubs
        VALUES(group_id, title||'_group' , 'courseGroup'); 
        
        UPDATE Courses
        SET groupID = group_id
        WHERE id = course_id; 
        
      END IF;
  END LOOP;
  CLOSE c;
END;
 
/*question 4*/
create or replace PROCEDURE viewStu(
  input IN Students.name%TYPE
)
AS
  CURSOR c IS
  SELECT * FROM Students 
  WHERE name = input;
  some_student c%ROWTYPE;
BEGIN 
  OPEN c;
  LOOP
      FETCH c INTO some_student;
      EXIT WHEN c%NOTFOUND;
      IF lower(some_student.publicProfile) = 'yes' THEN
        dbms_output.put_line('Here is the basic information about ' ||some_student.name);
        dbms_output.put_line('username:      ' ||some_student.username);
        dbms_output.put_line('email:         ' ||some_student.email);
        dbms_output.put_line('yearBegan:     ' ||some_student.yearBegan);
        dbms_output.put_line('semesterBegan: ' ||some_student.semesterBegan);
        dbms_output.put_line('GPA:           ' ||some_student.GPA);
        dbms_output.put_line('email:         ' ||some_student.email);
        dbms_output.put_line('degreeStatus:  ' ||some_student.degreeStatus);
        dbms_output.put_line('degreeType:    ' ||some_student.degreeType);
      ELSE
        dbms_output.put_line('Profile hidden.');
      END IF;
  END LOOP;
  CLOSE c;
END;

/*question 5*/
create or replace PROCEDURE popTopic(
  input IN DiscussionForums.title%TYPE
)
AS
  CURSOR c IS
  SELECT topic FROM Comments
  WHERE forumID = (SELECT id FROM DiscussionForums 
                  WHERE title = input)
  GROUP BY topic
  ORDER BY count(*) desc; 
  pop_topic c%ROWTYPE;
  
BEGIN
OPEN c;
  FOR i IN 1..3 LOOP
      FETCH c INTO pop_topic;
      EXIT WHEN c%NOTFOUND;
      dbms_output.put_line(pop_topic.topic);
  END LOOP;
CLOSE c;
END;

/*question 6 */  
create or replace TRIGGER GPAupdate
AFTER INSERT OR UPDATE ON Take
FOR EACH ROW
  DECLARE   
      currentGPA Students.GPA%TYPE;
      currentCredit Courses.creditHour%TYPE;
      newGPA students.GPA%TYPE := 0;
      credit_hour Courses.creditHour%TYPE;
      CreditTaken Courses.creditHour%TYPE;
      CourseTaken Students.totalCourseTaken%TYPE;
      
  BEGIN
      SELECT GPA, totalCreditTaken, totalCourseTaken
      INTO currentGPA, CreditTaken, CourseTaken
      FROM students
      WHERE username = :NEW.student;
      
      SELECT creditHour INTO credit_hour
      FROM courses
      WHERE id = :NEW.courseID;
      
     IF CourseTaken = 1 AND upper(:OLD.grade) <> 'I' THEN
          currentGPA := 0;
          CreditTaken:= CreditTaken;
          CourseTaken:= CourseTaken;
      ELSIF CourseTaken = 1 AND upper(:OLD.grade) = 'I' THEN
         currentGPA := currentGPA;
         currentCredit := CreditTaken;
         CreditTaken := CreditTaken + credit_hour;
         CourseTaken := CourseTaken + 1;
      ELSE
        IF upper(:OLD.grade) = 'A' THEN
          currentGPA := (currentGPA * CreditTaken - credit_hour * 4)/(CreditTaken - credit_hour);
        ELSIF upper(:OLD.grade) = 'B' THEN
          currentGPA := (currentGPA * CreditTaken - credit_hour * 3)/(CreditTaken - credit_hour);
        ELSIF upper(:OLD.grade) = 'C' THEN
          currentGPA := (currentGPA * CreditTaken - credit_hour * 2)/(CreditTaken - credit_hour);
        ELSIF upper(:OLD.grade) = 'D' THEN
          currentGPA := (currentGPA * CreditTaken - credit_hour * 1)/(CreditTaken - credit_hour);
        ELSE 
          currentGPA := currentGPA;
          currentCredit := CreditTaken;
          CreditTaken := CreditTaken + credit_hour;
          CourseTaken:= CourseTaken + 1;  
        END IF;
        currentCredit := CreditTaken - credit_hour;
        CourseTaken:= CourseTaken;
      END IF;
      
      IF upper(:NEW.grade) = 'A' THEN
         newGPA := (currentGPA * currentCredit + credit_hour * 4)/(currentCredit + credit_hour);
      ELSIF upper(:NEW.grade) = 'B' THEN
         newGPA := (currentGPA* currentCredit + credit_hour * 3)/(currentCredit + credit_hour);
      ELSIF upper(:NEW.grade) = 'C' THEN
         newGPA := (currentGPA* currentCredit + credit_hour * 2)/(currentCredit + credit_hour);
      ELSIF upper(:NEW.grade) = 'D' THEN
         newGPA := (currentGPA* currentCredit+ credit_hour * 1)/(currentCredit + credit_hour);
      ELSE 
         newGPA := currentGPA;
         CreditTaken := CreditTaken - credit_hour;
         CourseTaken:= CourseTaken - 1; 
      END IF;
      
      UPDATE students
      SET  GPA = newGPA, totalCreditTaken = CreditTaken, totalCourseTaken = CourseTaken
      WHERE username = :NEW.student;
  END;
  
/*question 7*/
create or replace TRIGGER addBonus
AFTER INSERT ON Comments 
FOR EACH ROW
DECLARE 
  group_type InterestGroupsClubs.type%TYPE;
  group_id InterestGroupsClubs.id%TYPE;
BEGIN
  SELECT groupID INTO group_id
  FROM DiscussionForums 
  WHERE id = :New.forumID;
  
  SELECT type INTO group_type 
  FROM InterestGroupsClubs
  WHERE id = group_id;
  
  IF LOWER(group_type) = 'coursegroup' THEN
    UPDATE Take 
    SET TotalBouns = TotalBouns + 5
    WHERE student = (SELECT username FROM GroupUsers
                      WHERE groupUser = :New.author)
          AND courseID = (SELECT id FROM Courses
                          WHERE groupID = group_id);
  END IF;
END;

/*question 8*/
create or replace PROCEDURE addTA(
  course_id IN TAs.courseID%TYPE,
  course_TA IN TAs.TA%TYPE
)
AS
BEGIN 
  INSERT INTO TAs
  VALUES(dbms_random.string('L', 5),course_id,course_TA);
END;

create or replace TRIGGER assignTA
BEFORE INSERT OR UPDATE ON TAs
FOR EACH ROW
DECLARE
  CURSOR c IS
    SELECT student FROM Take
    WHERE Take.courseID = :NEW.courseID;
  stu_enrolled c%ROWTYPE;
BEGIN
 OPEN c;
   LOOP
      FETCH c INTO stu_enrolled;
      EXIT WHEN c%NOTFOUND;
      IF :NEW.TA = stu_enrolled.student THEN
        raise_application_error (-20001, 'Sorry, the student ENROLLED in this course is not allowed to be TA.');
      END IF;
   END LOOP;
 CLOSE c; 
END;

/*question9*/
create or replace PROCEDURE POSTCOMMENT(
  T IN Comments.topic%TYPE,
  M IN Comments.message%TYPE,
  A IN Comments.author%TYPE,
  F IN Comments.forumID%TYPE
)
AS
  group_id  GroupUsers.groupID%TYPE;
  moderator GroupUsers.groupUser%TYPE;
  
  CURSOR c IS
    SELECT groupUser
    FROM GroupUsers
    WHERE groupID =(SELECT groupID 
                    FROM DiscussionForums
                    WHERE id = F);
  game c%ROWTYPE;
  
BEGIN
  INSERT INTO Comments 
  VALUES (dbms_random.string('U', 5), T, M, A, F, sysdate); 
  
  SELECT groupID INTO group_id
  FROM DiscussionForums
  WHERE id = F;
  
  OPEN c;
  LOOP
      FETCH c INTO game;
      EXIT WHEN c%NOTFOUND;
      IF game.groupUser = A THEN
        dbms_output.put_line('Thanks for your post.');
        GOTO stop_PROCEDURE;
      ELSE
        CONTINUE;
      END IF;
    END LOOP;
  CLOSE c;
  
  SELECT groupUser INTO moderator
  FROM GroupUsers
  WHERE groupID = group_id AND LOWER(isModerator) = 'yes'
  AND ROWNUM = 1;
  
  INSERT INTO Alerts 
  VALUES (dbms_random.string('U', 5), 'A non member tries to post comment on group' || group_id, moderator, group_id);    

<<stop_PROCEDURE>> 
    dbms_output.put_line(''); 
END;
