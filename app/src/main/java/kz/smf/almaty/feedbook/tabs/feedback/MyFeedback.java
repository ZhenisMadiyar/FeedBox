package kz.smf.almaty.feedbook.tabs.feedback;

/**
 * Created by madiyarzhenis on 16.10.15.
 */
public class MyFeedback {
    String title;
    String desc;
    String created;
    String imageUrl;
    String logoUrl;
    String placeName;
    String answer;
    String address;

    public MyFeedback(String title, String desc, String created, String imageUrl, String logoUrl,
                      String placeName, String answer, String address) {
        this.title = title;
        this.desc = desc;
        this.created = created;
        this.imageUrl = imageUrl;
        this.logoUrl = logoUrl;
        this.placeName = placeName;
        this.answer = answer;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MyFeedback(String title, String desc, String created, String imageUrl, String logoUrl,
                      String placeName, String address) {
        this.title = title;
        this.desc = desc;
        this.created = created;
        this.imageUrl = imageUrl;
        this.logoUrl = logoUrl;
        this.placeName = placeName;
        this.address = address;
    }

    public MyFeedback(String title, String desc, String created, String logoUrl, String placeName, String address) {
        this.title = title;
        this.desc = desc;
        this.created = created;
        this.logoUrl = logoUrl;
        this.placeName = placeName;
        this.address = address;

    }

    public MyFeedback(String title, String desc, String created) {
        this.title = title;
        this.desc = desc;
        this.created = created;
//        this.logoUrl = logoUrl;
//        this.placeName = placeName;

    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
