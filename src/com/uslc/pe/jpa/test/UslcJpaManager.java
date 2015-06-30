package com.uslc.pe.jpa.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import javax.persistence.spi.PersistenceUnitTransactionType;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.persistence.config.TargetServer;








import com.uslc.pe.jpa.comparator.ColorComparator;
import com.uslc.pe.jpa.entity.Color;
import com.uslc.pe.jpa.entity.Style;
import com.uslc.pe.jpa.entity.PurchaseOrder;
import com.uslc.pe.jpa.entity.PurchaseOrderDetail;
import com.uslc.pe.jpa.entity.Size;
import com.uslc.pe.jpa.entity.Upc;
import com.uslc.pe.jpa.entity.User;
import com.uslc.pe.jpa.logic.UserType;
import com.uslc.pe.jpa.util.Encryptor;
import com.uslc.pe.jpa.util.UslcJpa;

public class UslcJpaManager {
	public List<PurchaseOrder> poList = null;
	private Map<String,String> properties = null;
	public List<Color> colorList = null;
	public List<Upc> upcList = null;
	public List<Style> itemList = null;
	public List<Size> sizeList = null;
	public List<User> userList = null;
	private UslcJpa jpa = null;
	
	private static Logger log = null;
	
	public UslcJpaManager( String dbHost, int dbPort, String dbName, String dbUser, String dbPassword, String jdbcDriver ) {
		properties = new HashMap<String, String>();
		 
	    // Ensure RESOURCE_LOCAL transactions is used.
	    properties.put(TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
	 
	    // Configure the internal EclipseLink connection pool
	    properties.put(JDBC_DRIVER, jdbcDriver);
	    properties.put(JDBC_URL, "jdbc:mysql://"+dbHost+":"+dbPort+"/" + dbName);
	    properties.put(JDBC_USER, dbUser);
	    properties.put(JDBC_PASSWORD, dbPassword);
	 
	    // Configure logging. FINE ensures all SQL is shown
	    properties.put(LOGGING_LEVEL, "FINE");
	    properties.put(LOGGING_TIMESTAMP, "false");
	    properties.put(LOGGING_THREAD, "false");
	    properties.put(LOGGING_SESSION, "false");
	 
	    // Ensure that no server-platform is configured
	    properties.put(TARGET_SERVER, TargetServer.None);
	}
	
	public List<PurchaseOrder> getPurchaseOrderList() {
		if( poList == null ) {
			
			Query q = getJpa().getEntityManager().createQuery("SELECT po FROM PurchaseOrder po WHERE po.deleted=:deleted ORDER BY po.referenceNumber DESC");
			q.setParameter("deleted", false);
			//q.setHint("javax.persistence.cache.storeMode", "REFRESH");
			poList = (List<PurchaseOrder>)q.getResultList();
			/*for( PurchaseOrder po : poList ) {
				getJpa().refresh( po );
			}*/
		}
		return poList;
	}
	
	public List<Color> getColors(){
		if( colorList == null ) {
			Query q = getJpa().getEntityManager().createQuery("SELECT c FROM Color c");
			colorList = (List<Color>)q.getResultList();
		}
		return colorList;
	}
	public List<Upc> getUpcs(){
		if( upcList == null ) {
			Query q = getJpa().getEntityManager().createQuery("SELECT u FROM Upc u ORDER BY u.deleted, u.upcCode ASC");
			upcList = (List<Upc>)q.getResultList();
		}
		return upcList;
	}
	public List<Style> getItems() {
		if( itemList == null ) {
			Query q = getJpa().getEntityManager().createQuery("SELECT i FROM Item i");
			itemList = (List<Style>)q.getResultList();
		}
		return itemList;
	}
	public List<Size> getSizes() {
		if( sizeList == null ) {
			Query q = getJpa().getEntityManager().createQuery("SELECT s FROM Size s");
			sizeList = (List<Size>)q.getResultList();
		}
		return sizeList;
	}
	public List<User> getUsers() {
		if( userList == null ) {
			Query q = getJpa().getEntityManager().createQuery("SELECT u FROM User u ORDER BY u.enabled DESC, u.userType ASC, u.firstName ASC");
			userList = (List<User>)q.getResultList();
			
			getLog().info( "userList size: " + userList.size() );
			
			if( userList==null || !(userList.size()>0) ) {
				User user = new User();
				user.setId(-1);
				user.setUsername( "Admin" );
				user.setPassword( "QtoGVZsBx8F+qyZTWR4b0w==" );
				user.setFirstName("admin");
				user.setLastName("uslc");
				user.setEnabled(true);
				user.setActive(false);
				user.setUserType(UserType.ADMIN.getId());
				user.setTimestamp( Calendar.getInstance().getTime() );

				userList = new ArrayList<User>();
				userList.add(user);
			}
		}
		return userList;
	}
	public User[] getUsers(boolean enabled) {
		User[] users = new User[0];
		
		List<User> userList = new ArrayList<User>();
		for( User u : getUsers() ) {
			if( u.isEnabled()==enabled ) {
				userList.add(u);
			}
		}
		
		users = userList.toArray(users);
		
		return users;
	}
	
	public void refreshUsers() {
		for(User u : getUsers()) {
			getJpa().refresh( u );
		}
	}
	
	public UslcJpa getJpa() {
		if( jpa == null ) {
			jpa = new UslcJpa( getProperties() );
		}
		return jpa;
	}
	
	public PurchaseOrderDetail[] getPurchaseOrderDetails( PurchaseOrder po, boolean deleted ) {
		PurchaseOrderDetail[] pods = new PurchaseOrderDetail[0];
		List<PurchaseOrderDetail> podList = new ArrayList<PurchaseOrderDetail>();
		
		for( PurchaseOrderDetail pod : po.getPurchaseOrderDetails() ) {
			if( pod.getDeleted()==deleted ) {
				podList.add(pod);
			}
		}
		
		pods = podList.toArray(pods);
		
		return pods;
	}
	public PurchaseOrderDetail[] getPurchaseOrderDetails( PurchaseOrder po, Color color, boolean deleted ){
		PurchaseOrderDetail[] pods = new PurchaseOrderDetail[0];
		
		List<PurchaseOrderDetail> podList = new ArrayList<PurchaseOrderDetail>();
		
		for( PurchaseOrderDetail pod : po.getPurchaseOrderDetails() ) {
			if( pod.getDeleted()==deleted ) {
				Upc upc = pod.getUpc();
				if( upc.getColor().getId() == color.getId() ) {
					podList.add(pod);
				}
			}
		}
		
		pods = podList.toArray(pods);
		
		return pods;
	}
	public Color[] getColors( PurchaseOrder po, boolean deleted ) {
		Color[] colors = new Color[0];
		
		List<Color> colorList = new ArrayList<Color>();
		for( PurchaseOrderDetail pod : getPurchaseOrderDetails(po, deleted) ) {
			Upc upc = pod.getUpc();
			
			boolean exists = false;
			for( Color c : colorList ) {
				if( c.getId() == upc.getColor().getId() ) {
					exists = true;
				}
			}
			if( !exists ) {
				colorList.add( upc.getColor() );
			}
		}
		
		Collections.sort(colorList, new ColorComparator() );
		
		colors = colorList.toArray(colors);
		
		return colors;
	}
	
	public User getUser( String username, String password ) {
		User user = null;
		
		Encryptor enc = new Encryptor("");
		
		for( User u : getUsers() ) {
			getLog().info( "encripted pass: " + enc.encrypt(password) + " against " + u.getPassword() );
			if( u.getUsername()!=null && u.getPassword()!=null && u.getUsername().compareTo( username )==0 && u.getPassword().compareTo( enc.encrypt(password) )==0 ) {
				user = u;
				break;
			}
		}
		
		return user;
	}
	
	private Map<String,String> getProperties(){
		return properties;
	}

	public void updateLiveDataAccess() {
		getJpa().evictAll();
		
		poList = null;
		colorList = null;
		upcList = null;
		itemList = null;
		sizeList = null;
		userList = null;
	}
	
	public static void main(String[] args) {

	}

	public User[] getUsersClients(boolean enabled) {
		User[] users = new User[0];
		
		List<User> userList = new ArrayList<User>();
		for( User u : getUsers() ) {
			if( u.isEnabled()==enabled && u.getUserType()==1 ) {
				userList.add(u);
			}
		}
		
		users = userList.toArray(users);
		
		return users;
	}
	
	private static Logger getLog() {
		if( log == null ) {
			log = Logger.getLogger(UslcJpaManager.class);
			PropertyConfigurator.configure( "log4j.properties" );
		}
		return log;
	}
}
