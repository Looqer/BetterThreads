package one;

public class FileClass extends Thread{
    @Override
    public void run(){
        gener();
    }

    public String gener()  {
        return("ABC");
    }

}
