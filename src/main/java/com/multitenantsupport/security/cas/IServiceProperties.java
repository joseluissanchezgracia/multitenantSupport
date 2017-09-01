package com.multitenantsupport.security.cas;

public interface IServiceProperties {

	public String getService();
	
	public boolean isSendRenew();

    public void setSendRenew(boolean sendRenew);

    public void setService(String service);

    public String getArtifactParameter();

    public  void setArtifactParameter(String artifactParameter);

    public  String getServiceParameter();

    public void setServiceParameter(String serviceParameter);
}