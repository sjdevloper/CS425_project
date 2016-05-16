package cs425;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class MyConfig extends Configuration
{
	@JsonProperty("database")
	public void setDataSourceFactory(
		DataSourceFactory factory)
	{
		this.factory = factory;
	}

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory()
	{
		return factory;
	}

	@Valid
	@NotNull
	private DataSourceFactory factory;
}
