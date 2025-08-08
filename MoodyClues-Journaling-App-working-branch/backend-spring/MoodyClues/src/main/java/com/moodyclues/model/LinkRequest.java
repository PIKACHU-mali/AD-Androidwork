package com.moodyclues.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class LinkRequest {

	@Id
	@GeneratedValue
	@UuidGenerator
	private String id;
	
	@ManyToOne
	private CounsellorUser counsellorUser;
	
	@ManyToOne
	private JournalUser journalUser;
	
	private LocalDateTime requestedAt;
	
	private Status status;
	

    public enum Status {
        PENDING, ACCEPTED, DECLINED
    }
    
    public LinkRequest() {
    	
    }

    
    // GETTERS AND SETTERS
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CounsellorUser getCounsellorUser() {
		return counsellorUser;
	}

	public void setCounsellorUser(CounsellorUser counsellorUser) {
		this.counsellorUser = counsellorUser;
	}

	public JournalUser getJournalUser() {
		return journalUser;
	}

	public void setJournalUser(JournalUser journalUser) {
		this.journalUser = journalUser;
	}

	public LocalDateTime getRequestedAt() {
		return requestedAt;
	}

	public void setRequestedAt(LocalDateTime requestedAt) {
		this.requestedAt = requestedAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
    
    
	
}
