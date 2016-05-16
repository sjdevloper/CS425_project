/**
 * 
 */
package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.CourseGPA;
import cs425.api.InstructorGPA;
import cs425.api.Take;

/**
 * @author Shanshan Jiang
 *
 */
public interface TakeDAO {
	@SqlQuery("SELECT * FROM take")
	Collection<Take> selectAll();

	@SqlQuery(
		"SELECT student FROM take WHERE courseID=:courseID AND student=:student")
	String select(@Bind("courseID") String courseID,
			@Bind("student") String student);

	@SqlUpdate(
		"INSERT INTO take (student, courseID, grade, approved, totalBonus)"
		+ " values (:student, :courseID, :grade, :approved, :totalBonus)")
	void insert(@BindBean Take t);

	@SqlUpdate(
		"UPDATE take"
		+" SET grade=:grade, approved=:approved"
		+" WHERE student=:student and courseID=:courseID")
	void update(@BindBean Take t);
	
	@SqlUpdate(
		"DELETE FROM take WHERE student=:student and courseID=:courseID")
	void delete(@Bind("student") String Student, 
			@Bind("courseID") String courseID);
	
	@SqlQuery(
		"SELECT student, courseID, grade"
		+" FROM take JOIN courses"
		+" ON take.courseID = courses.id"
		+" WHERE instructor=:instructor")
	Collection<Take> gradebyInstructor(@Bind("instructor") String instructor);
	
	@SqlQuery(
		"SELECT MAX(grade) AS max, MIN(grade) AS min"
		+" FROM take JOIN courses"
		+" ON take.courseID = courses.id"
		+" WHERE title=:title AND instructor=:instructor")
	InstructorGPA GPAbyaInstructor(@Bind("title") String title,
		@Bind("instructor") String instructor);
	
	@SqlQuery(
		"SELECT instructor, MAX(grade) AS max, MIN(grade) AS min"
		+" FROM take JOIN courses"
		+" ON take.courseID = courses.id"
		+" WHERE title=:title"
		+" GROUP BY instructor")
	Collection<InstructorGPA> GPAbyallInstructor(@Bind("title") String title);
	
	@SqlQuery(
		"SELECT grade, creditHour"
		+" FROM courses, take"
		+" WHERE courses.id=take.courseID AND student=:student")
	Collection<CourseGPA> selectAllGradebystudent(@Bind("student") String student);
	
}
