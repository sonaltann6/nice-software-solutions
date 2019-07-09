package com.nss.simplexweb.documents.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nss.simplexweb.documents.model.DocumentTrackStatus;

@Repository("documentTrackStatusRepository")
public interface DocumentTrackStatusRepository extends JpaRepository<DocumentTrackStatus, Long>{

	//findAll
	ArrayList<DocumentTrackStatus> findAll();
}
