package rocket

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.google.gson.GsonBuilder
import graphql.com.models.LaunchDetailsQuery
import graphql.com.models.LaunchListQuery
import graphql.com.models.LoginMutation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import rocket.graphql.RocketClient


object Rocket {
    val graphQLClient = RocketClient.getInstance()
    private lateinit var TOKEN: String

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

        // Como flujo
        val flowAll = getLaunchListFlow()
            .collect {
                it.data?.launches?.launches
                    ?.filter { item -> item?.site?.contains("C") ?: false }
                    ?.take(3)
                    ?.forEach { item ->
                        println(item.toJson())
                    }
            }

        // Login
        val login = async(Dispatchers.IO) { login("pepe@miamil.es") }
        // Las asyn/await se usa realmente cuando queremos obtener valores deuna función asíncrona, por eso difire del lucch, el cual no devolvería nada
        // usando corrutinas: https://www.geeksforgeeks.org/launch-vs-async-in-kotlin-coroutines/
        TOKEN = login.await().toString()
        if (TOKEN.isNotEmpty()) {
            println("Login correcto con token: $TOKEN")
        } else {
            println("Login incorrecto")
        }

    }

    /**
     * Obtiene la lista de lanzamientos
     */
    private suspend fun getLaunchList() {
        println("Query -> LaunchList")
        try {
            val response = graphQLClient.query(LaunchListQuery()).execute()
            // println(response.data)
            if (response.data != null && !response.hasErrors()) {
                println("Total Launches: ${response.data?.launches?.launches?.size}")
                println(response.data?.launches?.launches?.toJson())
//                response.data?.launches?.launches?.forEach {
//                    println("Launch: ${it.toJson()}")
//                }
            } else {
                println("Error: ${response.errors}")
            }
        } catch (e: ApolloException) {
            println("Error: ${e.message}")
        }
    }

    /**
     * Obtiene la lista de lanzamientos como flujo
     * @return Flow<Launch> -> flujo de lanzamientos
     */
    private fun getLaunchListFlow(): Flow<ApolloResponse<LaunchListQuery.Data>> {
        println("Query -> LaunchList as Flow")
        return graphQLClient.query(LaunchListQuery()).toFlow().flowOn(Dispatchers.IO)
    }

    /**
     * Obtiene un lanzamiento por id
     * @param id id del lanzamiento
     */
    private suspend fun getLaunchById(id: String = "1") {
        println("Query -> LaunchListDetails")
        val response = graphQLClient.query(LaunchDetailsQuery(id)).execute()
        try {
            if (response.data != null && !response.hasErrors()) {
                println(response.data?.launch.toJson())
            } else {
                println("Error: ${response.errors}")
            }
        } catch (e: ApolloException) {
            println("Error: ${e.message}")
        }
    }

    private suspend fun login(email: String): String? {
        println("Mutation -> Login")
        val response = graphQLClient.mutation(LoginMutation(Optional.Present(email))).execute()
        try {
            if (response.data != null && !response.hasErrors()) {
                println(response.data?.login.toJson())
                return response.data?.login?.token.toString()
            } else {
                println("Error: ${response.errors}")
            }
        } catch (e: ApolloException) {
            println("Error: ${e.message}")
        }
        return null
    }
}

// Me creo funciones de extensión para poder usar el Gson. Lo ideal sería mapear el modelo, usando los resultados para
private fun LaunchListQuery.Launch?.toJson() = GsonBuilder().setPrettyPrinting().create().toJson(this)
private fun LaunchDetailsQuery.Launch?.toJson() = GsonBuilder().setPrettyPrinting().create().toJson(this)
private fun List<LaunchListQuery.Launch?>.toJson() = GsonBuilder().setPrettyPrinting().create().toJson(this)
private fun LoginMutation.Login?.toJson() = GsonBuilder().setPrettyPrinting().create().toJson(this)







