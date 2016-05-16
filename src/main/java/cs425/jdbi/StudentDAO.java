package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.Student;

public interface StudentDAO
{
	@SqlQuery(
		"SELECT * FROM students NATURAL JOIN users")
	Collection<Student> selectAll();
	
	@SqlQuery(
		"SELECT * FROM students NATURAL JOIN users WHERE username=:username")
	Student select(@Bind("username") String username);
	
	@SqlUpdate(
		"INSERT INTO students (username, name, email, yearBegan, semesterBegan,"
		+" degreeStatus, degreeType, publicEmail, publicYearBegan,"
		+" publicSemesterBegan, publicDegreeStatus, publicDegreeType, publicGPA)"
		+" values (:username, :name, :email, :yearBegan, :semesterBegan,"
		+" :degreeStatus, :degreeType, :publicEmail, :publicYearBegan,"
		+" :publicSemesterBegan, :publicDegreeStatus, :publicDegreeType, :publicGPA)")
	void insert(@BindBean Student s);

	@SqlUpdate(
		"DELETE FROM students WHERE username=:username")
	void delete(@Bind("username") String username);
	
	@SqlUpdate(
		"UPDATE students"
		+" SET email=:email, yearBegan=:yearBegan, semesterBegan=:semesterBegan,"
		+" degreeStatus=:degreeStatus,degreeType=:degreeType,"
		+" publicEmail=:publicEmail, publicYearBegan=:publicYearBegan,"
		+" publicSemesterBegan=:publicSemesterBegan, publicDegreeStatus=:publicDegreeStatus,"
		+" publicDegreeType=:publicDegreeType, publicGPA=:publicGPA"
		+" WHERE username=:username")
	void update(@BindBean Student s);
}