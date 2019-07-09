package com.nss.simplexweb.documents.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nss.simplexweb.documents.model.DocumentType;

@Repository("documentTypeRepository")
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long>{

	//findAll
	ArrayList<DocumentType> findAll();
}
