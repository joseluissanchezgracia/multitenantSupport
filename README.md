# multitenantSupport
Maven / Java library for multitenancy support, based on Hibernate and Etcd.

To use it, include a file with Spring context:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context" xmlns:p="http://www.springframework.org/schema/p"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

	<import resource="classpath:/spring/empiproject-${dbconnection.type:jdbc}-datasource.xml" />

	<bean
		class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
		<property name="staticMethod" value="com.everis.ehcos.multitenantsupport.MultitenantContextProvider.setMultitenantEnabled" />
		<property name="arguments">
			<list>
				<value>${multitenancy.enabled}</value>
			</list>
		</property>
	</bean>

	<bean id="multitenantConfigurationEventFactory"
		class="com.everis.ehcos.multitenantsupport.configuration.events.TenantConfigurationEventFactory"
		lazy-init="true">
	</bean>

	<bean id="etcdConfigurationProvider"
		class="com.everis.ehcos.multitenantsupport.configuration.etcd.EtcdTenantConfigurationProvider"
		lazy-init="true">
		<constructor-arg index="0" value="${etcd.server.url}" />
		<constructor-arg index="1"
			value="${etcd.server.tenantsConfigurationPath}" />
		<property name="eventFactory" ref="multitenantConfigurationEventFactory" />

	</bean>
	
	<bean id="multitenantConfigurationProvider"
		class="com.everis.ehcos.multitenantsupport.configuration.TenantConfigurationProvider"
		lazy-init="true">
		<property name="jsonConfigurationProvider" ref="etcdConfigurationProvider" />
	</bean>

	<bean id="dataSourceFactory"
		class="com.everis.ehcos.multitenantsupport.datasource.BasicDbcpDataSourceFactory">
		<property name="defaultAutoCommit" value="false" />
		<property name="maxActive" value="30" />
		<property name="initialSize" value="5" />
		<property name="maxIdle" value="5" />
		<property name="maxWait" value="-1" />
		<property name="validationQuery" value="${jdbc.validationQuery}" />
		<property name="altUser" value="false" />
	</bean>

	<bean id="dataSourceProvider"
		class="com.everis.ehcos.multitenantsupport.datasource.DataSourceProvider">
		<property name="defaultDataSourceRef" ref="dataSource" />
		<property name="dataSourceFactory" ref="dataSourceFactory" />
		<property name="tenantConfigurationProvider" ref="multitenantConfigurationProvider" />
	</bean>

	<bean id="dataSourceMultitenantConnectionProvider"
		class="com.everis.ehcos.multitenantsupport.hibernate.DataSourceMultitenantConnectionProvider">
		<property name="dataSourceProvider" ref="dataSourceProvider" />
	</bean>

	<bean id="dataSourceTenantIdentifierResolver"
		class="com.everis.ehcos.multitenantsupport.hibernate.DataSourceCurrentTenantIdentifierResolver">
	</bean>

</beans>
```
