package com.nss.simplexweb.po.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nss.simplexweb.enquiry.template.model.product.ProductModelType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor 
@NoArgsConstructor
@Table(name="po_items_tbl")
public class POItems implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="po_item_id")
	private Long poItemId;
	
	@Column(name="po_item_number")
	private String poItemNumber;
	
	@Lob
	@Column(name="po_item_desc")
	private String poItemDesc;
	
	@Column(name="po_item_qty")
	private Double poItemQty;
	
	@Column(name="po_item_rate")
	private Double poItemRate;
	
	@Column(name="po_item_amount")
	private Double poItemAmount;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_type_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private ProductModelType productModelType;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="po_id")
    private PODetail poDetail;
}
