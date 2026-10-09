package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MetricDataPoint
import com.example.ui.theme.*
import kotlin.math.max

/**
 * Native Jetpack Compose Area Chart inspired by Recharts (AreaChart + Tooltip).
 * Visualizes conversion rate percentage with smooth Bezier curve, gradient fill,
 * grid lines, and interactive touch scrubbing tooltip.
 */
@Composable
fun RechartsConversionAreaChart(
    data: List<MetricDataPoint>,
    modifier: Modifier = Modifier,
    unitSuffix: String = "%"
) {
    if (data.isEmpty()) return

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val textMeasurer = rememberTextMeasurer()

    val maxVal = remember(data) {
        val peak = data.maxOfOrNull { it.conversionRate } ?: 5.0
        max(6.0, (kotlin.math.ceil(peak / 2.0) * 2.0))
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, AueraaSurfaceBorder, RoundedCornerShape(16.dp))
            .testTag("recharts_conversion_chart")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Chart Header & Interactive Tooltip Readout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(AueraaNeonCyan)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Conversion Rate Trend",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Touch or drag along chart to inspect daily data",
                        fontSize = 11.sp,
                        color = AueraaTextSecondary
                    )
                }

                // Selected point readout pill (like Recharts Tooltip)
                val activePoint = selectedIndex?.let { data.getOrNull(it) } ?: data.lastOrNull()
                if (activePoint != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AueraaSurfaceVariant)
                            .border(1.dp, AueraaNeonCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${activePoint.dateText} (${activePoint.label})",
                                fontSize = 10.sp,
                                color = AueraaTextMuted
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CVR: ",
                                    fontSize = 11.sp,
                                    color = AueraaNeonCyan,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = String.format(java.util.Locale.US, "%.2f%s", activePoint.conversionRate, unitSuffix),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "${activePoint.conversions} Leads  |  ₹${activePoint.adSpendInr} Spend",
                                fontSize = 10.sp,
                                color = AueraaSuccessEmerald
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Canvas Area & Line Rendering
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .pointerInput(data) {
                        detectTapGestures(
                            onPress = { offset ->
                                val count = data.size
                                if (count > 1) {
                                    val leftPad = 40.dp.toPx()
                                    val rightPad = 16.dp.toPx()
                                    val chartWidth = size.width - leftPad - rightPad
                                    val relX = (offset.x - leftPad).coerceIn(0f, chartWidth)
                                    val idx = ((relX / chartWidth) * (count - 1)).toInt().coerceIn(0, count - 1)
                                    selectedIndex = idx
                                }
                            }
                        )
                    }
                    .pointerInput(data) {
                        detectDragGestures(
                            onDrag = { change, _ ->
                                val count = data.size
                                if (count > 1) {
                                    val leftPad = 40.dp.toPx()
                                    val rightPad = 16.dp.toPx()
                                    val chartWidth = size.width - leftPad - rightPad
                                    val relX = (change.position.x - leftPad).coerceIn(0f, chartWidth)
                                    val idx = ((relX / chartWidth) * (count - 1)).toInt().coerceIn(0, count - 1)
                                    selectedIndex = idx
                                }
                            }
                        )
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val leftPad = 40.dp.toPx()
                    val bottomPad = 26.dp.toPx()
                    val topPad = 12.dp.toPx()
                    val rightPad = 16.dp.toPx()

                    val chartWidth = width - leftPad - rightPad
                    val chartHeight = height - topPad - bottomPad

                    val gridColor = Color(0xFF22304A)
                    val labelStyle = TextStyle(color = Color(0xFF8E9BAE), fontSize = 10.sp)

                    // Draw Horizontal Grid Lines & Y-Axis Labels
                    val ySteps = 4
                    for (i in 0..ySteps) {
                        val y = topPad + chartHeight * (i.toFloat() / ySteps)
                        val value = maxVal - (maxVal * (i.toFloat() / ySteps))

                        drawLine(
                            color = gridColor,
                            start = Offset(leftPad, y),
                            end = Offset(width - rightPad, y),
                            strokeWidth = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                        )

                        val yText = String.format(java.util.Locale.US, "%.1f%s", value, unitSuffix)
                        drawText(
                            textMeasurer = textMeasurer,
                            text = yText,
                            topLeft = Offset(4.dp.toPx(), y - 7.dp.toPx()),
                            style = labelStyle
                        )
                    }

                    if (data.size < 2) return@Canvas

                    // Compute Coordinates
                    val points = data.mapIndexed { index, point ->
                        val x = leftPad + (index.toFloat() / (data.size - 1)) * chartWidth
                        val normalized = (point.conversionRate / maxVal).coerceIn(0.0, 1.0)
                        val y = topPad + chartHeight * (1f - normalized.toFloat())
                        Offset(x, y)
                    }

                    // Build Smooth Cubic Spline Path
                    val strokePath = Path().apply {
                        moveTo(points[0].x, points[0].y)
                        for (i in 0 until points.size - 1) {
                            val p0 = points[i]
                            val p1 = points[i + 1]
                            val cx = (p0.x + p1.x) / 2f
                            cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                        }
                    }

                    // Build Area Path
                    val areaPath = Path().apply {
                        addPath(strokePath)
                        lineTo(points.last().x, topPad + chartHeight)
                        lineTo(points.first().x, topPad + chartHeight)
                        close()
                    }

                    // Fill Gradient Area
                    drawPath(
                        path = areaPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                AueraaNeonCyan.copy(alpha = 0.40f),
                                AueraaElectricBlue.copy(alpha = 0.15f),
                                Color.Transparent
                            ),
                            startY = topPad,
                            endY = topPad + chartHeight
                        )
                    )

                    // Stroke Spline Line
                    drawPath(
                        path = strokePath,
                        brush = Brush.horizontalGradient(
                            colors = listOf(AueraaElectricBlue, AueraaNeonCyan)
                        ),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // Draw Data Points
                    points.forEachIndexed { i, pt ->
                        val isSelected = selectedIndex == i
                        drawCircle(
                            color = AueraaDarkBackground,
                            radius = if (isSelected) 6.dp.toPx() else 3.5.dp.toPx(),
                            center = pt
                        )
                        drawCircle(
                            color = if (isSelected) AueraaNeonCyan else AueraaElectricBlue,
                            radius = if (isSelected) 4.5.dp.toPx() else 2.5.dp.toPx(),
                            center = pt
                        )
                    }

                    // Draw X-Axis Labels
                    data.forEachIndexed { index, point ->
                        val x = points[index].x
                        drawText(
                            textMeasurer = textMeasurer,
                            text = point.label,
                            topLeft = Offset(x - 10.dp.toPx(), height - 18.dp.toPx()),
                            style = labelStyle
                        )
                    }

                    // Draw Interactive Hairline Cursor if selected
                    selectedIndex?.let { idx ->
                        if (idx in points.indices) {
                            val pt = points[idx]
                            drawLine(
                                color = AueraaNeonCyan.copy(alpha = 0.8f),
                                start = Offset(pt.x, topPad),
                                end = Offset(pt.x, topPad + chartHeight),
                                strokeWidth = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f), 0f)
                            )
                            // Outer Glow Indicator
                            drawCircle(
                                color = AueraaNeonCyan.copy(alpha = 0.35f),
                                radius = 10.dp.toPx(),
                                center = pt
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 4.dp.toPx(),
                                center = pt
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Native Jetpack Compose Grouped Bar Chart inspired by Recharts (BarChart + Bar).
 * Visualizes Ad Spend (Blue) vs Revenue Generated (Emerald Green) side by side.
 */
@Composable
fun RechartsAdSpendRevenueBarChart(
    data: List<MetricDataPoint>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val textMeasurer = rememberTextMeasurer()

    val maxAmount = remember(data) {
        val peak = data.maxOfOrNull { max(it.adSpendInr, it.revenueInr) } ?: 100000L
        val rounded = ((peak / 10000L) + 1) * 10000L
        max(50000L, rounded)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, AueraaSurfaceBorder, RoundedCornerShape(16.dp))
            .testTag("recharts_ad_spend_bar_chart")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with Recharts Legends
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ad Spend vs Return on Investment",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Campaign expenditure vs client revenue generated",
                        fontSize = 11.sp,
                        color = AueraaTextSecondary
                    )
                }

                // Legends
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(AueraaElectricBlue)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ad Spend", fontSize = 11.sp, color = AueraaTextSecondary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(AueraaSuccessEmerald)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Revenue", fontSize = 11.sp, color = AueraaTextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Active Bar Readout Card
            val activePoint = selectedIndex?.let { data.getOrNull(it) } ?: data.lastOrNull()
            if (activePoint != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(AueraaSurfaceVariant.copy(alpha = 0.7f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${activePoint.dateText} (${activePoint.label})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "Spend: ₹${activePoint.adSpendInr}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AueraaElectricBlue
                        )
                        Text(
                            text = "Revenue: ₹${activePoint.revenueInr}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AueraaSuccessEmerald
                        )
                        Text(
                            text = "ROAS: ${String.format(java.util.Locale.US, "%.1fx", activePoint.roas)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AueraaNeonCyan
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Canvas Dual-Bar Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .pointerInput(data) {
                        detectTapGestures(
                            onPress = { offset ->
                                val count = data.size
                                if (count > 0) {
                                    val leftPad = 45.dp.toPx()
                                    val rightPad = 12.dp.toPx()
                                    val chartWidth = size.width - leftPad - rightPad
                                    val slotWidth = chartWidth / count
                                    val relX = (offset.x - leftPad).coerceIn(0f, chartWidth)
                                    val idx = (relX / slotWidth).toInt().coerceIn(0, count - 1)
                                    selectedIndex = idx
                                }
                            }
                        )
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val leftPad = 45.dp.toPx()
                    val bottomPad = 26.dp.toPx()
                    val topPad = 10.dp.toPx()
                    val rightPad = 12.dp.toPx()

                    val chartWidth = width - leftPad - rightPad
                    val chartHeight = height - topPad - bottomPad

                    val gridColor = Color(0xFF22304A)
                    val labelStyle = TextStyle(color = Color(0xFF8E9BAE), fontSize = 10.sp)

                    // Draw Horizontal Grid Lines & Y-Axis Labels
                    val ySteps = 4
                    for (i in 0..ySteps) {
                        val y = topPad + chartHeight * (i.toFloat() / ySteps)
                        val value = maxAmount - (maxAmount * (i.toFloat() / ySteps))

                        drawLine(
                            color = gridColor,
                            start = Offset(leftPad, y),
                            end = Offset(width - rightPad, y),
                            strokeWidth = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                        )

                        val yText = "₹${value / 1000}k"
                        drawText(
                            textMeasurer = textMeasurer,
                            text = yText,
                            topLeft = Offset(4.dp.toPx(), y - 7.dp.toPx()),
                            style = labelStyle
                        )
                    }

                    val count = data.size
                    val slotWidth = chartWidth / count
                    val barWidth = (slotWidth * 0.32f).coerceAtMost(16.dp.toPx())
                    val barGap = 4.dp.toPx()

                    data.forEachIndexed { i, pt ->
                        val isSelected = selectedIndex == i
                        val slotCenter = leftPad + (i + 0.5f) * slotWidth

                        // Highlighting background slot
                        if (isSelected) {
                            drawRoundRect(
                                color = Color.White.copy(alpha = 0.05f),
                                topLeft = Offset(leftPad + i * slotWidth, topPad),
                                size = Size(slotWidth, chartHeight),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
                            )
                        }

                        // Bar 1: Ad Spend (Electric Blue)
                        val spendFrac = (pt.adSpendInr.toFloat() / maxAmount).coerceIn(0f, 1f)
                        val spendHeight = chartHeight * spendFrac
                        val spendLeft = slotCenter - barWidth - (barGap / 2f)
                        val spendTop = topPad + chartHeight - spendHeight

                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                listOf(AueraaElectricBlue, Color(0xFF0056B3)),
                                startY = spendTop,
                                endY = topPad + chartHeight
                            ),
                            topLeft = Offset(spendLeft, spendTop),
                            size = Size(barWidth, spendHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )

                        // Bar 2: Revenue (Emerald Green)
                        val revFrac = (pt.revenueInr.toFloat() / maxAmount).coerceIn(0f, 1f)
                        val revHeight = chartHeight * revFrac
                        val revLeft = slotCenter + (barGap / 2f)
                        val revTop = topPad + chartHeight - revHeight

                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                listOf(AueraaSuccessEmerald, Color(0xFF059669)),
                                startY = revTop,
                                endY = topPad + chartHeight
                            ),
                            topLeft = Offset(revLeft, revTop),
                            size = Size(barWidth, revHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )

                        // X-Axis Day Label
                        drawText(
                            textMeasurer = textMeasurer,
                            text = pt.label,
                            topLeft = Offset(slotCenter - 10.dp.toPx(), height - 18.dp.toPx()),
                            style = labelStyle
                        )
                    }
                }
            }
        }
    }
}

/**
 * Recharts-inspired Donut / Radial breakdown of Channel Ad Spend.
 */
@Composable
fun RechartsChannelDonutChart(
    modifier: Modifier = Modifier
) {
    val channels = listOf(
        Triple("Meta Ads", 45, Color(0xFF1877F2)),
        Triple("Google PMax", 30, Color(0xFFEA4335)),
        Triple("LinkedIn B2B", 15, Color(0xFF0A66C2)),
        Triple("YouTube", 10, Color(0xFFFF0000))
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, AueraaSurfaceBorder, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ad Spend Distribution by Platform",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circular Canvas Donut
                Box(
                    modifier = Modifier.size(110.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        var startAngle = -90f
                        val stroke = 18.dp.toPx()

                        channels.forEach { (_, pct, color) ->
                            val sweep = 360f * (pct / 100f)
                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = sweep - 2f,
                                useCenter = false,
                                style = Stroke(width = stroke, cap = StrokeCap.Round)
                            )
                            startAngle += sweep
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("100%", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Multi-Ad", fontSize = 9.sp, color = AueraaTextSecondary)
                    }
                }

                // Legend List
                Column(
                    modifier = Modifier.weight(1f).padding(start = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    channels.forEach { (name, pct, color) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(name, fontSize = 12.sp, color = Color.White)
                            }
                            Text(
                                text = "$pct%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = color
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Authentic Canvas Vector Icon for Google Account authentication.
 */
@Composable
fun GoogleBrandIcon(modifier: Modifier = Modifier.size(18.dp)) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val radius = w / 2f

        // Draw Google G geometry using arcs
        val blue = Color(0xFF4285F4)
        val red = Color(0xFFEA4335)
        val yellow = Color(0xFFFBBC05)
        val green = Color(0xFF34A853)
        val stroke = w * 0.22f

        // Blue right arc + horizontal bar
        drawArc(blue, 315f, 90f, false, style = Stroke(stroke, cap = StrokeCap.Square))
        drawLine(blue, Offset(cx - radius * 0.1f, cy), Offset(w, cy), strokeWidth = stroke)

        // Green bottom arc
        drawArc(green, 45f, 90f, false, style = Stroke(stroke, cap = StrokeCap.Square))

        // Yellow left arc
        drawArc(yellow, 135f, 90f, false, style = Stroke(stroke, cap = StrokeCap.Square))

        // Red top arc
        drawArc(red, 225f, 90f, false, style = Stroke(stroke, cap = StrokeCap.Square))
    }
}

/**
 * Authentic Canvas Vector Icon for LinkedIn Professional authentication.
 */
@Composable
fun LinkedInBrandIcon(modifier: Modifier = Modifier.size(18.dp)) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val corner = 3.dp.toPx()

        // LinkedIn Blue Rounded Box
        drawRoundRect(
            color = Color(0xFF0A66C2),
            topLeft = Offset(0f, 0f),
            size = Size(w, h),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(corner, corner)
        )

        // White 'i' dot & stem
        val white = Color.White
        val stroke = w * 0.12f
        val iX = w * 0.28f
        drawCircle(white, radius = stroke * 0.7f, center = Offset(iX, h * 0.30f))
        drawLine(white, Offset(iX, h * 0.44f), Offset(iX, h * 0.75f), strokeWidth = stroke)

        // White 'n'
        val nX = w * 0.50f
        drawLine(white, Offset(nX, h * 0.44f), Offset(nX, h * 0.75f), strokeWidth = stroke)
        drawArc(
            color = white,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(nX - stroke / 2f, h * 0.38f),
            size = Size(w * 0.30f, h * 0.24f),
            style = Stroke(stroke, cap = StrokeCap.Square)
        )
        drawLine(white, Offset(nX + w * 0.30f - stroke / 2f, h * 0.50f), Offset(nX + w * 0.30f - stroke / 2f, h * 0.75f), strokeWidth = stroke)
    }
}
