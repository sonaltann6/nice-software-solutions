package com.nss.simplexweb.enquiry.template.model.fibcCosting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor 
@NoArgsConstructor
@Table(name="fibc_item_cost_table")
public class FibcItemCostTable implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sr_no")
	private int srNo;
	
	@Column(name="cost_unit")
	private String costUnit;
	
	@Column(name="item_id")
	private String itemId;

	@Column(name="item_name")
	private String itemName;

}
