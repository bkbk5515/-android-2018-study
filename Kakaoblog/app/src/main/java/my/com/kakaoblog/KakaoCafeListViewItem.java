package my.com.kakaoblog;


public class KakaoCafeListViewItem {

    private String url ;
    private String thumbnail ;
    private String title ;
    private String contents ;

    public KakaoCafeListViewItem(String url, String thumbnail, String title, String contents){

        this.url = url;
        this.thumbnail = thumbnail;
        this.title = title;
        this.contents = contents;
    }

    public KakaoCafeListViewItem(){    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}