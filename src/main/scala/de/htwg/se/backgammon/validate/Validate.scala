package de.htwg.se.backgammon.validate

import scala.util.Try

trait ValidateStrategy {
  private var ex: List[Exception] = List()

  def execute(): Option[Exception] = if (ex.isEmpty) then None else Some(ex(0))

  def require(
      requirement: Boolean,
      exception: Exception = Exception()
  ): Unit = {
    if (!requirement) then ex :+ Some(exception)
  }

  def exceptions = ex
}