package shopping;

public class ArticleOld {
    private final ArticleCategory articleCategory;
    public ArticleOld(ArticleName articleName, ArticleCategory articleCategory) {
        this.articleCategory = articleCategory;
    }

    public ArticleCategory getArticleCategory() {
        return articleCategory;
    }
}
