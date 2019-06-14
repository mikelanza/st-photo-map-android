package com.streetography.stphotomap.operations.network.geojson_tile

import com.streetography.stphotomap.models.geojson.GeoJSON
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.operations.base.errors.OperationError
import com.streetography.stphotomap.operations.base.operations.Operation
import com.streetography.stphotomap.operations.base.results.OperationResult
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference

class GetGeojsonTileOperation(
    val model: GetGeojsonTileOperationModel.Request,
    val operationCompletionHandler: OperationResult<GetGeojsonTileOperationModel.Response>
) : Operation() {
    private var call: WeakReference<Call>? = null

    override fun run(completion: (() -> Unit)?) {
        super.run(completion)

        if (this.shouldCancelOperation()) return

        val client = OkHttpClient()
        val request = GetGeojsonTileOperationRequestBuilder(this.model).request()

        this.call = WeakReference(client.newCall(request))
        this.call?.get()?.enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                if (shouldCancelOperation()) {
                    response.body()?.close()
                    return
                }

                val jsonData = response.body()?.string()
                try {
                    val jsonObject = JSONObject(jsonData)
                    val geoJSONObject = GeoJSON().parse(jsonObject)
                    shouldCompleteOperationWithSuccess(geoJSONObject)
                } catch (error: JSONException) {
                    shouldCompleteOperationWithFailure(OperationError.CANNOT_PARSE_RESPONSE)
                }

                response.body()?.close()
            }

            override fun onFailure(call: Call, e: IOException) {
                shouldCompleteOperationWithFailure(OperationError.NO_INTERNET_CONNECTION)
            }
        })
    }

    override fun cancel() {
        super.cancel()
        this.call?.get()?.cancel()
    }

    private fun shouldCancelOperation(): Boolean {
        if (this.isCancelled) {
            this.shouldCompleteOperationWithFailure(OperationError.OPERATION_CANCELLED)
            return true
        }
        return false
    }

    private fun shouldCompleteOperationWithSuccess(geojsonObject: GeoJSONObject) {
        this.operationCompletionHandler.onSuccess(GetGeojsonTileOperationModel.Response(geojsonObject))
        this.completion?.invoke()
    }

    private fun shouldCompleteOperationWithFailure(error: OperationError) {
        this.operationCompletionHandler.onFailure(error)
        this.completion?.invoke()
    }
}