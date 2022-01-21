import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.*
import rocket.Rocket
import kotlin.system.exitProcess

fun main() {
    println(
        colorize(
            " \uD83D\uDC4B HOLA API REST con Kotlin y Retrofit2 ",
            YELLOW_TEXT(),
            BLACK_BACK(),
            BOLD(),
            UNDERLINE()
        )
    )
    Rocket.run()
    println("Fin del programa")
    exitProcess(0)
}
