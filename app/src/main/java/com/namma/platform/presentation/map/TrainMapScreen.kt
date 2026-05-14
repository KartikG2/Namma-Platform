package com.namma.platform.presentation.map

import android.Manifest
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.namma.platform.domain.model.*
import com.namma.platform.ui.theme.*
import com.namma.platform.util.LocationHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainMapScreen(
    onNavigateBack: () -> Unit,
    viewModel: TrainMapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // ─── Google Maps Camera ───
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(12.9716, 77.5946), 10f) // Default: Bengaluru
    }

    // ─── Animated train position ───
    var animatedTrainLat by remember { mutableDoubleStateOf(0.0) }
    var animatedTrainLng by remember { mutableDoubleStateOf(0.0) }

    // ─── User Location ───
    var userLocation by remember { mutableStateOf<android.location.Location?>(null) }
    
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || 
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Permission granted, will start collecting below
        }
    }

    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        
        if (!hasFine && !hasCoarse) {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    LaunchedEffect(Unit) {
        if (LocationHelper.hasLocationPermission(context)) {
            LocationHelper.locationUpdates(context).collect { location ->
                userLocation = location
            }
        }
    }

    // ─── Pulsing animation for markers ───
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseOut),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    // ─── Animate train between last & next station ───
    LaunchedEffect(uiState.trainPosition) {
        val pos = uiState.trainPosition ?: return@LaunchedEffect
        val lastLat = pos.lastStation.lat
        val lastLng = pos.lastStation.lng
        val nextLat = pos.nextStation.lat
        val nextLng = pos.nextStation.lng

        if (lastLat != 0.0 && lastLng != 0.0) {
            animatedTrainLat = lastLat
            animatedTrainLng = lastLng

            // Animate from last to next station over 5 minutes
            if (nextLat != 0.0 && nextLng != 0.0) {
                val animator = ValueAnimator.ofFloat(0f, 1f).apply {
                    duration = 5 * 60 * 1000L
                    addUpdateListener { anim ->
                        val fraction = anim.animatedFraction
                        animatedTrainLat = lastLat + (nextLat - lastLat) * fraction
                        animatedTrainLng = lastLng + (nextLng - lastLng) * fraction
                    }
                }
                animator.start()
            }
        }
    }

    // ─── Recenter camera when user taps FAB ───
    LaunchedEffect(uiState.recenterTrigger) {
        if (uiState.recenterTrigger > 0 && animatedTrainLat != 0.0) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(animatedTrainLat, animatedTrainLng), 13f
                ),
                durationMs = 800
            )
        }
    }

    // ─── Auto-center on first data load ───
    var hasInitiallyAnimated by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.trainPosition) {
        if (!hasInitiallyAnimated && uiState.trainPosition != null) {
            val pos = uiState.trainPosition!!
            if (pos.lastStation.lat != 0.0) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(pos.lastStation.lat, pos.lastStation.lng), 10f
                    ),
                    durationMs = 1200
                )
                hasInitiallyAnimated = true
            }
        }
    }

    // ─── Dark map style JSON ───
    val mapStyleJson = remember {
        """
        [
          { "elementType": "geometry", "stylers": [{ "color": "#1d2c4d" }] },
          { "elementType": "labels.text.fill", "stylers": [{ "color": "#8ec3b9" }] },
          { "elementType": "labels.text.stroke", "stylers": [{ "color": "#1a3646" }] },
          { "featureType": "administrative.country", "elementType": "geometry.stroke", "stylers": [{ "color": "#4b6878" }] },
          { "featureType": "land_parcel", "elementType": "labels.text.fill", "stylers": [{ "color": "#64779e" }] },
          { "featureType": "poi", "elementType": "geometry", "stylers": [{ "color": "#283d6a" }] },
          { "featureType": "poi", "elementType": "labels.text.fill", "stylers": [{ "color": "#6f9ba5" }] },
          { "featureType": "poi.park", "elementType": "geometry.fill", "stylers": [{ "color": "#023e58" }] },
          { "featureType": "poi.park", "elementType": "labels.text.fill", "stylers": [{ "color": "#3C7680" }] },
          { "featureType": "road", "elementType": "geometry", "stylers": [{ "color": "#304a7d" }] },
          { "featureType": "road", "elementType": "labels.text.fill", "stylers": [{ "color": "#98a5be" }] },
          { "featureType": "road.highway", "elementType": "geometry", "stylers": [{ "color": "#2c6675" }] },
          { "featureType": "road.highway", "elementType": "geometry.stroke", "stylers": [{ "color": "#255763" }] },
          { "featureType": "road.highway", "elementType": "labels.text.fill", "stylers": [{ "color": "#b0d5ce" }] },
          { "featureType": "transit", "elementType": "labels.text.fill", "stylers": [{ "color": "#98a5be" }] },
          { "featureType": "transit.line", "elementType": "geometry.fill", "stylers": [{ "color": "#E25B2D" }] },
          { "featureType": "transit.station", "elementType": "geometry", "stylers": [{ "color": "#3a4762" }] },
          { "featureType": "water", "elementType": "geometry", "stylers": [{ "color": "#0e1626" }] },
          { "featureType": "water", "elementType": "labels.text.fill", "stylers": [{ "color": "#4e6d70" }] }
        ]
        """.trimIndent()
    }

    val mapProperties = remember(mapStyleJson) {
        MapProperties(
            mapStyleOptions = MapStyleOptions(mapStyleJson),
            isMyLocationEnabled = false,
            mapType = MapType.NORMAL,
            minZoomPreference = 5f,
            maxZoomPreference = 18f
        )
    }

    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = true,
            myLocationButtonEnabled = false,
            rotationGesturesEnabled = true,
            tiltGesturesEnabled = true
        )
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 220.dp,
        sheetContainerColor = Color(0xFF1A1A2E),
        sheetContentColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetDragHandle = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Train Tracking",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val boardingName = uiState.boardingStation?.nameEnglish ?: "Unknown"
                            val nextName = uiState.trainPosition?.nextStation?.nameEnglish ?: "..."
                            
                            Text(
                                text = "From: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                text = boardingName,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF81C784) // Light Green for boarding
                            )
                            Text(
                                text = " • To: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                text = nextName,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F0F23),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        sheetContent = {
            val currentStation = uiState.trainPosition?.lastStation
            val delayMins = uiState.trainPosition?.delayMinutes ?: 0
            
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                // Large Station Name Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Reached ${currentStation?.nameEnglish ?: "Station"}",
                            color = Color(0xFFC62828), // Professional Red
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentStation?.nameKannada ?: "",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    
                    // Refresh Button
                    IconButton(
                        onClick = { viewModel.onRecenterTapped() },
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(Icons.Default.MyLocation, "Refresh", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Delay Badge and Timestamp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Delay Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFFFF3E0), // Orange background
                        border = BorderStroke(1.dp, Color(0xFFFFB74D))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (delayMins > 0) Color(0xFFE65100) else Color(0xFF4CAF50))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (delayMins > 0) "Delayed by $delayMins mins" else "On Time",
                                color = if (delayMins > 0) Color(0xFFE65100) else Color(0xFF2E7D32),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Timestamp
                    Text(
                        text = if (uiState.lastUpdatedMinutes == 0) "Just now" else "${uiState.lastUpdatedMinutes} mins ago",
                        color = Color.White.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 20.dp),
                    color = Color.White.copy(alpha = 0.1f)
                )

                // Upcoming Stations Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Train,
                        contentDescription = null,
                        tint = Color(0xFF81C784),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "NEXT STOPS",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                    itemsIndexed(uiState.upcomingStations) { index, stationEta ->
                        UpcomingStationRow(stationEta, isNext = index == 0)
                    }
                }
            }
        }

    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            // ─── Google Map ───
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings
            ) {

                // ─── Railway Route Polyline ───
                val routeCoordinates = uiState.trainPosition?.fullRoute?.map { 
                    LatLng(it.station.lat, it.station.lng) 
                } ?: emptyList()

                if (routeCoordinates.isNotEmpty()) {
                    Polyline(
                        points = routeCoordinates,
                        width = 10f,
                        color = Color(0xFF37474F), // Dark grey charcoal
                        jointType = JointType.ROUND,
                        startCap = RoundCap(),
                        endCap = RoundCap(),
                        zIndex = 1f
                    )
                }

                // ─── Station Markers ───
                uiState.trainPosition?.fullRoute?.forEach { routeStn ->
                    val station = routeStn.station
                    val isPassed = routeStn.status == StationStatus.PASSED
                    val isCurrent = routeStn.status == StationStatus.CURRENT
                    
                    Marker(
                        state = MarkerState(position = LatLng(station.lat, station.lng)),
                        title = station.nameEnglish,
                        snippet = station.nameKannada,
                        icon = if (isCurrent) {
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        } else {
                            BitmapDescriptorFactory.defaultMarker(
                                if (isPassed) BitmapDescriptorFactory.HUE_CYAN else BitmapDescriptorFactory.HUE_RED
                            )
                        },
                        alpha = if (isPassed) 0.5f else 1.0f,
                        zIndex = if (isCurrent) 10f else 5f
                    )
                }

                // ─── Live Train Marker ───
                if (animatedTrainLat != 0.0 && animatedTrainLng != 0.0) {
                    Marker(
                        state = MarkerState(position = LatLng(animatedTrainLat, animatedTrainLng)),
                        title = "Train ${uiState.trainNo}",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                        zIndex = 20f
                    )
                    
                    // Pulsing Dot
                    Circle(
                        center = LatLng(animatedTrainLat, animatedTrainLng),
                        radius = 1000.0,
                        fillColor = Color.Red.copy(alpha = pulseAlpha),
                        strokeColor = Color.Red.copy(alpha = pulseAlpha * 1.5f),
                        strokeWidth = 2f
                    )
                }

            }

            // ─── Premium Journey Status Card ───
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF0F0F23).copy(alpha = 0.85f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                    shadowElevation = 12.dp
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (uiState.lastUpdatedMinutes < 3) Color(0xFF81C784) else Color(0xFFFFB74D))
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (uiState.lastUpdatedMinutes == 0) "Live Tracking" else "Updated ${uiState.lastUpdatedMinutes}m ago",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f),
                                letterSpacing = 1.sp
                            )
                        }
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp).width(40.dp),
                            color = Color.White.copy(alpha = 0.1f)
                        )
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val boardingName = uiState.boardingStation?.nameEnglish ?: "..."
                            val nextName = uiState.trainPosition?.nextStation?.nameEnglish ?: "..."
                            
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("BOARDING", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.4f), fontSize = 9.sp)
                                Text(boardingName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF81C784))
                            }
                            
                            Icon(
                                Icons.Default.Train, 
                                contentDescription = null, 
                                modifier = Modifier.padding(horizontal = 20.dp).size(20.dp),
                                tint = Color.White.copy(alpha = 0.2f)
                            )
                            
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("NEXT STOP", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.4f), fontSize = 9.sp)
                                Text(nextName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            // ─── Delay badge ───
            uiState.trainPosition?.let { pos ->
                if (pos.delayMinutes > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 12.dp, top = 56.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = ErrorRed.copy(alpha = 0.9f),
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = "⚠ ${pos.delayMinutes} min late",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ─── Recenter & User Location FABs ───
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .padding(bottom = 60.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        userLocation?.let { loc ->
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(LatLng(loc.latitude, loc.longitude), 14f),
                                    durationMs = 800
                                )
                            }
                        } ?: run {
                            locationPermissionLauncher.launch(
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                            )
                        }
                    },
                    modifier = Modifier.shadow(12.dp, CircleShape),
                    containerColor = Color(0xFF2C2C4E),
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.DirectionsWalk,
                        contentDescription = "My Location",
                        modifier = Modifier.size(24.dp)
                    )
                }

                FloatingActionButton(
                    onClick = { viewModel.onRecenterTapped() },
                    modifier = Modifier.shadow(12.dp, CircleShape),
                    containerColor = Color(0xFF1A1A2E),
                    contentColor = PrimaryOrange,
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = "Recenter Map",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedCoachItem(coachCode: String, isEngine: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "coach_bob")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -3f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(y = offsetY.dp)
    ) {
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(35.dp)
                .clip(RoundedCornerShape(topStart = if(isEngine) 16.dp else 4.dp, topEnd = 4.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                .background(if (isEngine) ErrorRed else CoachBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if(isEngine) "🚂" else coachCode,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = if(isEngine) 20.sp else 12.sp
            )
        }
        Row(
            modifier = Modifier.width(40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Gray))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Gray))
        }
    }
}


@Composable
fun UpcomingStationRow(stationEta: StationEta, isNext: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isNext) Modifier.background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryOrange.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) else Modifier
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Station indicator dot
            Box(
                modifier = Modifier
                    .size(if (isNext) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (isNext) PrimaryOrange else Color.White.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stationEta.station.nameKannada,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal,
                    color = if (isNext) Color.White else Color.White.copy(alpha = 0.7f)
                )
                Text(
                    text = stationEta.station.nameEnglish,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${stationEta.etaMinutes}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (isNext) PrimaryOrange else Color.White.copy(alpha = 0.8f)
            )
            Text(
                "mins",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.4f)
            )
        }
    }
}
