package com.ty.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ty.Entities.Presentation;

public interface Presentation_Repository extends JpaRepository<Presentation, Integer> {

	@Query("SELECT p FROM Presentation p WHERE p.user.id = :uid")
	List<Presentation> findByUserId(@Param("uid") int uid);

}
