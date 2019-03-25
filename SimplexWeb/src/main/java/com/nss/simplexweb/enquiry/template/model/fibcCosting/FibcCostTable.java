package com.nss.simplexweb.enquiry.template.model.fibcCosting;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the fibc_cost_table database table.
 * 
 */
@Data
@Builder
@Entity
@AllArgsConstructor 
@NoArgsConstructor
@Table(name="fibc_cost_table")
@NamedQuery(name="FibcCostTable.findAll", query="SELECT f FROM FibcCostTable f")
public class FibcCostTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="cost_unit")
	private String costUnit;

	@Column(name="item_cost")
	private double itemCost;

	@Column(name="item_id")
	private String itemId;

	@Column(name="item_name")
	private String itemName;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sr_no")
	private int srNo;

	@Column(name="user_id")
	private Long userId;

	

}