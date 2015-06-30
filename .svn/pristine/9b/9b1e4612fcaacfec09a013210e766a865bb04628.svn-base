package com.uslc.pe.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the item database table.
 * 
 */
@Entity
@Table(name="style")
@NamedQuery(name="Item.findAll", query="SELECT i FROM Style i")
public class Style implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="code")
	private String code;

	//bi-directional many-to-one association to Upc
	@OneToMany(mappedBy="style")
	private List<Upc> upcs;

	public Style() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Upc> getUpcs() {
		return this.upcs;
	}

	public void setUpcs(List<Upc> upcs) {
		this.upcs = upcs;
	}

	public Upc addUpc(Upc upc) {
		getUpcs().add(upc);
		upc.setStyle(this);

		return upc;
	}

	public Upc removeUpc(Upc upc) {
		getUpcs().remove(upc);
		upc.setStyle(null);

		return upc;
	}

}