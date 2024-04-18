package shopping0;

import org.jmolecules.ddd.annotation.Entity;

@Entity
public class Cart {
    private Article article;
    public void put(Article article) {
        this.article = article;
    }

    public boolean isDeliverable(AddressType addressType) {
        return switch (this.article.getCategory()) {
            case FURNITURE -> 
                false;
            case LIFESTYLE -> 
                true;
        };
    }
}
