package com.multitenantsupport.configuration.model.tenant.datasource;

/**
 * 
 * Interface que contiene la configuracion basica del DataSource de un tenant
 *
 */
public interface IDataSourceConfiguration {

	/**
	 * 
	 * @return Nombre del driver 
	 */
	public String getDriverName();
	
	/**
	 * 
	 * @param driverName Nombre del driver
	 */
	public void setDriverName(String driverName);
	
	/**
	 * 
	 * @return User name de conexion
	 */
	public String getUserName();
	
	/**
	 * 
	 * @param userName User name de conexion
	 */
	public void setUserName(String userName);
	
	/**
	 * 
	 * @return Password de conexion
	 */
	public String getPassword();
	
	/**
	 * 
	 * @param password Password de conexion
	 */
	public void setPassword(String password);
	
	/**
	 * 
	 * @return URL de conexion. Por ejemplo jdbc:postgresql://55.55.55.55:5432/ehcos
	 */
	public String getUrl();
	
	/**
	 * 
	 * @param url URL de conexion. Por ejemplo jdbc:postgresql://55.55.55.55:5432/ehcos
	 */
	public void setUrl(String url);
	
	/**
	 * 
	 * @return Alternative User name de conexion
	 */
	public String getAltUserName();
	
	/**
	 * 
	 * @param userName Alternative User name de conexion
	 */
	public void setAltUserName(String altUserName);
	
	/**
	 * 
	 * @return Password de conexion
	 */
	public String getAltPassword();
	
	/**
	 * 
	 * @param password Alternative Password de conexion
	 */
	public void setAltPassword(String altPassword);
	
}
