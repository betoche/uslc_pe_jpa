package com.uslc.pe.jpa.util;

public enum Constants {
	MESSAGE_BOX_DIAG_TITLE( "USLC Apparel - PO Master" );
	
	private String msg = "";
	
	private Constants( String msg ){
		this.msg = msg;
	}
	
	public String toString(){
		return msg;
	}
}
