package shopping;

import org.jmolecules.ddd.annotation.Entity;

@Entity // remark: it's a thing ... not
public class Article {
    ArticleName name;
    ArticleCategory category;

    public Article(ArticleName name, ArticleCategory category) {
        this.name = name;
        this.category = category;
    }
}
