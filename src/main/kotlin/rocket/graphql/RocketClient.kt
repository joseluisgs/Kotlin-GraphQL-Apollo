package rocket.graphql

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

object RocketClient {
    private val API_URL = "https://apollo-fullstack-tutorial.herokuapp.com/graphql"

    // Creamos una instancia de Apollo
    fun getInstance(token: String? = null): ApolloClient {

        // Añadimos un cliente okHttp para poder procesar las peticiones con token de autenticación
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(token))
            .build()

        // Devolvemos el cliente Apollo
        return ApolloClient.Builder()
            .serverUrl(API_URL)
            // Le metemos un websocket para las suscripciones
            .webSocketServerUrl("wss://apollo-fullstack-tutorial.herokuapp.com/graphql") // Opcional, solo suscripciones
            .okHttpClient(okHttpClient) // Opcional, solo por si queremos tocken o enviar en el header
            .build()
    }
}

/**
 * Para añadir Header para tokens, mos creamos un interceptador
 */
private class AuthorizationInterceptor(val token: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            //addHeader("Authorization", "Bearer $token") --> Si es del tipo Bearer Token
            .addHeader("Authorization", token ?: "")
            .build()

        return chain.proceed(request)
    }
}