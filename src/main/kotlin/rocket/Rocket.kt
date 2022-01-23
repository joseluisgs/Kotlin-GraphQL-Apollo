package rocket

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.google.gson.GsonBuilder
import graphql.com.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import rocket.graphql.RocketClient

/**
 * Lo ideal en este ejemplo no sería imprimir las cosas en los métodos, pero eso es solo para mostrar como funcionaria
 * Deberíamos usar asyn/await para obtener los resultados de la consulta de la API GraphQL y luego con ellos mostrarlos o
 * procesarlos según sea el caso.
 * Pero es un ejemplo de uso, no lo olvides.
 */

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
        // Las asyn/await se usa realmente cuando queremos obtener valores deuna función asíncrona, por eso difire del lucch, el cual no devolvería nada
        // usando corrutinas: https://www.geeksforgeeks.org/launch-vs-async-in-kotlin-coroutines/
        val launch = launchById.await()

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
        TOKEN = login.await().toString()
        if (TOKEN.isNotEmpty()) {
            println("Login correcto con token: $TOKEN")
        } else {
            println("Login incorrecto")
        }

        // println("Luanch: $launch")
        // Cancelar y anular lanzamientos
        if (launch?.isBooked == true) {
            println("El lanzamiento ${launch.id} con misión a ${launch.mission?.name} está confirmado y lo anularemos")
            async(Dispatchers.IO) { cancelLaunch(launch.id) }
        } else {
            println("El lanzamiento ${launch?.id} con misión a ${launch?.mission?.name} no esta confirmado y lo confirmaremos")
            async(Dispatchers.IO) { bookLaunch(launch!!.id) }
        }
/*

        // Vamos a crear una suscripcion para que se ejecute cada vez que se actualice el lanzamiento
        // y se muestre en consola
        val subscription = launch(Dispatchers.IO) {
            println("Lanzando suscripción para escuchar cambios")
            var graphQLClient = RocketClient.getInstance(TOKEN)
            graphQLClient.subscription(TripsBookedSubscription()).toFlow()
                .collect {
                    println("Suscripción lanzamientos actualizados: ${it.data?.tripsBooked?.toJson()}")
                }
        }

        // Ahora vamos a a cambiar el lanzamiento
        async(Dispatchers.IO) { bookLaunch(launch!!.id) }.await()
*/

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
     * @return Launch? -> lanzamiento
     */
    private suspend fun getLaunchById(id: String = "1"): LaunchDetailsQuery.Launch? {
        println("Query -> LaunchListDetails")
        val response = graphQLClient.query(LaunchDetailsQuery(id)).execute()
        try {
            if (response.data != null && !response.hasErrors()) {
                println(response.data?.launch.toJson())
                return response.data?.launch
            } else {
                println("Error: ${response.errors}")
            }
        } catch (e: ApolloException) {
            println("Error: ${e.message}")
        }
        return null
    }

    /**
     * Login de usuario
     * @param email email del usuario
     * @return String -> token
     */
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

    /**
     * Cancela un lanzamiento en base a su mutacion con token, pues requiere estar autenticado
     * @param id id del lanzamiento
     */
    private suspend fun cancelLaunch(id: String) {
        println("Mutation -> Cancel Launch with Token")
        // Necesitamos una instancia con el token
        val graphQLClient = RocketClient.getInstance(this.TOKEN)
        val response = graphQLClient.mutation(CancelTripMutation(id)).execute()
        try {
            if (response.data != null && !response.hasErrors()) {
                println(response.data?.cancelTrip.toJson())
            } else {
                println("Error: ${response.errors}")
            }
        } catch (e: ApolloException) {
            println("Error: ${e.message}")
        }
    }

    /**
     * Confirma un lanzamiento en base a su mutacion, pues requiere estar autenticado
     * @param id id del lanzamiento
     */
    private suspend fun bookLaunch(id: String) {
        println("Mutation -> Book Launch with Token")
        // Necesitamos una instancia con el token
        val graphQLClient = RocketClient.getInstance(this.TOKEN)
        val response = graphQLClient.mutation(BookTripMutation(id)).execute()
        try {
            if (response.data != null && !response.hasErrors()) {
                println(response.data?.bookTrips.toJson())
            } else {
                println("Error: ${response.errors}")
            }
        } catch (e: ApolloException) {
            println("Error: ${e.message}")
        }
    }
}

// Me creo funciones de extensión para poder usar el Gson. Lo ideal sería mapear el modelo, usando los resultados para
private fun Any?.toJson() = GsonBuilder().setPrettyPrinting().create().toJson(this)








