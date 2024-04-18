package shopping0;

import org.jmolecules.ddd.annotation.Entity;

@Entity
public class Article {
    ArticleCategory category;
    public Article(ArticleName articleName, ArticleCategory category) {
        this.category = category;
    }

    public ArticleCategory getCategory() {
        return category;
    }
}
