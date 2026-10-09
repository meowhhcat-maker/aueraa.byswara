package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ContactChannelEntity
import com.example.ui.theme.*

@Composable
fun ContactEditorDialog(
    contactToEdit: ContactChannelEntity?,
    isNewContact: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (ContactChannelEntity) -> Unit
) {
    val initial = contactToEdit ?: ContactChannelEntity(
        id = "CONT-${System.currentTimeMillis() % 100000}",
        title = "New Contact Channel",
        channelType = "WHATSAPP",
        value = "+91 ",
        subtitle = "Direct communication line",
        isPrimary = false
    )

    var title by remember(contactToEdit) { mutableStateOf(initial.title) }
    var channelType by remember(contactToEdit) { mutableStateOf(initial.channelType) }
    var value by remember(contactToEdit) { mutableStateOf(initial.value) }
    var subtitle by remember(contactToEdit) { mutableStateOf(initial.subtitle) }
    var isPrimary by remember(contactToEdit) { mutableStateOf(initial.isPrimary) }

    val channelTypes = listOf("WHATSAPP", "PHONE", "EMAIL", "ADDRESS", "SOCIAL", "OTHER")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, AueraaElectricBlue.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            color = AueraaSurfaceDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isNewContact) Icons.Default.AddIcCall else Icons.Default.Edit,
                            contentDescription = null,
                            tint = AueraaElectricBlue,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isNewContact) "Add Contact Channel" else "Edit Contact Details",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = AueraaTextSecondary)
                    }
                }

                HorizontalDivider(color = AueraaSurfaceBorder)

                // Channel Type Selector Chips
                Text("Channel Type", fontSize = 12.sp, color = AueraaNeonCyan, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    channelTypes.take(3).forEach { type ->
                        FilterChip(
                            selected = channelType == type,
                            onClick = { channelType = type },
                            label = { Text(type, fontSize = 11.sp) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    channelTypes.drop(3).forEach { type ->
                        FilterChip(
                            selected = channelType == type,
                            onClick = { channelType = type },
                            label = { Text(type, fontSize = 11.sp) }
                        )
                    }
                }

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Channel Title / Department") },
                    placeholder = { Text("e.g. Sales WhatsApp, Support Hotline") },
                    modifier = Modifier.fillMaxWidth().testTag("contact_editor_title"),
                    singleLine = true
                )

                // Value
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = {
                        Text(
                            when (channelType) {
                                "WHATSAPP", "PHONE" -> "Phone / WhatsApp Number (with country code)"
                                "EMAIL" -> "Email Address"
                                "ADDRESS" -> "Office Physical Address"
                                else -> "Value / URL / Handle"
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().testTag("contact_editor_value"),
                    singleLine = channelType != "ADDRESS"
                )

                // Subtitle / Working hours
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Notes / Operating Hours") },
                    placeholder = { Text("e.g. Mon-Sat 9:30 AM - 7:00 PM, Instant replies") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Primary channel toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AueraaSurfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Set as Primary Channel", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        Text("Featured at top of customer contact directory", fontSize = 11.sp, color = AueraaTextMuted)
                    }
                    Switch(
                        checked = isPrimary,
                        onCheckedChange = { isPrimary = it }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel", color = AueraaTextSecondary)
                    }

                    Button(
                        onClick = {
                            if (title.isNotBlank() && value.isNotBlank()) {
                                onSave(
                                    initial.copy(
                                        title = title.trim(),
                                        channelType = channelType,
                                        value = value.trim(),
                                        subtitle = subtitle.trim(),
                                        isPrimary = isPrimary
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                        modifier = Modifier.weight(1f).testTag("contact_editor_save_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Contact", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
