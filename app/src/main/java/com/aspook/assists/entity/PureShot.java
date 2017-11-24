package com.aspook.assists.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * basic shot
 * <p>
 * Created by ASPOOK on 17/7/19.
 */

public class PureShot implements Parcelable {

    /**
     * id : 3663360
     * title : Nature
     * description : <p>Outdoorsy things. </p>
     * width : 400
     * height : 300
     * images : {"hidpi":"https://cdn.dribbble.com/users/52084/screenshots/3663360/nature.jpg","normal":"https://cdn.dribbble.com/users/52084/screenshots/3663360/nature_1x.jpg","teaser":"https://cdn.dribbble.com/users/52084/screenshots/3663360/nature_teaser.jpg"}
     * views_count : 5635
     * likes_count : 550
     * comments_count : 13
     * attachments_count : 1
     * rebounds_count : 0
     * buckets_count : 27
     * created_at : 2017-07-17T15:54:34Z
     * updated_at : 2017-07-17T15:56:44Z
     * html_url : https://dribbble.com/shots/3663360-Nature
     * attachments_url : https://api.dribbble.com/v1/shots/3663360/attachments
     * buckets_url : https://api.dribbble.com/v1/shots/3663360/buckets
     * comments_url : https://api.dribbble.com/v1/shots/3663360/comments
     * likes_url : https://api.dribbble.com/v1/shots/3663360/likes
     * projects_url : https://api.dribbble.com/v1/shots/3663360/projects
     * rebounds_url : https://api.dribbble.com/v1/shots/3663360/rebounds
     * animated : false
     * tags : ["geometric","illustration","modern","nature","outdoors","poster","print","sun","tree","wood"]
     * team : {"id":107759,"name":"UI8","username":"UI8","html_url":"https://dribbble.com/UI8","avatar_url":"https://cdn.dribbble.com/users/107759/avatars/normal/8b6e6f4db451424c7883c3a8cd5de9a9.png?1493958117","bio":"A small team building a curated marketplace for UI designers. ","location":"San Francisco, CA","links":{"web":"http://www.ui8.net","twitter":"https://twitter.com/ui8net"},"buckets_count":1,"comments_received_count":13410,"followers_count":88294,"followings_count":391,"likes_count":1447,"likes_received_count":293074,"projects_count":9,"rebounds_received_count":195,"shots_count":529,"can_upload_shot":true,"type":"Team","pro":false,"buckets_url":"https://api.dribbble.com/v1/users/107759/buckets","followers_url":"https://api.dribbble.com/v1/users/107759/followers","following_url":"https://api.dribbble.com/v1/users/107759/following","likes_url":"https://api.dribbble.com/v1/users/107759/likes","projects_url":"https://api.dribbble.com/v1/users/107759/projects","shots_url":"https://api.dribbble.com/v1/users/107759/shots","created_at":"2012-02-25T06:29:44Z","updated_at":"2017-07-04T05:36:22Z","members_count":8,"members_url":"https://api.dribbble.com/v1/teams/107759/members","team_shots_url":"https://api.dribbble.com/v1/teams/107759/shots"}
     */

    private int id;
    private String title;
    private String description;
    private int width;
    private int height;
    private ImagesBean images;
    private int views_count;
    private int likes_count;
    private int comments_count;
    private int attachments_count;
    private int rebounds_count;
    private int buckets_count;
    private String created_at;
    private String updated_at;
    private String html_url;
    private String attachments_url;
    private String buckets_url;
    private String comments_url;
    private String likes_url;
    private String projects_url;
    private String rebounds_url;
    private boolean animated;
    private TeamBean team;
    private List<String> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
    }

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getAttachments_count() {
        return attachments_count;
    }

    public void setAttachments_count(int attachments_count) {
        this.attachments_count = attachments_count;
    }

    public int getRebounds_count() {
        return rebounds_count;
    }

    public void setRebounds_count(int rebounds_count) {
        this.rebounds_count = rebounds_count;
    }

    public int getBuckets_count() {
        return buckets_count;
    }

    public void setBuckets_count(int buckets_count) {
        this.buckets_count = buckets_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getAttachments_url() {
        return attachments_url;
    }

    public void setAttachments_url(String attachments_url) {
        this.attachments_url = attachments_url;
    }

    public String getBuckets_url() {
        return buckets_url;
    }

    public void setBuckets_url(String buckets_url) {
        this.buckets_url = buckets_url;
    }

    public String getComments_url() {
        return comments_url;
    }

    public void setComments_url(String comments_url) {
        this.comments_url = comments_url;
    }

    public String getLikes_url() {
        return likes_url;
    }

    public void setLikes_url(String likes_url) {
        this.likes_url = likes_url;
    }

    public String getProjects_url() {
        return projects_url;
    }

    public void setProjects_url(String projects_url) {
        this.projects_url = projects_url;
    }

    public String getRebounds_url() {
        return rebounds_url;
    }

    public void setRebounds_url(String rebounds_url) {
        this.rebounds_url = rebounds_url;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean team) {
        this.team = team;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public static class ImagesBean implements Parcelable {
        /**
         * hidpi : https://cdn.dribbble.com/users/52084/screenshots/3663360/nature.jpg
         * normal : https://cdn.dribbble.com/users/52084/screenshots/3663360/nature_1x.jpg
         * teaser : https://cdn.dribbble.com/users/52084/screenshots/3663360/nature_teaser.jpg
         */

        private String hidpi;
        private String normal;
        private String teaser;

        public String getHidpi() {
            return hidpi;
        }

        public void setHidpi(String hidpi) {
            this.hidpi = hidpi;
        }

        public String getNormal() {
            return normal;
        }

        public void setNormal(String normal) {
            this.normal = normal;
        }

        public String getTeaser() {
            return teaser;
        }

        public void setTeaser(String teaser) {
            this.teaser = teaser;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.hidpi);
            dest.writeString(this.normal);
            dest.writeString(this.teaser);
        }

        public ImagesBean() {
        }

        protected ImagesBean(Parcel in) {
            this.hidpi = in.readString();
            this.normal = in.readString();
            this.teaser = in.readString();
        }

        public static final Parcelable.Creator<ImagesBean> CREATOR = new Parcelable.Creator<ImagesBean>() {
            @Override
            public ImagesBean createFromParcel(Parcel source) {
                return new ImagesBean(source);
            }

            @Override
            public ImagesBean[] newArray(int size) {
                return new ImagesBean[size];
            }
        };
    }

    public static class TeamBean implements Parcelable {
        /**
         * id : 107759
         * name : UI8
         * username : UI8
         * html_url : https://dribbble.com/UI8
         * avatar_url : https://cdn.dribbble.com/users/107759/avatars/normal/8b6e6f4db451424c7883c3a8cd5de9a9.png?1493958117
         * bio : A small team building a curated marketplace for UI designers.
         * location : San Francisco, CA
         * links : {"web":"http://www.ui8.net","twitter":"https://twitter.com/ui8net"}
         * buckets_count : 1
         * comments_received_count : 13410
         * followers_count : 88294
         * followings_count : 391
         * likes_count : 1447
         * likes_received_count : 293074
         * projects_count : 9
         * rebounds_received_count : 195
         * shots_count : 529
         * can_upload_shot : true
         * type : Team
         * pro : false
         * buckets_url : https://api.dribbble.com/v1/users/107759/buckets
         * followers_url : https://api.dribbble.com/v1/users/107759/followers
         * following_url : https://api.dribbble.com/v1/users/107759/following
         * likes_url : https://api.dribbble.com/v1/users/107759/likes
         * projects_url : https://api.dribbble.com/v1/users/107759/projects
         * shots_url : https://api.dribbble.com/v1/users/107759/shots
         * created_at : 2012-02-25T06:29:44Z
         * updated_at : 2017-07-04T05:36:22Z
         * members_count : 8
         * members_url : https://api.dribbble.com/v1/teams/107759/members
         * team_shots_url : https://api.dribbble.com/v1/teams/107759/shots
         */

        private int id;
        private String name;
        private String username;
        private String html_url;
        private String avatar_url;
        private String bio;
        private String location;
        private LinksBean links;
        private int buckets_count;
        private int comments_received_count;
        private int followers_count;
        private int followings_count;
        private int likes_count;
        private int likes_received_count;
        private int projects_count;
        private int rebounds_received_count;
        private int shots_count;
        private boolean can_upload_shot;
        private String type;
        private boolean pro;
        private String buckets_url;
        private String followers_url;
        private String following_url;
        private String likes_url;
        private String projects_url;
        private String shots_url;
        private String created_at;
        private String updated_at;
        private int members_count;
        private String members_url;
        private String team_shots_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getHtml_url() {
            return html_url;
        }

        public void setHtml_url(String html_url) {
            this.html_url = html_url;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public LinksBean getLinks() {
            return links;
        }

        public void setLinks(LinksBean links) {
            this.links = links;
        }

        public int getBuckets_count() {
            return buckets_count;
        }

        public void setBuckets_count(int buckets_count) {
            this.buckets_count = buckets_count;
        }

        public int getComments_received_count() {
            return comments_received_count;
        }

        public void setComments_received_count(int comments_received_count) {
            this.comments_received_count = comments_received_count;
        }

        public int getFollowers_count() {
            return followers_count;
        }

        public void setFollowers_count(int followers_count) {
            this.followers_count = followers_count;
        }

        public int getFollowings_count() {
            return followings_count;
        }

        public void setFollowings_count(int followings_count) {
            this.followings_count = followings_count;
        }

        public int getLikes_count() {
            return likes_count;
        }

        public void setLikes_count(int likes_count) {
            this.likes_count = likes_count;
        }

        public int getLikes_received_count() {
            return likes_received_count;
        }

        public void setLikes_received_count(int likes_received_count) {
            this.likes_received_count = likes_received_count;
        }

        public int getProjects_count() {
            return projects_count;
        }

        public void setProjects_count(int projects_count) {
            this.projects_count = projects_count;
        }

        public int getRebounds_received_count() {
            return rebounds_received_count;
        }

        public void setRebounds_received_count(int rebounds_received_count) {
            this.rebounds_received_count = rebounds_received_count;
        }

        public int getShots_count() {
            return shots_count;
        }

        public void setShots_count(int shots_count) {
            this.shots_count = shots_count;
        }

        public boolean isCan_upload_shot() {
            return can_upload_shot;
        }

        public void setCan_upload_shot(boolean can_upload_shot) {
            this.can_upload_shot = can_upload_shot;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isPro() {
            return pro;
        }

        public void setPro(boolean pro) {
            this.pro = pro;
        }

        public String getBuckets_url() {
            return buckets_url;
        }

        public void setBuckets_url(String buckets_url) {
            this.buckets_url = buckets_url;
        }

        public String getFollowers_url() {
            return followers_url;
        }

        public void setFollowers_url(String followers_url) {
            this.followers_url = followers_url;
        }

        public String getFollowing_url() {
            return following_url;
        }

        public void setFollowing_url(String following_url) {
            this.following_url = following_url;
        }

        public String getLikes_url() {
            return likes_url;
        }

        public void setLikes_url(String likes_url) {
            this.likes_url = likes_url;
        }

        public String getProjects_url() {
            return projects_url;
        }

        public void setProjects_url(String projects_url) {
            this.projects_url = projects_url;
        }

        public String getShots_url() {
            return shots_url;
        }

        public void setShots_url(String shots_url) {
            this.shots_url = shots_url;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getMembers_count() {
            return members_count;
        }

        public void setMembers_count(int members_count) {
            this.members_count = members_count;
        }

        public String getMembers_url() {
            return members_url;
        }

        public void setMembers_url(String members_url) {
            this.members_url = members_url;
        }

        public String getTeam_shots_url() {
            return team_shots_url;
        }

        public void setTeam_shots_url(String team_shots_url) {
            this.team_shots_url = team_shots_url;
        }

        public static class LinksBean implements Parcelable {
            /**
             * web : http://www.ui8.net
             * twitter : https://twitter.com/ui8net
             */

            private String web;
            private String twitter;

            public String getWeb() {
                return web;
            }

            public void setWeb(String web) {
                this.web = web;
            }

            public String getTwitter() {
                return twitter;
            }

            public void setTwitter(String twitter) {
                this.twitter = twitter;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.web);
                dest.writeString(this.twitter);
            }

            public LinksBean() {
            }

            protected LinksBean(Parcel in) {
                this.web = in.readString();
                this.twitter = in.readString();
            }

            public static final Parcelable.Creator<LinksBean> CREATOR = new Parcelable.Creator<LinksBean>() {
                @Override
                public LinksBean createFromParcel(Parcel source) {
                    return new LinksBean(source);
                }

                @Override
                public LinksBean[] newArray(int size) {
                    return new LinksBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.username);
            dest.writeString(this.html_url);
            dest.writeString(this.avatar_url);
            dest.writeString(this.bio);
            dest.writeString(this.location);
            dest.writeParcelable(this.links, flags);
            dest.writeInt(this.buckets_count);
            dest.writeInt(this.comments_received_count);
            dest.writeInt(this.followers_count);
            dest.writeInt(this.followings_count);
            dest.writeInt(this.likes_count);
            dest.writeInt(this.likes_received_count);
            dest.writeInt(this.projects_count);
            dest.writeInt(this.rebounds_received_count);
            dest.writeInt(this.shots_count);
            dest.writeByte(this.can_upload_shot ? (byte) 1 : (byte) 0);
            dest.writeString(this.type);
            dest.writeByte(this.pro ? (byte) 1 : (byte) 0);
            dest.writeString(this.buckets_url);
            dest.writeString(this.followers_url);
            dest.writeString(this.following_url);
            dest.writeString(this.likes_url);
            dest.writeString(this.projects_url);
            dest.writeString(this.shots_url);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeInt(this.members_count);
            dest.writeString(this.members_url);
            dest.writeString(this.team_shots_url);
        }

        public TeamBean() {
        }

        protected TeamBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.username = in.readString();
            this.html_url = in.readString();
            this.avatar_url = in.readString();
            this.bio = in.readString();
            this.location = in.readString();
            this.links = in.readParcelable(LinksBean.class.getClassLoader());
            this.buckets_count = in.readInt();
            this.comments_received_count = in.readInt();
            this.followers_count = in.readInt();
            this.followings_count = in.readInt();
            this.likes_count = in.readInt();
            this.likes_received_count = in.readInt();
            this.projects_count = in.readInt();
            this.rebounds_received_count = in.readInt();
            this.shots_count = in.readInt();
            this.can_upload_shot = in.readByte() != 0;
            this.type = in.readString();
            this.pro = in.readByte() != 0;
            this.buckets_url = in.readString();
            this.followers_url = in.readString();
            this.following_url = in.readString();
            this.likes_url = in.readString();
            this.projects_url = in.readString();
            this.shots_url = in.readString();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.members_count = in.readInt();
            this.members_url = in.readString();
            this.team_shots_url = in.readString();
        }

        public static final Parcelable.Creator<TeamBean> CREATOR = new Parcelable.Creator<TeamBean>() {
            @Override
            public TeamBean createFromParcel(Parcel source) {
                return new TeamBean(source);
            }

            @Override
            public TeamBean[] newArray(int size) {
                return new TeamBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeParcelable(this.images, flags);
        dest.writeInt(this.views_count);
        dest.writeInt(this.likes_count);
        dest.writeInt(this.comments_count);
        dest.writeInt(this.attachments_count);
        dest.writeInt(this.rebounds_count);
        dest.writeInt(this.buckets_count);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.html_url);
        dest.writeString(this.attachments_url);
        dest.writeString(this.buckets_url);
        dest.writeString(this.comments_url);
        dest.writeString(this.likes_url);
        dest.writeString(this.projects_url);
        dest.writeString(this.rebounds_url);
        dest.writeByte(this.animated ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.team, flags);
        dest.writeStringList(this.tags);
    }

    public PureShot() {
    }

    protected PureShot(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.images = in.readParcelable(ImagesBean.class.getClassLoader());
        this.views_count = in.readInt();
        this.likes_count = in.readInt();
        this.comments_count = in.readInt();
        this.attachments_count = in.readInt();
        this.rebounds_count = in.readInt();
        this.buckets_count = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.html_url = in.readString();
        this.attachments_url = in.readString();
        this.buckets_url = in.readString();
        this.comments_url = in.readString();
        this.likes_url = in.readString();
        this.projects_url = in.readString();
        this.rebounds_url = in.readString();
        this.animated = in.readByte() != 0;
        this.team = in.readParcelable(TeamBean.class.getClassLoader());
        this.tags = in.createStringArrayList();
    }

    public static final Parcelable.Creator<PureShot> CREATOR = new Parcelable.Creator<PureShot>() {
        @Override
        public PureShot createFromParcel(Parcel source) {
            return new PureShot(source);
        }

        @Override
        public PureShot[] newArray(int size) {
            return new PureShot[size];
        }
    };
}
