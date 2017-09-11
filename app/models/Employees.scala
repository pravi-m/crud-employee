package models


import play.api.libs.json.Json
import play.api.libs.functional.syntax._
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONObjectIDIdentity
import reactivemongo.bson.BSONStringHandler
import reactivemongo.bson.Producer.nameValue2Producer
import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat

/**
  * Created by Medi on 22-08-2017.
  */
case class Employees (
                       _id: Option[BSONObjectID],
                       firstName: String,
                       lastName: String,
                       emailid: String
                     )

object Employees{
  implicit val postFormat = Json.format[Employees]

  // case class extends serializable.. so the objects can be serialized or desrialized

  // deserializing from a BSON document
  implicit object EmployeeBSONReader extends BSONDocumentReader[Employees] {
    def read(doc: BSONDocument): Employees =
      Employees(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("firstName").get,
        doc.getAs[String]("lastName").get,
        doc.getAs[String]("emailid").get)
  }

  // serializing to a BSON Document (
  implicit object EmployeeBSONWriter extends BSONDocumentWriter[Employees] {
    def write(employee: Employees): BSONDocument =
      BSONDocument(
        "_id" -> employee._id.getOrElse(BSONObjectID.generate),
        "firstName" -> employee.firstName,
        "lastName" -> employee.lastName,
        "emailid" -> employee.emailid)
  }

}