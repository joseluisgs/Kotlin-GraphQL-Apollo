# Kotlin GraphQL Apollo
Sencillo cliente para consumir una API GraphQL con Apollo usando Kotlin. Para ello se consumen servicios GraphQL de una API de prueba.

[![Kotlin](https://img.shields.io/badge/Code-Kotlin-blueviolet)](https://kotlinlang.org/)
[![LISENCE](https://img.shields.io/badge/Lisence-MIT-green)]()
![GitHub](https://img.shields.io/github/last-commit/joseluisgs/Kotlin-GraphQL-Apollo)


![imagen](https://www.adesso-mobile.de/wp-content/uploads/2021/02/kotlin-einfu%CC%88hrung.jpg)


## API GraphQL
GraphQL es un lenguaje de consulta para APIs. En lugar de enviar múltiples peticiones independientes (por ejemplo, una para la lista de usuarios, otra para sus roles, otra para su empresa), GraphQL permite a los clientes hacer una sola petición, y obtener la respuesta con todos los campos que necesitan.

Minimiza la cantidad de datos que deben transferirse a través de la red y, por lo tanto, mejora considerablemente las aplicaciones que operan en circunstancias por ejemplo en donde la red no sea eficiente o el dispositivo sea de baja potencia.

En GraphQL se define una estructura de datos para representar una API. El dominio de GraphQL son los datos. Se puede utilizar para diseñar un esquema que represente los datos y tiparlos.

Los elementos fundamentales de una API GraphQL, son:

- [Esquema](./src/main/graphql/rocket/rocket.graphqls): Es una representación de los datos donde se pueden fijar su tipo y relaciones. Así como el tipo de dato que se espera obtener y las operaciones que se pueden realizar sobre ellos.
- Query: Definen las consultas que se pueden realizar sobre los datos, [sin parámetros](./src/main/graphql/rocket/LaunchDetails.graphql) o c[on parámetros](./src/main/graphql/rocket/LaunchDetails.graphql).
- Mutation: Definen las mutaciones que se pueden realizar sobre los datos. Es decir, cambios sobre los mismos: [insercciones](./src/main/graphql/rocket/Login.graphql), [actualizaciones](./src/main/graphql/rocket/BookTrip.graphql), o [borrados](./src/main/graphql/rocket/CancelTrip.graphql).
- Subscriptions: Las [suscripciones](./src/main/graphql/rocket/TripsBooked.graphql) son útiles para notificar a su cliente en tiempo real sobre cambios en los datos de back-end, como la creación de un nuevo objeto o actualizaciones de un campo importante.

## Autor

Codificado con :sparkling_heart: por [José Luis González Sánchez](https://twitter.com/joseluisgonsan)

[![Twitter](https://img.shields.io/twitter/follow/joseluisgonsan?style=social)](https://twitter.com/joseluisgonsan)
[![GitHub](https://img.shields.io/github/followers/joseluisgs?style=social)](https://github.com/joseluisgs)

### Contacto
<p>
  Cualquier cosa que necesites házmelo saber por si puedo ayudarte 💬.
</p>
<p>
    <a href="https://twitter.com/joseluisgonsan" target="_blank">
        <img src="https://i.imgur.com/U4Uiaef.png" 
    height="30">
    </a> &nbsp;&nbsp;
    <a href="https://github.com/joseluisgs" target="_blank">
        <img src="https://distreau.com/github.svg" 
    height="30">
    </a> &nbsp;&nbsp;
    <a href="https://www.linkedin.com/in/joseluisgonsan" target="_blank">
        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/LinkedIn_logo_initials.png/768px-LinkedIn_logo_initials.png" 
    height="30">
    </a>  &nbsp;&nbsp;
    <a href="https://joseluisgs.github.io/" target="_blank">
        <img src="https://joseluisgs.github.io/favicon.png" 
    height="30">
    </a>
</p>


## Licencia

Este proyecto está licenciado bajo licencia **MIT**, si desea saber más, visite el fichero [LICENSE](./LICENSE) para su uso docente y educativo.