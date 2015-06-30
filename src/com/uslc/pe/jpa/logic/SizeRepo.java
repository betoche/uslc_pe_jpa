package com.uslc.pe.jpa.logic;

import java.util.List;

import javax.persistence.Query;

import com.uslc.pe.jpa.entity.Size;
import com.uslc.pe.jpa.util.UslcJpa;

public class SizeRepo {
	private static UslcJpa jpa = null;
	
	public static List<Size> findAll(){
		Query q = getJpa().getEntityManager().createQuery("SELECT s FROM Size s");
	    return (List<Size>)q.getResultList();
	}
	private static UslcJpa getJpa(){
		if( jpa == null ){
			jpa = new UslcJpa();
		}
		return jpa;
	}
	public static Size findByWaistInstream(String waistInseam) {
		Size size = null;
			Query q = getJpa().getEntityManager().createQuery("SELECT s FROM Size s WHERE CONCAT(S.waist,'x',s.inseam) = :sizexinseam");
			q.setParameter("sizexinseam", waistInseam);
			q.setMaxResults(1);
			
			List<Size> items = (List<Size>)q.getResultList();
			
			if( items!=null && items.size()>0 ){
				size = items.get(0);
			}
		return size;
	}
	public static Size findByWaist(int waist) {
		Size size = null;
			Query q = getJpa().getEntityManager().createQuery("SELECT s FROM Size s WHERE s.waist=:waist AND s.inseam=:inseam");
			q.setParameter("waist", waist);
			q.setParameter("inseam", 36);
			q.setMaxResults(1);
			
			List<Size> items = (List<Size>)q.getResultList();
			
			if( items!=null && items.size()>0 ){
				size = items.get(0);
			}
		return size;
	}
	public static Size createSize(Size size) throws Exception {
		size = (Size)getJpa().merge(size);
		if( size==null ){
			throw new Exception("there was a problem persisting the size");
		}
		return size;
	}
}