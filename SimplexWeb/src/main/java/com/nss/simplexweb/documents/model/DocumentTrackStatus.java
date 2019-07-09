package com.nss.simplexweb.documents.model;

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
@Table(name="doc_track_status_tbl")
public class DocumentTrackStatus implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="doc_track_status_id")
	private Long docTrackStatusId;
	
	@Column(name="doc_track_status_abbr")
	private String docTrackStatusAbbr;
	
	@Column(name="doc_track_status_name")
	private String docTrackStatusName;
	
}
