import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String args[]){
        Parent guptas = new Parent("Arvid Gupta");
        Parent shuklas = new Parent("Manoj Shukla");
        Parent pandays = new Parent("Chulbul Pandey");
        VacancyData vacancyData = new VacancyData("KV Delhi");
        vacancyData.register(guptas);
        vacancyData.register(shuklas);
        vacancyData.register(pandays);
        vacancyData.declination();

        vacancyData.unregister(shuklas);
        vacancyData.declination();

        VacancyData vacancyData1 = new VacancyData("International school Delhi");
        vacancyData1.register(guptas);
        vacancyData1.register(shuklas);
        vacancyData1.register(pandays);
        vacancyData1.declination();

        vacancyData1.unregister(shuklas);
        vacancyData1.declination();
    }

}
abstract class SchoolVacancyNotification{
    String schoolName;
    SchoolVacancyNotification(String schoolName){
        this.schoolName = schoolName;
    }
    abstract void register(Observer observer);
    abstract void unregister(Observer observer);
    abstract void notifyVacancy();
    abstract void declination();
}
class VacancyData extends SchoolVacancyNotification{

    int maxStrength;
    int numberOfCurrentRegistration;
    List<Observer> observers;
    VacancyData(String schoolName) {
        super(schoolName);
        maxStrength = 50;
        numberOfCurrentRegistration = 50;
        observers = new LinkedList<Observer>();
    }

    @Override
    void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    void unregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    void notifyVacancy() {
        for(Observer observer : observers){
            observer.update(schoolName);
        }
    }

    @Override
    void declination() {
        notifyVacancy();
    }

}

 abstract class Observer{
    abstract void update(String schoolNameWithVacancy);
 }

 class Parent extends Observer{
     String name;

     Parent(String name){
         this.name = name;
     }

     @Override
     void update(String schoolNameWithVacancy) {
         System.out.println("Greetings, "+name+".");
         System.out.println("Vacancy has been created in the school "+schoolNameWithVacancy+".");
         System.out.println("You can approach the school authority for the admission of your child");
     }
 }