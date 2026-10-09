package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AueraaLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    containerColor: Color = AueraaElectricBlue,
    strokeColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.22f))
            .background(containerColor)
            .padding(size * 0.12f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            val apexX = w / 2f
            val apexY = h * 0.16f

            val wingLeftX = w * 0.14f
            val wingLeftY = h * 0.50f

            val wingRightX = w * 0.86f
            val wingRightY = h * 0.50f

            val botLeftX = w * 0.22f
            val botLeftY = h * 0.84f

            val botRightX = w * 0.78f
            val botRightY = h * 0.84f

            val notchX = w / 2f
            val notchY = h * 0.72f

            val innerMidX = w / 2f
            val innerMidY = h * 0.58f

            val strokeWidth = (w * 0.06f).coerceAtLeast(1.5f)
            val stroke = Stroke(width = strokeWidth)

            // Outer contour polygon
            val outerPath = Path().apply {
                moveTo(apexX, apexY)
                lineTo(wingRightX, wingRightY)
                lineTo(botRightX, botRightY)
                lineTo(notchX, notchY)
                lineTo(botLeftX, botLeftY)
                lineTo(wingLeftX, wingLeftY)
                close()
            }
            drawPath(outerPath, color = strokeColor, style = stroke)

            // Central vertical spine
            drawLine(strokeColor, Offset(apexX, apexY), Offset(notchX, notchY), strokeWidth = strokeWidth)

            // Horizontal wing strut
            drawLine(strokeColor, Offset(wingLeftX, wingLeftY), Offset(wingRightX, wingRightY), strokeWidth = strokeWidth)

            // Inner facet lines
            drawLine(strokeColor, Offset(wingLeftX, wingLeftY), Offset(innerMidX, innerMidY), strokeWidth = strokeWidth)
            drawLine(strokeColor, Offset(wingRightX, wingRightY), Offset(innerMidX, innerMidY), strokeWidth = strokeWidth)
            drawLine(strokeColor, Offset(botLeftX, botLeftY), Offset(innerMidX, innerMidY), strokeWidth = strokeWidth)
            drawLine(strokeColor, Offset(botRightX, botRightY), Offset(innerMidX, innerMidY), strokeWidth = strokeWidth)
        }
    }
}

@Composable
fun AueraaBrandBanner(
    modifier: Modifier = Modifier,
    height: Dp = 48.dp
) {
    // Exact visual reproduction of the user's uploaded banner
    Row(
        modifier = modifier
            .height(height)
            .border(1.5.dp, AueraaElectricBlue, RoundedCornerShape(4.dp))
            .background(Color.Black, RoundedCornerShape(4.dp))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Aueraa",
                fontSize = (height.value * 0.42f).sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = ".Digital",
                fontSize = (height.value * 0.42f).sp,
                fontWeight = FontWeight.ExtraBold,
                color = AueraaElectricBlue,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = (-0.5).sp
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        AueraaLogoMark(
            size = height * 0.76f,
            containerColor = AueraaElectricBlue
        )
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status.uppercase()) {
        "ACTIVE", "COMPLETED", "PAID" -> Triple(
            AueraaSuccessEmerald.copy(alpha = 0.15f),
            AueraaSuccessEmerald,
            status.replace("_", " ")
        )
        "IN_PROGRESS", "PARTIALLY_PAID", "REVIEW" -> Triple(
            AueraaElectricBlue.copy(alpha = 0.15f),
            AueraaElectricBlue,
            status.replace("_", " ")
        )
        "PLANNING", "NEW", "PROPOSAL_SENT" -> Triple(
            AueraaRoyalPurple.copy(alpha = 0.15f),
            AueraaNeonCyan,
            status.replace("_", " ")
        )
        "DRAFT", "PENDING" -> Triple(
            AueraaAccentAmber.copy(alpha = 0.15f),
            AueraaAccentAmber,
            status.replace("_", " ")
        )
        "REVISIONS", "OVERDUE", "LOST", "ARCHIVED" -> Triple(
            AueraaErrorRose.copy(alpha = 0.15f),
            AueraaErrorRose,
            status.replace("_", " ")
        )
        else -> Triple(
            AueraaSurfaceVariant,
            AueraaTextSecondary,
            status
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, textColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun CurrencyText(
    amount: Long,
    modifier: Modifier = Modifier,
    isCustomQuote: Boolean = false,
    fontSize: Int = 20,
    color: Color = AueraaTextPrimary,
    frequency: String? = null
) {
    if (isCustomQuote || amount <= 0) {
        Text(
            text = "Custom Quote Required",
            fontSize = (fontSize - 4).sp,
            fontWeight = FontWeight.SemiBold,
            color = AueraaNeonCyan,
            modifier = modifier
        )
    } else {
        val formatted = try {
            NumberFormat.getNumberInstance(Locale("en", "IN")).format(amount)
        } catch (_: Exception) {
            amount.toString()
        }
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "₹$formatted",
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            if (!frequency.isNullOrBlank()) {
                Text(
                    text = " / $frequency",
                    fontSize = 12.sp,
                    color = AueraaTextSecondary,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    accentPill: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (!accentPill.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(AueraaRoyalPurple.copy(alpha = 0.2f))
                    .border(0.8.dp, AueraaRoyalPurple.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = accentPill.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AueraaNeonCyan,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = AueraaTextPrimary,
            fontWeight = FontWeight.Bold
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = AueraaTextSecondary
            )
        }
    }
}

@Composable
fun FloatingWhatsAppButton(
    whatsappNumber: String,
    prefilledMessage: String = "Hi Aueraa.Digital, I would like to enquire about your digital services.",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    FloatingActionButton(
        onClick = {
            openWhatsAppChat(context, whatsappNumber, prefilledMessage)
        },
        containerColor = Color(0xFF25D366),
        contentColor = Color.White,
        shape = CircleShape,
        modifier = modifier
            .testTag("floating_whatsapp_button")
            .shadow(8.dp, CircleShape)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Chat on WhatsApp",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Chat with Us",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

fun openWhatsAppChat(context: Context, number: String, message: String) {
    val cleanNumber = number.replace("+", "").replace(" ", "").replace("-", "")
    val url = "https://api.whatsapp.com/send?phone=$cleanNumber&text=${Uri.encode(message)}"
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp: $number", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color = AueraaElectricBlue,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AueraaSurfaceBorder, Color.Transparent))),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = AueraaTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AueraaTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = AueraaNeonCyan,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
