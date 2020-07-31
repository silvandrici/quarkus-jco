package com.gpa.poc.model;

import java.sql.Timestamp;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Item
 *
 */
@Entity
public class Access {

	   
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="accesseq")
	private int Id;
	private Timestamp Time;
	private String Info;

	public int getId() {
		return this.Id;
	}

	public void setId(int Id) {
		this.Id = Id;
	}   
	public Timestamp getTime() {
		return this.Time;
	}

	public void setTime(Timestamp Time) {
		this.Time = Time;
	} 
	public String getInfo() {
		return this.Info;
	}

	public void setInfo(String Info) {
		this.Info = Info;
	}  
   
}
