package de.htwg.se.backgammon.model

enum Player {
  case White
  case Black
  case None

  def toLowerCase = toString.toLowerCase
}
