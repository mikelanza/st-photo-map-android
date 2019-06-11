package com.streetography.stphotomap.operations.network.geojson_tile

import com.streetography.stphotomap.models.geojson.GeoJSON
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

    override fun run() {
        super.run()

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
                    operationCompletionHandler.onSuccess(GetGeojsonTileOperationModel.Response(geoJSONObject))
                } catch (error: JSONException) {
                    operationCompletionHandler.onFailure(OperationError.CANNOT_PARSE_RESPONSE)
                }

                response.body()?.close()
            }

            override fun onFailure(call: Call, e: IOException) {
                operationCompletionHandler.onFailure(OperationError.NO_INTERNET_CONNECTION)
            }
        })
    }

    override fun cancel() {
        super.cancel()
        this.call?.get()?.cancel()
    }

    private fun shouldCancelOperation(): Boolean {
        if (this.isCancelled) {
            this.operationCompletionHandler.onFailure(OperationError.OPERATION_CANCELLED)
            return true
        }
        return false
    }
}