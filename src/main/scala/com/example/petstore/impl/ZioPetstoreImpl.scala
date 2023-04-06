package com.example.petstore.impl

import com.example.petstore.generated.petstore.ZioPetstore._

import zio.{IO, RefM}
import zio.stream.{Stream, ZStream}

import io.grpc.Status
import com.example.petstore.generated.petstore.User
import com.example.petstore.generated.petstore.PetByIdRequest
import com.example.petstore.generated.petstore.PetByIdResponse
import com.example.petstore.generated.petstore.UserByNameRequest
import com.example.petstore.generated.petstore.UserByNameResponse
import com.example.petstore.generated.petstore.Pet
import com.example.petstore.generated.petstore.ListUsersRequest
import com.example.petstore.generated.petstore.StoreUsersResponse

class ZioPetstoreImpl(userState: ZioPetstoreImpl.State) extends PetStoreService {

  override def petById(request: PetByIdRequest) =
    request.id match {
      case 0 => IO.succeed(PetByIdResponse(Some(Pet(0, "Ralph the Dog"))))
      case 1 => IO.succeed(PetByIdResponse(Some(Pet(1, "Billy the Goat"))))
      case 2 => IO.succeed(PetByIdResponse(Some(Pet(2, "Puss in Boots"))))
      case _ => IO.fail(Status.NOT_FOUND)
    }

  override def userByName(request: UserByNameRequest): IO[Status, UserByNameResponse] =
    userState.get.flatMap { state =>
      state.get(request.username) match {
        case None             => IO.fail(Status.NOT_FOUND)
        case user: Some[User] => IO.succeed(UserByNameResponse(user = user))
      }
    }

  override def listUsers(request: ListUsersRequest): Stream[Status, User] =
    ZStream.fromIterableM(
      userState.get.map(_.values)
    )

  override def storeUsers(request: Stream[Status, User]): IO[Status, StoreUsersResponse] =
    request
      .mapM { case user =>
        userState.updateSome {
          case state if (!state.contains(user.username) && user.username.nonEmpty) =>
            IO.succeed(state + ((user.username, user)))
        }
      }
      .runDrain
      .map(_ => StoreUsersResponse.of("Finished Processing Request"))

  override def bulkUsers(request: Stream[Status, User]): Stream[Status, User] =
    request.mapM {
      case user if (user.username.isEmpty()) => IO.fail(Status.INVALID_ARGUMENT)
      case user                              =>
        userState
          .update(state =>
            if (state.contains(user.username))
              IO.fail(Status.ALREADY_EXISTS)
            else
              IO.succeed(state + ((user.username, user)))
          )
          .map(_ => user)
    }

}

object ZioPetstoreImpl {
  type State = RefM[Map[String, User]]
}
