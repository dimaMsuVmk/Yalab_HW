package eventsourcing.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    PersonApi personApi = applicationContext.getBean(PersonApi.class);
    personApi.savePerson(1L,"Ivan","Ivanov","Ivanovich");
    personApi.savePerson(2L,"Foma","Fomov","Fomovich");
    personApi.savePerson(3L,"Lena","Lena","Lena");
    personApi.savePerson(4L,"Lena","Lena","Lena");
    personApi.savePerson(4L,"Lena","Lena","Lena");

       Thread.sleep(3000);
    System.out.println(personApi.findPerson(2L));
    personApi.savePerson(2L,"igor","igor","igor");
    System.out.println(personApi.findAll());
    personApi.deletePerson(2L);
    System.out.println(personApi.findAll());
    personApi.deletePerson(111L);
    System.out.println(personApi.findAll());
  }
}
