package com.moodyclues.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "counsellor_user")
public class CounsellorUser extends User {
	
	@ManyToMany
	@JoinTable(name = "counsellor_client",
	joinColumns = @JoinColumn(name = "counsellor_id"),
	inverseJoinColumns = @JoinColumn(name = "client_id"))
	private List<JournalUser> clients = new ArrayList<>();
	
	@OneToMany(mappedBy = "counsellorUser")
	private List<LinkRequest> linkRequests = new ArrayList<>();
	
	// EMPTY CONSTRUCTOR
	public CounsellorUser() {
		
	}

	
	// GETTERS AND SETTERS BELOW
	public List<JournalUser> getClients() {
		return clients;
	}

	public void setClients(List<JournalUser> clients) {
		this.clients = clients;
	}
	
	
	
	
}
