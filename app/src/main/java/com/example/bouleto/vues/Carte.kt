package com.example.bouleto.vues

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
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
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.draw.clipToBounds


// 🎨 Style de carte sombre
val DARK_MAP_TILE_SOURCE = XYTileSource(
    "CartoDarkMatter",
    0, 19, 256, ".png",
    arrayOf(
        "https://a.basemaps.cartocdn.com/dark_all/",
        "https://b.basemaps.cartocdn.com/dark_all/",
        "https://c.basemaps.cartocdn.com/dark_all/",
        "https://d.basemaps.cartocdn.com/dark_all/"
    ),
    "© OpenStreetMap contributors © CARTO"
)

// 🔧 Fonction pour redimensionner les icônes
fun resizeDrawable(drawable: Drawable, width: Int, height: Int): BitmapDrawable {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    val scaled = Bitmap.createScaledBitmap(bitmap, width, height, true)
    return BitmapDrawable(null, scaled)
}

@Composable
fun Carte(
    membres: List<Membre> = emptyList(),
    modifier: Modifier = Modifier,
    viewModel: MainViewmodel
) {
    val context = LocalContext.current
    var mapView by remember { mutableStateOf<MapView?>(null) }
    val iconCache = remember { mutableMapOf<Int, BitmapDrawable>() }

    // Configuration OSMDroid
    DisposableEffect(Unit) {
        Configuration.getInstance().apply {
            userAgentValue = context.packageName
            osmdroidBasePath = context.filesDir
            osmdroidTileCache = context.cacheDir
        }
        onDispose { }
    }

    // ✅ BOX AVEC CLIP POUR EMPÊCHER LE DÉBORDEMENT
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()  // 🔥 CLEF : Empêche la carte de déborder
    ) {
        // 🗺️ CARTE
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds(),  // 🔥 Double sécurité
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(DARK_MAP_TILE_SOURCE)
                    setMultiTouchControls(true)
                    controller.setZoom(5.0)
                    controller.setCenter(GeoPoint(48.8566, 2.3522))

                    isTilesScaledToDpi = true
                    minZoomLevel = 3.0
                    maxZoomLevel = 19.0

                    // ✅ IMPORTANT : Désactiver le scroll fling agressif
                    isHorizontalMapRepetitionEnabled = false
                    isVerticalMapRepetitionEnabled = false
                    setScrollableAreaLimitLatitude(
                        MapView.getTileSystem().maxLatitude,
                        MapView.getTileSystem().minLatitude,
                        0
                    )

                    mapView = this
                }
            },
            update = { map ->
                map.overlays.clear()

                membres.forEach { membre ->
                    membre.point.forEach { point ->
                        val lat = point.latidute
                        val lon = point.longitude

                        val marker = Marker(map).apply {
                            position = GeoPoint(lat, lon)
                            title = membre.nom
                            snippet = "${membre.nom.uppercase()} ${membre.prenom}"
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                            try {
                                val drawable = ContextCompat.getDrawable(
                                    map.context,
                                    android.R.drawable.ic_menu_mylocation
                                )
                                drawable?.let { d ->
                                    val zoom = map.zoomLevelDouble
                                    val iconSize = (400 * (zoom / 10.0)).toInt().coerceIn(30, 80)

                                    val cached = iconCache[iconSize] ?: run {
                                        val resized = resizeDrawable(d, iconSize, iconSize)
                                        iconCache[iconSize] = resized
                                        resized
                                    }

                                    icon = cached
                                }
                            } catch (e: Exception) {
                                android.util.Log.e("Carte", "Erreur icône", e)
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
                Icon(Icons.Default.Add, "Zoom avant")
            }

            FloatingActionButton(
                onClick = { mapView?.controller?.zoomOut() },
                containerColor = Color.White,
                contentColor = Color(0xFFFFB300)
            ) {
                Icon(Icons.Default.Close, "Zoom arrière")
            }

            FloatingActionButton(
                onClick = {
                    mapView?.controller?.animateTo(GeoPoint(48.8566, 2.3522))
                },
                containerColor = Color(0xFFFFB300),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Place, "Recentrer")
            }
        }
    }
}
