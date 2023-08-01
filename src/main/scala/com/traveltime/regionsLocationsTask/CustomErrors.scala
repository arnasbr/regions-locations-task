package com.traveltime.regionsLocationsTask

sealed trait CustomError {
  def msg: String
}
object CustomErrors {
  case class FileReadError(override val msg: String) extends CustomError {
    override def toString: String = s"Failed to read file: $msg"
  }

  case class ParseError(override val msg: String) extends CustomError {
    override def toString: String = s"Failed to parse from file: $msg"
  }

  case class DecodeError(override val msg: String) extends CustomError {
    override def toString: String = s"Failed to decode from JSON: $msg"
  }

  case class ConversionError(override val msg: String) extends CustomError {
    override def toString: String = s"Failed to convert to JSON: $msg"
  }
}
