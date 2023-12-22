package Server;



import java.net.InetAddress;
import java.util.Objects;

public class Address {
    public InetAddress address;

    public Address(InetAddress address) {
        this.address = address;
    }

    public String getName() {
        return address.getHostName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return address.equals(address1.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
