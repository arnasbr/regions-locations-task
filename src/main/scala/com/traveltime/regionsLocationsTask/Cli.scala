package com.traveltime.regionsLocationsTask

import cats.data.{Validated, ValidatedNel}
import cats.implicits.catsSyntaxTuple3Semigroupal
import com.monovore.decline._
import com.traveltime.regionsLocationsTask.Models.CliArgs

import scala.reflect.io.File

object Cli {
  implicit val fileArgument: Argument[File] = new Argument[File] {
    def read(string: String): ValidatedNel[String, File] = {
      val file = File(string)
      if (file.exists && file.canRead)
        Validated.validNel(file)
      else
        Validated.invalidNel(
          s"!!! ERROR: In arguments passed file ${file.path} doesn't exists or isn't readable !!!"
        )
    }

    def defaultMetavar = "File path"
  }

  private val locationsInputFile: Opts[File] =
    Opts.option[File]("locations", help = "Path to locations JSON file")
  private val regionsInputFile: Opts[File] =
    Opts.option[File]("regions", help = "Path to regions JSON file")
  val outputFile: Opts[File] =
    Opts.option[File]("output", help = "Path to output JSON file")

  val command: Opts[CliArgs] = Opts.subcommand(
    Command(name = "match", header = "Matches locations to regions")(
      (locationsInputFile, regionsInputFile, outputFile).mapN(CliArgs)
    )
  )
}
