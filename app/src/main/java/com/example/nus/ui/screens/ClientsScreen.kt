package com.example.nus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nus.model.Client
import com.example.nus.viewmodel.ClientsViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(
    counsellorId: String = "",
    onHomeClick: () -> Unit = {},
    onInviteClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onJournalClick: (Client) -> Unit = {},
    onDashboardClick: (Client) -> Unit = {},
    viewModel: ClientsViewModel = viewModel()
) {
    val clients by viewModel.filteredClients.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    
    // Set counsellor ID when screen loads
    LaunchedEffect(counsellorId) {
        if (counsellorId.isNotEmpty()) {
            viewModel.setCounsellorId(counsellorId)
        }
    }
    
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Left Sidebar
        LeftSidebar(
            onHomeClick = onHomeClick,
            onInviteClick = onInviteClick,
            onLogoutClick = onLogoutClick
        )
        
        // Main Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.White)
                .padding(32.dp)
        ) {
            // Header with title and invite button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Linked Clients",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Button(
                    onClick = onInviteClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Invite Client",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                placeholder = {
                    Text(
                        text = "Search...",
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Clients Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Date linked",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Name",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(200.dp)) // Space for action buttons
            }
            
            Divider(color = Color.LightGray, thickness = 1.dp)
            
            // Clients List
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(clients) { client ->
                        ClientRow(
                            client = client,
                            onJournalClick = { onJournalClick(client) },
                            onDashboardClick = { onDashboardClick(client) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LeftSidebar(
    onHomeClick: () -> Unit,
    onInviteClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
    ) {
        // MoodyClues Title
        Text(
            text = "MoodyClues",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Quick Start Section
        Text(
            text = "Quick Start",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Navigation Items
        SidebarItem(
            icon = Icons.Default.Home,
            text = "Home",
            onClick = onHomeClick
        )
        
        SidebarItem(
            icon = Icons.Default.Group,
            text = "Clients",
            onClick = { /* Current screen */ },
            isSelected = true
        )
        
        SidebarItem(
            icon = Icons.Default.PersonAdd,
            text = "Pending Invites",
            onClick = onInviteClick
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Bottom Items
        SidebarItem(
            icon = Icons.Default.Person,
            text = "Edit Profile",
            onClick = { /* TODO */ }
        )
        
        SidebarItem(
            icon = Icons.Default.Logout,
            text = "Logout",
            onClick = onLogoutClick
        )
    }
}

@Composable
private fun SidebarItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFFE3F2FD) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (isSelected) Color(0xFF1976D2) else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) Color(0xFF1976D2) else Color.Gray
        )
    }
}

@Composable
private fun ClientRow(
    client: Client,
    onJournalClick: () -> Unit,
    onDashboardClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date linked
        Text(
            text = client.linkedDate.format(DateTimeFormatter.ofPattern("d/M/yyyy")),
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        
        // Name
        Text(
            text = client.displayName,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        
        // Action Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onJournalClick,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Gray
                ),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = "Journal",
                    fontSize = 12.sp
                )
            }

            OutlinedButton(
                onClick = onDashboardClick,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Gray
                ),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = "Dashboard",
                    fontSize = 12.sp
                )
            }
        }
    }
}
