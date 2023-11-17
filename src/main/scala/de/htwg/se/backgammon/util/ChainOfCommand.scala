package de.htwg.se.backgammon.util

import scala.util.Failure
import scala.util.Success
import scala.util.Try

case class ChainHandler[R](successor: Option[ChainHandler[R]])(function: R => Option[R]) {

  def handle[T <: R](in: T): Try[Option[R]] = Try(function(in))

  def handleRequest[T <: R](in: T): Option[R] = {
    handle(in) match {
      case s: Success[Option[R]] =>
        if s.get.isDefined then s.get
        else if successor.isDefined then successor.get.handleRequest(in)
        else None
      case f: Failure[Option[R]] => if (successor.isDefined) successor.get.handleRequest(in) else None
    }
  }
}

object ChainHandler {
  def apply[R](list: List[(R) => (Option[R])]): ChainHandler[R] = {
    list match {
      case Nil          => ChainHandler[R](None)(_ => None)
      case func :: tail => ChainHandler[R](Some(ChainHandler(tail)))(func)
    }
  }
}
