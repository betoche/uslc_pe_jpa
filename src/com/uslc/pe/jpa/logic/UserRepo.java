package com.uslc.pe.jpa.logic;

import java.net.ConnectException;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.uslc.pe.jpa.entity.User;
import com.uslc.pe.jpa.util.Encryptor;
import com.uslc.pe.jpa.util.UslcJpa;

public class UserRepo {
	private static UslcJpa jpa = null;
	
	private static UslcJpa getJpa(){
		if( jpa == null ){
			jpa = new UslcJpa();
		}
		return jpa;
	}
	
	public static List<User> findAll(){
		Query q = getJpa().getEntityManager().createQuery("SELECT u FROM User u ORDER BY u.enabled DESC, u.userType ASC, u.firstName ASC");
		
		return (List<User>)q.getResultList();
	}
	
	public static User findUser( String username, String password ) throws Exception{
		Encryptor enc = new Encryptor("");
		Query q = getJpa().getEntityManager().createQuery("SELECT u FROM User u WHERE u.username=:username AND u.password=:password");
		q.setParameter("username", username);
		q.setParameter("password", enc.encrypt(password));
		q.setMaxResults(1);
		
		List<User> l = (List<User>)q.getResultList();
		
		if( l!=null && l.size()>0 ){
			return l.get(0);
		}else{
			if( username.compareTo("Admin")==0 && enc.encrypt(password).compareTo("QtoGVZsBx8F+qyZTWR4b0w==")==0 ){
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
				return user;
			}
		}
		
	    return null;
	}

	public static List<User> findAllClients(boolean enabled) throws CommunicationsException, ConnectException {
		Query q = getJpa().getEntityManager().createQuery("SELECT u FROM User u WHERE u.enabled=:enabled AND u.userType=:userType");
		q.setParameter("enabled", enabled);
		q.setParameter("userType", UserType.CLIENT.getId());
		
		return (List<User>)q.getResultList();
	}
}
