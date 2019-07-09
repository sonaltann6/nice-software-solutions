package com.nss.simplexweb.documents.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name="document_track_detail_tbl")
public class DocumentTrackingDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="doc_track_Detail_id")
	private Long docTrackDetailId;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User partner;
	
	@Lob
	@Column(name="documents")
	private String documents;
	
	@ManyToOne
	@JoinColumn(name="document_type_id")
	private DocumentType docType;
	
	@ManyToOne
	@JoinColumn(name="doc_track_status_id")
	private DocumentTrackStatus status;
	
	@Lob
	@Column(name="notes")
	private String notes;
	
	@Column(name = "document_track_timestamp", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp documentTrackTimestamp;

}
