public class Port {
    private String Name_port;
    private Object Value_port;

    Port(String x, Object y){
        setName_port(x);
        setValue_port(y);
    }
    public void setName_port(String name_port) {
        Name_port = name_port;
    }

    public String getName_port() {
        return Name_port;
    }

    public Object getValue_port() {
        return Value_port;
    }

    public void setValue_port(Object value_port) {
        Value_port = value_port;
    }
}
