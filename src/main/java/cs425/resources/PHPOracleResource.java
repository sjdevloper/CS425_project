package cs425.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/oracle")
@Produces(MediaType.TEXT_HTML)
public class PHPOracleResource
{
	public PHPOracleResource(
		DataSourceFactory factory,
		java.nio.file.Path location)
	{
		this.user = factory.getUser();
		this.password = factory.getPassword();
		
		String url = factory.getUrl();
		int at = url.indexOf('@');
		int last = url.lastIndexOf(':');
		this.connection = "//"+url.substring(at+1, last)+"/"+url.substring(last+1);
		
		this.location = location;
		
		logger.info("serving php from {} using {}@{}",
			this.location, this.user, this.connection);
	}
	
	@Path("/{phpfile}")
	@GET @CacheControl(noCache=true)
	public Response process(
		@PathParam("phpfile") @NotEmpty String phpfile,
		@QueryParam("paramA") String paramA,
		@QueryParam("paramB") String paramB,
		@QueryParam("paramC") String paramC,
		@QueryParam("paramD") String paramD)
	{
		if (paramA == null)
			paramA = "paramA";
		if (paramB == null)
			paramB = "paramB";
		if (paramC == null)
			paramC = "paramC";
		if (paramD == null)
			paramD = "paramD";

		logger.info("process {}, {}, {}, {}, {}",
			phpfile, paramA, paramB, paramC, paramD);
		
		try
		{
			Process php = new ProcessBuilder("php", "-f",
				location.resolve(phpfile).toString(),
				user, password, connection,
				paramA, paramB, paramC, paramD).start();
				
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(php.getInputStream()));
		
			StringBuilder builder = new StringBuilder();
			for (;;)
			{
				String line = reader.readLine();
				if (line == null)
					break;
				builder.append(line);
				builder.append("\r\n");
			}
			
			php.waitFor();
			
			return Response.ok(builder.toString()).build();
		}
		catch (Exception e)
		{
			return Response.status(Status.BAD_REQUEST).build();
		}
    }
	
	final String user;
	final String password;
	final String connection;
	
	final java.nio.file.Path location;
	
	private static final Logger logger
		= LoggerFactory.getLogger(PHPOracleResource.class);
}
