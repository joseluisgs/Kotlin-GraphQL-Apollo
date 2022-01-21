package rocket

import com.apollographql.apollo3.exception.ApolloException
import com.google.gson.GsonBuilder
import graphql.com.models.LaunchDetailsQuery
import graphql.com.models.LaunchListQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import rocket.Rocket.getLaunchList
import rocket.graphql.RocketClient


object Rocket {
    val graphQLClient = RocketClient.getInstance()

    // Al hacerlo run blocking todo termina cuando todas las funciones suspendidas terminen
    // coroutineScope permite ejecutar una serie de funciones suspendidas y trasforma esta en suspendida
    fun run() = runBlocking {
        println("--------------------------------------------------------------------------------")
        println("API GraphQL Rocket.in - https://apollo-fullstack-tutorial.herokuapp.com/graphql")
        println("--------------------------------------------------------------------------------")

        // Consultas a la API GraphQL Lauches todo en asíncrono
        val launchList = async(Dispatchers.IO) { getLaunchList() }
        val launchById = async(Dispatchers.IO) { getLaunchById("3") }
        // Para imprimirlos en orden
        launchList.await()
        launchById.await()

    }

    private suspend fun getLaunchList() {
        println("Query -> LaunchList")
        try {
            val response = graphQLClient.query(LaunchListQuery()).execute()
            // println(response.data)
            if (response.data != null && !response.hasErrors()) {
                println("Total Launches: ${response.data?.launches?.launches?.size}")
                response.data?.launches?.launches?.forEach {
                    println("Launch: $it")
                }
            } else {
                println("Error: ${response.errors}")
            }
        } catch (e: ApolloException) {
            println("Error: ${e.message}")
        }
    }

    private suspend fun getLaunchById(id: String = "1") {
        println("Query -> LaunchListDetails")
        val response = graphQLClient.query(LaunchDetailsQuery(id)).execute()
        try {
            if (response.data != null && !response.hasErrors()) {
                println(response.data)
            } else {
                println("Error: ${response.errors}")
            }
        } catch (e: ApolloException) {
            println("Error: ${e.message}")
        }
    }
}