package com.uslc.pe.jpa.comparator;

import java.util.Comparator;

import com.uslc.pe.jpa.entity.PackingDetail;

public class PackageDetailComparator implements Comparator<PackingDetail> {
    @Override
    public int compare(PackingDetail o1, PackingDetail o2) {
    	//getLog().info( o1.getSku() + " - " + o2.getSku() );
        return o1.getSku()-o2.getSku();
    }
}
