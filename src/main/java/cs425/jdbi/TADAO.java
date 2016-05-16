/**
 * 
 */
package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.TA;

/**
 * @author Shanshan Jiang
 *
 */
public interface TADAO {
	@SqlQuery("SELECT * FROM TAs")
	Collection<TA> selectAll();

	@SqlQuery("SELECT courseTA FROM TAs WHERE courseID=:courseID")
	Collection<String> findTAByCourse(@Bind("courseID") String courseID);
	
	@SqlUpdate(
		"INSERT INTO TAs (id, courseID, courseTA)"+
		"  values (:id, :courseID, :courseTA)")
		void insert(@BindBean TA i);
	
	@SqlUpdate(
		"DELETE FROM TAs WHERE id=:id")
		void delete(@Bind("id") String id);
	
	@SqlUpdate(
		"UPDATE TAs "
		+"SET courseID=:courseID, courseTA=:courseTA "
		+"WHERE id=:id")
		void update(@BindBean TA i);
}
