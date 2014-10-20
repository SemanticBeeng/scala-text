package de.kp.scala.text.actor
/* Copyright (c) 2014 Dr. Krusche & Partner PartG
* 
* This file is part of the Scala-Text project
* (https://github.com/skrusche63/scala-text).
* 
* Scala-Text is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* 
* Scala-Text is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with
* Scala-Text. 
* 
* If not, see <http://www.gnu.org/licenses/>.
*/

import akka.actor.{Actor,ActorLogging}

import de.kp.scala.text.Configuration
import de.kp.scala.text.model._

class TopicQuestor extends Actor with ActorLogging {

  implicit val ec = context.dispatcher
  
  def receive = {

    case req:ServiceRequest => {
      
      val origin = sender    
      val uid = req.data("uid")

      req.task match {
       
        case "get:concept" => {

          // TODO
          val resp:ServiceResponse = null       
           
          origin ! Serializer.serializeResponse(resp)
          context.stop(self)
          
        }
        
        case _ => {
          
          val msg = Messages.TASK_IS_UNKNOWN(uid,req.task)
          
          origin ! Serializer.serializeResponse(failure(req,msg))
          context.stop(self)
           
        }
        
      }
      
    }
    
    case _ => {
      
      val origin = sender               
      val msg = Messages.REQUEST_IS_UNKNOWN()          
          
      origin ! Serializer.serializeResponse(failure(null,msg))
      context.stop(self)

    }
  
  }

  private def failure(req:ServiceRequest,message:String):ServiceResponse = {
    
    if (req == null) {
      val data = Map("message" -> message)
      new ServiceResponse("","",data,TextStatus.FAILURE)	
      
    } else {
      val data = Map("uid" -> req.data("uid"), "message" -> message)
      new ServiceResponse(req.service,req.task,data,TextStatus.FAILURE)	
    
    }
    
  }
}