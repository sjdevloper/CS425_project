package cs425;

import java.nio.file.Paths;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

import cs425.auth.*;
import cs425.resources.*;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MyApp extends Application<MyConfig>
{
	@Override
	public void initialize(Bootstrap<MyConfig> bootstrap)
	{
	    bootstrap.addBundle(
	    	new AssetsBundle("/assets/", "/", "index.html"));
	    
	    bootstrap.addBundle(
	    	new DBIExceptionsBundle());
	}
	
	@Override
	public void run(MyConfig config, Environment env)
		throws Exception
	{
		// Setup database
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(env,
			config.getDataSourceFactory(), "database");
		jdbi.registerMapper(new BeanMapperFactory());

		JerseyEnvironment jersey = env.jersey();
		
		// Setup PHP
		jersey.register(new PHPOracleResource(
			config.getDataSourceFactory(),
			Paths.get("assets/oracle")));
		
		// Setup users
		CookieAuth auth = new CookieAuth();
		jersey.register(new AuthDynamicFeature(auth));
		jersey.register(RolesAllowedDynamicFeature.class);
		jersey.register(new AuthValueFactoryProvider.Binder<>(Account.class));
		jersey.register(new Accounts(auth, jdbi));

		// Resources depending on database
		jersey.register(new UsersResource(jdbi));
		jersey.register(new GroupUsersResource(jdbi));
		jersey.register(new FacultyResource(jdbi));
		jersey.register(new StudentsResource(jdbi));
		jersey.register(new PublicStudentsResource(jdbi));
		jersey.register(new PublicFacultyResource(jdbi));
		jersey.register(new CoursesResource(jdbi));
		jersey.register(new TakeResource(jdbi));
		jersey.register(new ProjectsResource(jdbi));
		jersey.register(new InternshipsResource(jdbi));
		jersey.register(new GroupsResource(jdbi));
		jersey.register(new CommentsResource(jdbi));
		jersey.register(new TAResource(jdbi));
		jersey.register(new ForumsResource(jdbi));
		jersey.register(new PublicResource(jdbi));
	}

	public static void main(String[] args)
		throws Exception
	{
		new MyApp().run(args);
	}
}
