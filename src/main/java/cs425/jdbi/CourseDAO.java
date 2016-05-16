/**
 * 
 */
package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.Course;

/**
 * @author Shanshan Jiang
 *
 */
public interface CourseDAO {
	@SqlQuery(
			"SELECT * FROM courses")
		Collection<Course> selectAll();
	
	@SqlQuery(
			"SELECT * FROM courses WHERE id=:id")
		Course select(@Bind("id") String id);

	@SqlUpdate(
			"INSERT INTO courses (id, title, term, instructor, creditHour, groupID)"
			+ " values (:id, :title, :term, :instructor, :creditHour, :groupID)")
		void insert(@BindBean Course c);

	@SqlUpdate(
			"UPDATE courses"
			+" SET term=:term, instructor=:instructor, creditHour=:creditHour"
			+" WHERE id=:id")
		void update(@BindBean Course c);
	
	@SqlUpdate(
			"DELETE FROM courses WHERE id=:id")
		void delete(@Bind("id") String id);
	
	@SqlQuery(
			"SELECT * FROM courses WHERE groupID=:groupID")
		Course findCourseByGroup(@Bind("groupID") String groupID);
}
