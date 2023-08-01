package com.traveltime.regionsLocationsTask

sealed trait CustomErrors
object CustomErrors {
  case class FileReadError(msg: String) extends CustomErrors {
    override def toString: String = "Failed to read file"
  }

  case class ParseError(msg: String) extends CustomErrors {
    override def toString: String = "Failed to parse from file"
  }

  case class DecodeError(msg: String) extends CustomErrors {
    override def toString: String = "Failed to decode from JSON"
  }

  case class ConversionError(msg: String) extends CustomErrors {
    override def toString: String = "Failed to convert to JSON"
  }
}
