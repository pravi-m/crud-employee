package controllers

import javax.inject._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import models.Employees
import models.Employees._
import models.Employees.EmployeeBSONReader
import models.Employees.EmployeeBSONWriter
import play.api._
import play.api.libs.json._
import play.api.mvc.{Action, _}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents, _}

import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentIdentity
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONObjectIDIdentity
import reactivemongo.bson.Producer.nameValue2Producer

import scala.collection.immutable
import scala.collection.immutable.List


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, val reactiveMongoApi:ReactiveMongoApi)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents{

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  override lazy val parse: PlayBodyParsers = cc.parsers

  def collection= db.collection[BSONCollection]("employees")

  //def collection: JSONCollection = db.collection[JSONCollection]("employees")


  def index = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.main("Employee Form"))
  }

  // to get all the records from the collection
  def all = Action.async{
    val cursor = collection.find(BSONDocument()).cursor[Employees]
    val futureList = cursor.collect[List]()
    futureList.map { allemployees =>
      Ok(Json.toJson(allemployees))
    }
  }

  // to create a new record
  def create = Action.async(parse.json) { request =>
    request.body.validate[Employees].map {
      employees =>
        collection.insert(employees).map {
          lastError => Created(s"Employee created successfully")
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  // to update an existing record
  def update(id:String) = Action.async(parse.json){request=>
    request.body.validate[Employees].map{ emp=>
      collection.update(BSONDocument("_id" -> BSONObjectID(id)), emp).map{
        lastError => Accepted(s"Employee details updated")
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))

  }

  // to delete a record
  def delete(id:String) = Action.async{
    println("Debug Info" + BSONObjectID(id));
    collection.remove(BSONDocument("_id" -> BSONObjectID(id))).map{

      lastError => Ok(s"Employee Redord Deleted")
    }.recover{ case _ => InternalServerError}
  }

}
