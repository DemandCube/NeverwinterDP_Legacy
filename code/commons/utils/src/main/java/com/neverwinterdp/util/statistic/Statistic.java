package com.neverwinterdp.util.statistic;

import java.io.Serializable;

public class Statistic implements Serializable {
	final static public String DOCUMENT = "Document" ;
	final static public String ALL      = "All" ;
	final static public String COUNT    = "count" ;
	
  private String name ;
	private long   frequency ;
	private String relateTo ;
	private Object model ;

	public Statistic(String name, String relateTo, long freq)  { 
		this.name = name ; 
		this.relateTo = relateTo ;
		this.frequency = freq ; 
	}
	
	public String getName() { return this.name ; }

	public long getFrequency() { return this.frequency ; }
	
	public String getRelateTo(){ return this.relateTo; }

	public void incr(long value) { this.frequency += value ; }

	public <T> T getModel() { return (T) model ; }
	public void  setModel(Object object) { this.model = object ; }
}