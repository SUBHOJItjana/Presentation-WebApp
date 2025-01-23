package com.ty.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ty.Entities.Rating;
import com.ty.Entities.User;
import com.ty.Enum.Role;

public interface RatingRepo extends JpaRepository<Rating, Integer> {

	List<Rating> findAllRatingByUser_Uid(Integer uid);
	
	

}
