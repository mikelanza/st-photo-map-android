package com.streetography.stphotomap.scenes.stphotomap

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.gms.maps.model.TileProvider
import com.google.android.gms.maps.model.UrlTileProvider
import com.streetography.stphotomap.R
import com.streetography.stphotomap.scenes.stphotomap.interactor.STPhotoMapBusinessLogic
import com.streetography.stphotomap.scenes.stphotomap.interactor.STPhotoMapInteractor
import java.lang.ref.WeakReference
import java.net.MalformedURLException
import java.net.URL
import java.util.*

interface STPhotoMapDisplayLogic {
}

public class STPhotoMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr),
    STPhotoMapDisplayLogic, OnMapReadyCallback {
    var interactor: STPhotoMapBusinessLogic? = null

    var mapView: GoogleMap? = null
    var supportMapFragment: SupportMapFragment? = null

    init {
        this.setup()

        inflate(context, R.layout.st_photo_map_view, null)

        this.findViews(context)
        this.setupViews()
    }

    private fun setup() {
        val interactor = STPhotoMapInteractor()
        val presenter = STPhotoMapPresenter()

        presenter.displayer = WeakReference(this)
        interactor.presenter = presenter

        this.interactor = interactor
    }

    private fun findViews(context: Context?) {
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        this.supportMapFragment = fragmentManager.findFragmentById(R.id.supportMapFragment) as? SupportMapFragment
    }

    private fun setupViews() {
        this.setupMap()
    }

    private fun setupMap() {
        this.supportMapFragment?.getMapAsync(this)
    }

    //region Google map interfaces
    override fun onMapReady(googleMap: GoogleMap) {
        this.mapView = googleMap
        this.mapView?.addTileOverlay(getTileOverlayOptions())
    }
    //endregion

    //region Tile overlay & provider
    private fun getTileOverlayOptions(): TileOverlayOptions {
        return TileOverlayOptions().tileProvider(getTileProvider()).transparency(0.0f)
    }

    private fun getTileProvider(): TileProvider {
        return object : UrlTileProvider(512, 512) {
            override fun getTileUrl(x: Int, y: Int, zoom: Int): URL {
                val s = String.format(
                    Locale.getDefault(),
                    "https://tilesdev.streetography.com/tile/%d/%d/%d.jpeg",
                    zoom, x, y
                )

                Log.i("STPhotoMapView", String.format("x=%d, y=%d z=%d", x, y, zoom));

                try {
                    return URL(s)
                } catch (e: MalformedURLException) {
                    throw AssertionError(e)
                }
            }
        }
    }
    //endregion
}