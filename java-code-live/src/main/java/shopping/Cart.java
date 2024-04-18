package shopping;

import org.jmolecules.ddd.annotation.Entity;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Cart {
    Map<Article, Integer> articles = new HashMap<>();
    
    public void put(Article article) {
        this.articles.put(article, 1);
    }

    public boolean isDeliverable(AddressType addressType) {
        for (var article : this.articles.keySet()) {
            return switch (addressType) {
                case PACKSTATION -> switch (article.articleCategory()) {
                    case FURNITURE -> false;
                    case LIFESTYLE -> true;
                };
            };
        };
        return true;
    }
}
