package rocket.graphql

import com.apollographql.apollo3.ApolloClient

object RocketClient {
    private val API_URL = "https://apollo-fullstack-tutorial.herokuapp.com/graphql"

    // Creamos una instancia de Apollo
    fun getInstance(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(API_URL)
            .build()
    }
}