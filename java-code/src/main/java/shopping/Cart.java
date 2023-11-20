package shopping;

import org.jmolecules.ddd.annotation.Entity;

@Entity
public class Cart {
    public void put(Article article) {

    }

    public boolean isDeliverableTo(AddressType type) {
        return false;
    }
}
