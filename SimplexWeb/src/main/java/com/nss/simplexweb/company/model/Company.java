package com.nss.simplexweb.company.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nss.simplexweb.user.model.Country;
import com.nss.simplexweb.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor 
@NoArgsConstructor
@Table(name="company_master_tbl")
public class Company implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="company_id")
	private Long companyId;
	
	@Column(name="company_name", unique=true)
	private String companyName;
	
	@Column(name="company_address_line_one")
	private String companyAddressLineOne;
	
	@Column(name="company_address_line_two")
	private String companyAddressLineTwo;
	
	@Column(name="company_address_pin")
	private String companyAddressPIN;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="company_address_country")
	private Country companyAddressCountry;
	
	@Column(name="company_email")
	private String companyEmail;
	
	@Column(name="company_phone_primary", unique=true)
	private String companyPhonePrimary;
	
	@Column(name="company_phone_secondary", unique=true)
	private String companyPhoneSecondary;
	
	@Column(name="company_unique_code", unique=true)
	private String companyUniqueCode;
	
	@Column(name = "created_timestamp", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdTimestamp;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="created_by")
	@JsonBackReference
	private User createdBy;
	
	@Column(name = "is_active", nullable = false, columnDefinition = "int default 1")
	private int isActive;
	
	@Override
	public String toString(){
		  return null;
	}
}
