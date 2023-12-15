package de.htwg.se.backgammon.util

import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.model.IGame

object PrettyPrint {
  private def clean = {
    print("\u001b[2J")
    print("\u001b[H")
  }

  def printNew(string: String | IGame) = {
    clean; println(s"${string}\n")
  }

  def printGameWithIndizies(game: IGame) = {
    clean
    println(
      s"${game.barWhite} : " + underline(
        game.fields.mkString(" ")
      ) + s" : ${game.barBlack}"
    )
    println(
      " " * 3 + game.fields.zipWithIndex
        .map { case (v, i) =>
          s" ${if (i <= 10) " " else ""}${if (v.pieces >= 0) s"$i " else s" $i "}"
        }
        .mkString("") + " " * 4 + "\n"
    )
  }

  def printGameOver(player: Player) = {
    val victoryMessage =
      """ _     _             _                                              
      |( )   ( )           ( )                                             
      |`\`\_/'/'_    _   _ |/  _   _    __      _   _   _    _     ___     
      |  `\ /'/'_`\ ( ) ( )   ( ) ( ) /'__`\   ( ) ( ) ( ) /'_`\ /' _ `\   
      |   | |( (_) )| (_) |   | \_/ |(  ___/   | \_/ \_/ |( (_) )| ( ) |   
      |   (_)`\___/'`\___/'   `\___/'`\____)   `\___x___/'`\___/'(_) (_)   
      |                                                                    
      |""".stripMargin
    clean
    println(s"${victoryMessage}\nCongratulation to ${s"${player}".bold}!!")
  }

  implicit class MarkDifferencesBetweenGames(original: IGame) {

    def markDifferencesTo(modified: IGame): String = {
      var result = ""
      if (original.barWhite != modified.barWhite)
        result += bold(s"${original.barWhite}")
      else result += original.barWhite

      result += " : "

      result += original.fields
        .zip(modified.fields)
        .map { case (a, b) =>
          if (a == b) then a else underline(a.toString)
        }
        .mkString(" ")

      result += " : "

      if (original.barBlack != modified.barBlack)
        result += bold(s"${original.barBlack}")
      else result += original.barBlack

      result
    }

  }

  implicit class PrintDiceResults(original: List[Int]) {
    def toPrettyString = s"(${original.zipWithIndex
        .map { case (value, index) => s"${bold(value.toString)}" }
        .mkString(", ")}) ${
        if (original(0) == original(1)) s"${bold("DOUBLE")}" else ""
      }"
  }

  implicit class PrintBold(original: String) {
    def bold = PrettyPrint.bold(original)
  }

  private def underline(string: String) = s"\u001B[4m${string}\u001b[0m"

  private def bold(string: String) = s"\u001B[1m${string}\u001b[0m"
}
