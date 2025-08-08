package com.moodyclues.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moodyclues.model.CounsellorUser;
import com.moodyclues.model.JournalUser;
import com.moodyclues.model.LinkRequest;
import com.moodyclues.model.LinkRequest.Status;

public interface LinkRequestRepository extends JpaRepository<LinkRequest, String> {

	@Query("SELECT l FROM LinkRequest l WHERE l.counsellorUser = :counsellor AND l.journalUser = :journal")
	Optional<LinkRequest> findAllByCounsellorAndJournalUser(@Param("counsellor") CounsellorUser counsellor,
	                                                     @Param("journal") JournalUser journal);
    
	@Query("SELECT l FROM LinkRequest l WHERE l.journalUser = :j AND l.status = :s")
	List<LinkRequest> findAllByJournalUserAndStatus(@Param("j") JournalUser j, @Param("s") Status s);
    
	@Query("SELECT l FROM LinkRequest l WHERE l.counsellorUser = :c AND l.status = :s")
	List<LinkRequest> findByCounsellorAndStatus(@Param("c") CounsellorUser c, @Param("s") Status s);
	
	@Query("SELECT l FROM LinkRequest l WHERE l.counsellorUser.email = :email")
	List<LinkRequest> findAllByCounsellorEmail(@Param("email") String email);
	
	@Query("SELECT l FROM LinkRequest l WHERE l.journalUser.email = :email")
	List<LinkRequest> findAllByJournalUserEmail(@Param("email") String email);
	
	@Query("SELECT l FROM LinkRequest l WHERE l.counsellorUser.id = :id")
	List<LinkRequest> findAllByCounsellorId(@Param("id") String id);
	
	@Query("SELECT l FROM LinkRequest l WHERE l.journalUser.id = :id")
	List<LinkRequest> findAllByJournalUserId(@Param("id") String id);
	
}
