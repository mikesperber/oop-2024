package shopping;

import org.jmolecules.ddd.annotation.Entity;

@Entity
public class Cart {
    Article article;
    public void put(Article article) {
        this.article = article;
    }

    public boolean isDeliverableTo(AddressType type) {
        return article.category != ArticleCategory.FURNITURE;
    }
}
