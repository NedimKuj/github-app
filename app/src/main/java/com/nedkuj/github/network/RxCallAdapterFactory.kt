package com.nedkuj.github.network

import com.squareup.moshi.JsonDataException
import com.nedkuj.github.exception.NoInternetException
import com.nedkuj.github.exception.SerializationException
import com.nedkuj.github.exception.ApiErrorException
import com.nedkuj.github.exception.GenericException
import com.nedkuj.github.exception.UnauthorizedException
import com.nedkuj.github.exception.UnknownException
import com.nedkuj.github.model.base.ErrorMessageEnum
import com.nedkuj.github.model.base.ErrorResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.net.UnknownHostException

class RxCallAdapterFactory private constructor() : CallAdapter.Factory() {
  private val original: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

  override fun get(
      returnType: Type,
      annotations: Array<Annotation>,
      retrofit: Retrofit
  ): CallAdapter<*, *> {
    return RxCallAdapterWrapper(
        retrofit, original.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>
    )
  }

  private inner class RxCallAdapterWrapper constructor(
      private val retrofit: Retrofit,
      private val wrapped: CallAdapter<Any, Any>
  ) : CallAdapter<Any, Any> {
    override fun adapt(call: Call<Any>): Any {
      val adapted = wrapped.adapt(call)

      return when (adapted) {
        is Maybe<*>      -> adapted.onErrorResumeNext { it: Throwable -> Maybe.error(asGenericException(retrofit, it)) }
        is Single<*>     -> adapted.onErrorResumeNext { Single.error(asGenericException(retrofit, it)) }
        is Observable<*> -> adapted.onErrorResumeNext { it: Throwable -> Observable.error(asGenericException(retrofit, it)) }
        is Flowable<*>   -> adapted.onErrorResumeNext { it: Throwable -> Flowable.error(asGenericException(retrofit, it)) }
        is Completable   -> adapted.onErrorResumeNext { Completable.error(asGenericException(retrofit, it)) }
        else             -> adapted
      }

    }

    override fun responseType(): Type = wrapped.responseType()
  }

  private fun asGenericException(
      retrofit: Retrofit,
      throwable: Throwable
  ): GenericException {

    return if (throwable is HttpException) {
      when (throwable.code()) {
        401, 404         -> UnauthorizedException(throwable, throwable.code())
        400, in 403..499 -> {
            ApiErrorException(
                getErrorBodyAs(retrofit, throwable.response())?.error?.message ?: ErrorMessageEnum.SOMETHING_WENT_WRONG,
                throwable
            )
        }
        in 501..600      -> ApiErrorException(ErrorMessageEnum.SERVICE_TEMP_UNAVAILABLE, throwable)
        else             -> ApiErrorException(ErrorMessageEnum.SOMETHING_WENT_WRONG, throwable)
      }
    } else if (throwable is UnknownHostException) {
      NoInternetException(throwable = throwable)
    } else if ((throwable is IOException) or (throwable is JsonDataException)) {
      SerializationException(throwable = throwable)
    } else {
      UnknownException(throwable = throwable)
    }
  }

  private fun getErrorBodyAs(
      retrofit: Retrofit,
      response: Response<*>?
  ): ErrorResponse? {

    val converter: Converter<ResponseBody, ErrorResponse> = retrofit.responseBodyConverter(
        ErrorResponse::class.java, arrayOfNulls<Annotation>(0)
    )

    return response?.errorBody()
        ?.let { converter.convert(it) }
  }

  companion object {
    fun create(): RxCallAdapterFactory = RxCallAdapterFactory()
  }
}
