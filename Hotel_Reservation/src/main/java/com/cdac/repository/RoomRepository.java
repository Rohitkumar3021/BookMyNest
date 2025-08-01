package com.cdac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cdac.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
	@Query("SELECT DISTINCT r.roomType FROM Room r")
	List<String> findDistinctRoomTypes();
}
