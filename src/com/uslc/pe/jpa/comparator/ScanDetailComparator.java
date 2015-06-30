package com.uslc.pe.jpa.comparator;

import java.util.Comparator;

import com.uslc.pe.jpa.entity.ScanDetail;

public class ScanDetailComparator implements Comparator<ScanDetail> {

	@Override
	public int compare(ScanDetail o1, ScanDetail o2) {
		return (int)( o1.getTimestamp().getTime() - o2.getTimestamp().getTime() );
	}

}
