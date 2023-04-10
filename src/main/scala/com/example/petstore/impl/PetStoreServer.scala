package com.example.petstore.impl

import com.example.petstore.generated.petstore.User

import zio.{RefM, ZEnv}

import scalapb.zio_grpc.{ServerMain, ServiceList}

object PetStoreServer extends ServerMain {
  override def port: Int = 8080

  val createPetStore =
    for {
      userState <- RefM.make(Map.empty[String, User])
    } yield new ZioPetstoreImpl(userState)

  override def services: ServiceList[ZEnv] =
    ServiceList.addM(createPetStore)

}
