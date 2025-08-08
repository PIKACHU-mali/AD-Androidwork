package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.model.Client
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ClientsViewModel : ViewModel() {
    
    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()
    
    private val _filteredClients = MutableStateFlow<List<Client>>(emptyList())
    val filteredClients: StateFlow<List<Client>> = _filteredClients.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private var counsellorId: String = ""
    
    init {
        loadMockData()
    }
    
    fun setCounsellorId(id: String) {
        counsellorId = id
        println("ClientsViewModel: Setting counsellorId = '$id'")
        // TODO: Load real data from API when backend is ready
        // loadClientsFromApi(id)
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterClients(query)
    }
    
    private fun filterClients(query: String) {
        val allClients = _clients.value
        _filteredClients.value = if (query.isBlank()) {
            allClients
        } else {
            allClients.filter { client ->
                client.displayName.contains(query, ignoreCase = true) ||
                client.email.contains(query, ignoreCase = true)
            }
        }
    }
    
    private fun loadMockData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Mock data matching the web screenshot
            val mockClients = listOf(
                Client(
                    clientId = "1",
                    firstName = "Joe",
                    lastName = "",
                    email = "joe@email.com",
                    linkedDate = LocalDateTime.of(2025, 7, 21, 0, 0)
                ),
                Client(
                    clientId = "2", 
                    firstName = "Rick",
                    lastName = "Deckard",
                    email = "rick.deckard@email.com",
                    linkedDate = LocalDateTime.of(2025, 7, 22, 0, 0)
                ),
                Client(
                    clientId = "3",
                    firstName = "Tony",
                    lastName = "Soprano", 
                    email = "tony.soprano@email.com",
                    linkedDate = LocalDateTime.of(2025, 7, 23, 0, 0)
                )
            )
            
            _clients.value = mockClients
            _filteredClients.value = mockClients
            _isLoading.value = false
            
            println("ClientsViewModel: Loaded ${mockClients.size} mock clients")
        }
    }
    
    // TODO: Implement when backend API is ready
    private fun loadClientsFromApi(counsellorId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // API call will go here
                // val response = apiService.getClients(counsellorId)
                // _clients.value = response.map { it.toClient() }
                // _filteredClients.value = _clients.value
                println("ClientsViewModel: API call not implemented yet, using mock data")
                loadMockData()
            } catch (e: Exception) {
                println("ClientsViewModel: Error loading clients: ${e.message}")
                loadMockData() // Fallback to mock data
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun onJournalClick(client: Client) {
        println("ClientsViewModel: Journal clicked for client: ${client.displayName}")
        // TODO: Navigate to client's journal
    }
    
    fun onDashboardClick(client: Client) {
        println("ClientsViewModel: Dashboard clicked for client: ${client.displayName}")
        // TODO: Navigate to client's dashboard
    }
}
