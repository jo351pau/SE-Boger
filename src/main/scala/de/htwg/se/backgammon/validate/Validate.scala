package de.htwg.se.backgammon.validate

import scala.util.Try
import scala.util.Success
import scala.util.Failure

trait ValidateStrategy {
  def execute(): Option[Throwable] = Try(validate()) match {
    case Success(_)         => None
    case Failure(exception) => Some(exception)
  }

  def validate(): Unit
}
