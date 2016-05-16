package cs425.resources;

import java.util.Collection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.skife.jdbi.v2.DBI;
import cs425.api.Faculty;
import cs425.jdbi.FacultyDAO;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/public-faculty")
@Produces(MediaType.APPLICATION_JSON)
public class PublicFacultyResource {
	public PublicFacultyResource(DBI jdbi){
		this.dao = jdbi.onDemand(FacultyDAO.class);
	}

	@GET 
	@CacheControl(noCache = true)
	public Collection<Faculty> getAll(){
		Collection<Faculty> faculty = dao.selectAll();
		for (Faculty f: faculty){
			if (!f.getPublicEmail().equals("yes")){
				f.setEmail("hidden");
			}
			if (!f.getPublicPosition().equals("yes")){
				f.setPosition("hidden");
			}
			if (!f.getPublicYearJoined().equals("yes")){
				f.setYearJoined(-1);
			}
			if (!f.getPublicExperience().equals("yes")){
				f.setExperience("hidden");
			}
		}
		return faculty;		
	}
	
	private final FacultyDAO dao; 
	
    @Context
    UriInfo uri;
}
