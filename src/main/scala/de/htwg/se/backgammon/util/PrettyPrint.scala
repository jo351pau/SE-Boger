package de.htwg.se.backgammon.util

import de.htwg.se.backgammon.model.Game

object PrettyPrint {
  def printNew(string: String | Game) = {
    print("\u001b[2J")
    print("\u001b[H")
    println(s"${string}\n")
  }

  implicit class MarkDifferencesBetweenGames(original: Game) {

    def markDifferencesTo(modified: Game): String = {
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

    private def underline(string: String) = s"\u001B[4m${string}\u001b[0m"

  }

  implicit class PrintDiceResults(original: (Int, Int)) {
    def toPrettyString = s"(${original.productIterator.zipWithIndex
        .map { case (value, index) => s"${bold(value.toString)}" }
        .mkString(", ")}) ${
        if (original._1 == original._2) s"${bold("DOUBLE")}" else ""
      }"
  }

  implicit class PrintBold(original: String) {
    def bold = PrettyPrint.bold(original)
  }

  private def bold(string: String) = s"\u001B[1m${string}\u001b[0m"
}
