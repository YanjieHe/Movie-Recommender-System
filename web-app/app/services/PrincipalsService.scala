package services

import javax.inject._
import models.Principals
import dao.PrincipalsDao
import dao.PeopleDao

class PrincipalsService @Inject()(principalsDao: PrincipalsDao, peopleDao: PeopleDao) {
  def getPrincipalsList(imdbId: Int, n: Int): List[(String, Principals)] = {
      val list = principalsDao.getPrincipalList(imdbId, n)
      val people = list.map{
          principal => peopleDao.read(principal.nameId) match {
              case Some(people) => (people.primaryName, principal)
              case None => throw new Exception("person not found")
          }
      }
      people
  }
}
