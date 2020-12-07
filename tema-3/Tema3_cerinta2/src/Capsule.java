public class Capsule extends Thread{
    public Port Port_in = new Port("Controler", "CM");
    public Port Port_out = new Port("Stabilizer", "ST");
    public String Name_capsule = "Analyzer";

    @Override
    public void run(){
        System.out.println(Name_capsule + " Port In data: " + Port_in.getName_port() + ", " +
                Port_in.getValue_port().toString());
        System.out.println(Name_capsule + " Port Out data: " + Port_out.getName_port() + ", " +
                Port_out.getValue_port().toString());
    }
}
