package com.uslc.pe.jpa.logic;

import java.util.List;

import javax.persistence.Query;

import com.uslc.pe.jpa.entity.Style;
import com.uslc.pe.jpa.util.UslcJpa;

public class StyleRepo {
	private static UslcJpa jpa = null;
	
	public static List<Style> findAll(){
		Query q = getJpa().getEntityManager().createQuery("SELECT i FROM Style i");
	    return (List<Style>)q.getResultList();
	}
	private static UslcJpa getJpa(){
		if( jpa == null ){
			jpa = new UslcJpa();
		}
		return jpa;
	}
	public static Style findByCode(String styleCode) {
		Style item = null;
		Query q = getJpa().getEntityManager().createQuery("SELECT i FROM Style i WHERE i.code = :code");
		q.setParameter("code", styleCode);
		q.setMaxResults(1);
		
		List<Style> items = (List<Style>)q.getResultList();
		
		if( items!=null && items.size()>0 ){
			item = items.get(0);
		}
		
	    return item;
	}
	public static Style createItem(Style style) throws Exception {
		style = (Style)getJpa().merge(style);
		if( style==null ){
			throw new Exception( "there was a problem persisting the item" );
		}
		return style;
	}
}
