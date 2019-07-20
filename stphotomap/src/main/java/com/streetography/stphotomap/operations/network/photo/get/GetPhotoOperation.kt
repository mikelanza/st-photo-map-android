package com.streetography.stphotomap.operations.network.photo.get

import com.streetography.stphotomap.models.photo.STPhoto
import com.streetography.stphotomap.operations.base.errors.OperationError
import com.streetography.stphotomap.operations.base.operations.Operation
import com.streetography.stphotomap.operations.base.results.OperationResult
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference

class GetPhotoOperation(val model: GetPhotoOperationModel.Request, val operationCompletionHandler: OperationResult<GetPhotoOperationModel.Response>): Operation() {
    private var call: WeakReference<Call>? = null

    override fun run(completion: (() -> Unit)?) {
        super.run(completion)

        if (this.shouldCancelOperation()) return

        val client = OkHttpClient()
        val request = GetPhotoOperationRequestBuilder(this.model).request()

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
                    val photo = parsePhoto(jsonObject)
                    shouldCompleteOperationWithSuccess(photo)
                } catch (e: Exception) {
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
        this.shouldCancelOperation()
    }

    private fun shouldCancelOperation(): Boolean {
        if (this.isCancelled) {
            this.shouldCompleteOperationWithFailure(OperationError.OPERATION_CANCELLED)
            return true
        }
        return false
    }

    private fun shouldCompleteOperationWithSuccess(photo: STPhoto) {
        this.operationCompletionHandler.onSuccess(GetPhotoOperationModel.Response(photo))
        this.completion?.invoke()
    }

    private fun shouldCompleteOperationWithFailure(error: OperationError) {
        this.operationCompletionHandler.onFailure(error)
        this.completion?.invoke()
    }

    private fun parsePhoto(jsonObject: JSONObject): STPhoto {
        if (!jsonObject.has(Parameters.Photo.value)) {
            throw Exception(OperationError.CANNOT_PARSE_RESPONSE.errorMessage)
        }
        val photoObject = jsonObject.getJSONObject(Parameters.Photo.value)
        return STPhoto.fromJSON(photoObject)
    }

    enum class Parameters(val value: String) {
        Photo("photo")
    }
}