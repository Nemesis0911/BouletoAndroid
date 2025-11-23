package com.example.bouleto.vues

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import com.example.bouleto.models.Membre
import com.example.bouleto.models.Point
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class CustomInfoBulle(
    private val context: Context,
    mapView: MapView,
    private val membre: Membre,
    private val point: Point
) : InfoWindow(
    FrameLayout(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    },
    mapView
) {

    override fun onOpen(item: Any?) {
        // ✅ Vide le conteneur avant d'ajouter la nouvelle vue
        (mView as? ViewGroup)?.removeAllViews()

        // 🎨 Crée le ComposeView
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme {
                    BulleInfoMarker(
                        nom = membre.nom,
                        prenom = membre.prenom,
                        adresse = point.latidute.toString(),
                        points = point.score
                    )
                }
            }
        }

        // ✅ Ajoute le ComposeView au conteneur
        (mView as? ViewGroup)?.addView(composeView)

        // ✅ Rend la vue visible
        mView.visibility = View.VISIBLE
    }

    override fun onClose() {
        // ✅ Cache et nettoie la vue
        (mView as? ViewGroup)?.removeAllViews()
        mView.visibility = View.GONE
    }
}
