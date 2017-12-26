package gov.df.fap.bean.portal;

public class ArticleAttachBean {
    private String attachId;

    private String articleId;

    private String attachName;

    private byte[] attachContent;

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId == null ? null : attachId.trim();
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName == null ? null : attachName.trim();
    }

    public byte[] getAttachContent() {
        return attachContent;
    }

    public void setAttachContent(byte[] attachContent) {
        this.attachContent = attachContent;
    }
}