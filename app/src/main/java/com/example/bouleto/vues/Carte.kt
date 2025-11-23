package com.example.bouleto.vues

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.bouleto.MainViewmodel
import com.example.bouleto.models.Membre
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import com.example.bouleto.models.Point
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.MapTileIndex


//// 🎨 Style de carte sombre
//val DARK_MAP_TILE_SOURCE = XYTileSource(
//    "CartoDarkMatter",
//    0, 19, 256, ".png",
//    arrayOf(
//        "https://a.basemaps.cartocdn.com/dark_all/",
//        "https://b.basemaps.cartocdn.com/dark_all/",
//        "https://c.basemaps.cartocdn.com/dark_all/",
//        "https://d.basemaps.cartocdn.com/dark_all/"
//    ),
//    "© OpenStreetMap contributors © CARTO"
//)

// 🔧 Fonction pour redimensionner les icônes
fun resizeDrawable(drawable: Drawable, width: Int, height: Int): BitmapDrawable {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    val scaled = Bitmap.createScaledBitmap(bitmap, width, height, true)
    return BitmapDrawable(null, scaled)
}
private val LIGHT_MAP_TILE_SOURCE = object : OnlineTileSourceBase(
    "CartoCDBPositron",
    0, 20, 256, ".png",
    arrayOf(
        "https://a.basemaps.cartocdn.com/light_all/",
        "https://b.basemaps.cartocdn.com/light_all/",
        "https://c.basemaps.cartocdn.com/light_all/"
    )
) {
    override fun getTileURLString(pMapTileIndex: Long): String {
        return (baseUrl + MapTileIndex.getZoom(pMapTileIndex)
                + "/" + MapTileIndex.getX(pMapTileIndex)
                + "/" + MapTileIndex.getY(pMapTileIndex)
                + mImageFilenameEnding)
    }
}
@Composable
fun Carte(
    membres: List<Membre> = emptyList(),
    modifier: Modifier = Modifier,
    viewModel: MainViewmodel
) {
    val context = LocalContext.current
    var mapView by remember { mutableStateOf<MapView?>(null) }
    val iconCache = remember { mutableMapOf<String, BitmapDrawable>() }

    // Configuration OSMDroid
    DisposableEffect(Unit) {
        Configuration.getInstance().apply {
            userAgentValue = context.packageName
            osmdroidBasePath = context.filesDir
            osmdroidTileCache = context.cacheDir
        }
        onDispose { }
    }

    // 🎯 FONCTION : Calculer la taille selon le zoom
    fun getMarkerSizeForZoom(zoomLevel: Double): Int {
        return when {
            zoomLevel < 5 -> 40
            zoomLevel < 8 -> 60
            zoomLevel < 12 -> 80
            zoomLevel < 15 -> 100
            else -> 120
        }
    }

    // 🎯 FONCTION : Créer icône redimensionnée
    fun createResizedIcon(drawableId: Int, size: Int): BitmapDrawable {
        val cacheKey = "${drawableId}_$size"

        return iconCache.getOrPut(cacheKey) {
            try {
                val drawable = ContextCompat.getDrawable(context, drawableId)
                val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable?.setBounds(0, 0, size, size)
                drawable?.draw(canvas)
                BitmapDrawable(context.resources, bitmap)
            } catch (e: Exception) {
                Log.e("Carte", "Erreur création icône: ${e.message}")
                BitmapDrawable(context.resources, Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888))
            }
        }
    }

    // ✅ BOX AVEC CLIP POUR EMPÊCHER LE DÉBORDEMENT
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        // 🗺️ CARTE
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds(),
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(LIGHT_MAP_TILE_SOURCE)
                    setMultiTouchControls(true)

                    zoomController.setVisibility(
                        org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER
                    )

                    controller.setZoom(5.0)
                    controller.setCenter(GeoPoint(48.8566, 2.3522))

                    isTilesScaledToDpi = true
                    minZoomLevel = 3.0
                    maxZoomLevel = 19.0

                    isHorizontalMapRepetitionEnabled = true
                    isVerticalMapRepetitionEnabled = false

                    // 🎯 ÉCOUTEUR DE ZOOM
                    addMapListener(object : MapListener {
                        override fun onScroll(event: ScrollEvent?): Boolean = false

                        override fun onZoom(event: ZoomEvent?): Boolean {
                            event?.let {
                                Log.d("Carte", "Zoom: ${it.zoomLevel}")
                                invalidate()
                            }
                            return true
                        }
                    })

                    mapView = this
                }
            },
            update = { map ->
                map.overlays.removeAll { it is Marker }

                val currentZoom = map.zoomLevelDouble
                val markerSize = getMarkerSizeForZoom(currentZoom)

                membres.forEach { membre ->
                    membre.point.forEach { point ->
                        val lat = point.latidute
                        val lon = point.longitude

                        val marker = Marker(map).apply {
                            position = GeoPoint(lat, lon)
                            title = membre.pseudo.uppercase()
                            snippet = "${point.score}" + " points"
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                            // 🎯 ICÔNE REDIMENSIONNÉE SELON LE ZOOM
                            icon = createResizedIcon(
                                android.R.drawable.ic_menu_mylocation,
                                markerSize
                            )
                            infoWindow = CustomInfoBulle(context, map, membre, point)

                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                            // 👆 CLIC pour ouvrir la bulle
                            setOnMarkerClickListener { clickedMarker, _ ->
                                // Ferme toutes les autres bulles
                                map.overlays.filterIsInstance<Marker>()
                                    .forEach { it.closeInfoWindow() }

                                // Ouvre celle-ci
                                clickedMarker.showInfoWindow()
                                true
                            }
                        }

                        map.overlays.add(marker)
                    }
                }

                map.invalidate()
            }
        )

        // 🎮 CONTRÔLES DE ZOOM
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = { mapView?.controller?.zoomIn() },
                containerColor = Color.White,
                contentColor = Color(0xFFFFB300)
            ) {
                Icon(Icons.Default.KeyboardArrowUp, "Zoom avant")
            }

            FloatingActionButton(
                onClick = { mapView?.controller?.zoomOut() },
                containerColor = Color.White,
                contentColor = Color(0xFFFFB300)
            ) {
                Icon(Icons.Default.KeyboardArrowDown, "Zoom arrière")
            }

            FloatingActionButton(
                onClick = {
                    mapView?.controller?.animateTo(GeoPoint(43.36429, 2.14203))
                },
                containerColor = Color(0xFFFFB300),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Place, "Recentrer")
            }
        }
    }
}

@Composable
fun BulleInfoMarker(
    membre: Membre,
    point : Point,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(250.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(membre.couleur))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 👤 Pseudo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(membre.couleur),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "${membre.pseudo} ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }

            // 📍 ADRESSE
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "" + point.latidute + point.longitude,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666)
                )
            }

            // ⭐ POINTS
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(membre.couleur).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(membre.couleur),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "" + point.score + " points",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(membre.couleur)
                )
            }
        }
    }
}

