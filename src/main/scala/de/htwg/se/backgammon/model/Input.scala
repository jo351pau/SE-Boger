package de.htwg.se.backgammon.model

object Input {
  val Quit: Option[Quit] = Some(new Quit())
  val Undo: Option[Undo] = Some(new Undo())
  val Redo: Option[Redo] = Some(new Redo())
  val Invalid: Option[Invalid] = Some(new Invalid())
}

abstract class Input

class Quit extends Input

class Undo extends Input

class Redo extends Input

class Invalid extends Input
