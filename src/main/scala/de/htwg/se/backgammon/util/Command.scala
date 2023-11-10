package de.htwg.se.backgammon.util

import scala.util.Try

trait Command[T]:
  def noStep(t: T): T
  def doStep(t: T): Try[T]

class UndoManager[T]:
  private var stack: List[Command[T]] = Nil
  def doStep(t: T, command: Command[T]): Try[T] =
    stack = command :: stack
    command.doStep(t)