package com.traveltime.regionsLocationsTask

import cats.implicits.catsSyntaxTuple3Semigroupal
import com.monovore.decline._

object Cli {
  val locationsInputFile: Opts[String] =
    Opts.option[String]("locations", help = "Path to locations JSON file")
  val regionsInputFile: Opts[String] =
    Opts.option[String]("regions", help = "Path to regions JSON file")
  val outputFile: Opts[String] =
    Opts.option[String]("output", help = "Path to output JSON file")

  val command: Opts[(String, String, String)] = Opts.subcommand(
    Command(name = "match", header = "Matches locations to regions")(
      (locationsInputFile, regionsInputFile, outputFile).tupled
    )
  )
}
