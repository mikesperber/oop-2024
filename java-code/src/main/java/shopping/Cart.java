package shopping;

import org.jmolecules.ddd.annotation.Entity;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Cart {
    Set<Article> articles = new HashSet<>();
    
    public void put(Article article) {
        this.articles.add(article);
    }

    public boolean isDeliverableTo(AddressType type) {
        for (var article : articles) {
            if (!((article.category != ArticleCategory.FURNITURE)
                || (type == AddressType.HOME_ADDRESS)))
                return false;
        }
        return true;
    }
}
