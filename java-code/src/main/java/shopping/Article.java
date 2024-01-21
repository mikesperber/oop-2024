package shopping;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import java.util.Objects;

@Entity // remark: it's a thing ... not
public class Article {
    @Identity
    ArticleNumber number;
        
    ArticleName name;
    ArticleCategory category;

    public Article(ArticleNumber number, ArticleName name, ArticleCategory category) {
        this.number = number;
        this.name = name;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(number, article.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
