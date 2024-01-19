# Backgammon

[![Coverage Status](https://coveralls.io/repos/github/jo351pau/SE-Boger/badge.svg?branch=main)](https://coveralls.io/github/jo351pau/SE-Boger?branch=main)

![Screenshot from game](src/main/resources/screenshot.jpeg)

This is an open-source project that implements the classic board game Backgammon using the Scala programming language. This project aims to provide a scalable, maintainable, and extensible codebase for Backgammon enthusiasts and developers alike.

## Data Storage

This project supports data storage in both JSON and XML formats. You can save and load your game data using the provided serialization functionalities. Simply import the corresponding class into the `de.htwg.se.backgammon.Main` class.
1. json: `import de.htwg.se.backgammon.model.storage.JsonStorage.{given}`
2. xml: `import de.htwg.se.backgammon.model.storage.XmlStorage.{given}`
By implementing the following `Storage` trait,
you gain the flexibility to effortlessly support various serialization formats.
```scala
trait Storage
 def load[O <: Storable](parser: Parser[_ <: Storable], path: String): Try[O]
 def fileExtension: String
 def parse[O <: Storable](obj: O): String 
```


## Contributing

We welcome contributions from the community! If you'd like to contribute to Scala Backgammon, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature/bugfix: `git checkout -b feature-name`
3. Implement your changes.
4. Test thoroughly.
5. Create a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

- [Scala Documentation](https://docs.scala-lang.org/)
- [Backgammon Rules](https://www.bkgm.com/rules.html)