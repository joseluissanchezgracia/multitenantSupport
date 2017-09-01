package com.multitenantsupport.configuration.model.tenant.datasource;

import com.everis.ehcos.multitenantsupport.configuration.model.tenant.datasource.DataSourceConfiguration;
import com.everis.ehcos.multitenantsupport.configuration.model.tenant.datasource.IDataSourceConfiguration;

/**
 * 
 * Implementacion por defecto de configuracion de Datasources
 *
 */
public class DataSourceConfiguration implements IDataSourceConfiguration {

	private String driverName;

	private String userName;

	private String password;

	private String url;
	
	private String altUserName;

	private String altPassword;

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getAltUserName() {
		return altUserName;
	}

	public void setAltUserName(String altUserName) {
		this.altUserName = altUserName;
	}

	public String getAltPassword() {
		return altPassword;
	}

	public void setAltPassword(String altPassword) {
		this.altPassword = altPassword;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean equals(Object arg0) {
		boolean isEqual = false;
		if (arg0 instanceof DataSourceConfiguration) {
			DataSourceConfiguration dsc = (DataSourceConfiguration) arg0;
			if (dsc.getDriverName().equals(this.getDriverName()) && dsc.getPassword().equals(this.getPassword())
					&& dsc.getUrl().equals(this.getUrl()) && dsc.getUserName().equals(this.getUserName())
					&& dsc.getAltUserName().equals(this.getAltUserName()) && dsc.getAltPassword().equals(this.getAltPassword())) {
				isEqual = true;
			}
		}
		return isEqual;
	}

	@Override
	public String toString() {

		StringBuilder stBuilder = new StringBuilder();
		stBuilder.append("DataSource Configuration: ");

		stBuilder.append("[Drivername: ");
		stBuilder.append(driverName);
		stBuilder.append("], ");

		stBuilder.append("[URL: ");
		stBuilder.append(url);
		stBuilder.append("], ");

		stBuilder.append("[Username: ");
		stBuilder.append(userName);
		stBuilder.append("], ");

		stBuilder.append("[Password: ");
		stBuilder.append(password);
		stBuilder.append("] ");
		
		stBuilder.append("[Alt Username: ");
		stBuilder.append(altUserName);
		stBuilder.append("], ");

		stBuilder.append("[Alt Password: ");
		stBuilder.append(altPassword);
		stBuilder.append("] ");

		return stBuilder.toString();
	}

}
