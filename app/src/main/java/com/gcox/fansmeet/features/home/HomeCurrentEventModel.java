package com.gcox.fansmeet.features.home;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 7/13/2016.
 */
public class HomeCurrentEventModel implements Parcelable {

    @SerializedName("EventId")
    @Expose
    private int EventId;
    @SerializedName("Title")
    @Expose
    private String Title;
    @SerializedName("Description")
    @Expose
    private String Description;
    @SerializedName("Image")
    @Expose
    private String Image;
    @SerializedName("BeginDate")
    @Expose
    private String BeginDate;
    @SerializedName("EndDate")
    @Expose
    private String EndDate;
    @SerializedName("ActionType")
    @Expose
    private int ActionType;
    @SerializedName("ActionValue")
    @Expose
    private String ActionValue;
    @SerializedName("OrderIndex")
    @Expose
    private int OrderIndex;
    @SerializedName("DetailUrl")
    @Expose
    private String DetailUrl;

    private List<EventDetailsBean> EventDetails;


    public int getEventId() {
        return EventId;
    }

    public void setEventId(int EventId) {
        this.EventId = EventId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getBeginDate() {
        return BeginDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public int getActionType() {
        return ActionType;
    }

    public String getActionValue() {
        return ActionValue;
    }

    public int getOrderIndex() {
        return OrderIndex;
    }

    public List<EventDetailsBean> getEventDetails() {
        return EventDetails;
    }

    public String getDetailUrl() {
        return DetailUrl;
    }

    public static class EventDetailsBean implements Parcelable {
        @SerializedName("EventDetailId")
        @Expose
        private int EventDetailId;
        @SerializedName("EventId")
        @Expose
        private int EventId;
        @SerializedName("Title")
        @Expose
        private String Title;
        @SerializedName("Description")
        @Expose
        private String Description;
        @SerializedName("Image")
        @Expose
        private String Image;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.EventDetailId);
            dest.writeInt(this.EventId);
            dest.writeString(this.Title);
            dest.writeString(this.Description);
            dest.writeString(this.Image);
        }

        public EventDetailsBean() {
        }

        protected EventDetailsBean(Parcel in) {
            this.EventDetailId = in.readInt();
            this.EventId = in.readInt();
            this.Title = in.readString();
            this.Description = in.readString();
            this.Image = in.readString();
        }

        public static final Creator<EventDetailsBean> CREATOR = new Creator<EventDetailsBean>() {
            @Override
            public EventDetailsBean createFromParcel(Parcel source) {
                return new EventDetailsBean(source);
            }

            @Override
            public EventDetailsBean[] newArray(int size) {
                return new EventDetailsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.EventId);
        dest.writeString(this.Title);
        dest.writeString(this.Description);
        dest.writeString(this.Image);
        dest.writeString(this.BeginDate);
        dest.writeString(this.EndDate);
        dest.writeInt(this.ActionType);
        dest.writeString(this.ActionValue);
        dest.writeInt(this.OrderIndex);
        dest.writeTypedList(this.EventDetails);
    }

    public HomeCurrentEventModel() {
    }

    protected HomeCurrentEventModel(Parcel in) {
        this.EventId = in.readInt();
        this.Title = in.readString();
        this.Description = in.readString();
        this.Image = in.readString();
        this.BeginDate = in.readString();
        this.EndDate = in.readString();
        this.ActionType = in.readInt();
        this.ActionValue = in.readString();
        this.OrderIndex = in.readInt();
        this.EventDetails = in.createTypedArrayList(EventDetailsBean.CREATOR);
    }

    public static final Creator<HomeCurrentEventModel> CREATOR = new Creator<HomeCurrentEventModel>() {
        @Override
        public HomeCurrentEventModel createFromParcel(Parcel source) {
            return new HomeCurrentEventModel(source);
        }

        @Override
        public HomeCurrentEventModel[] newArray(int size) {
            return new HomeCurrentEventModel[size];
        }
    };

    @Override
    public String toString() {
        return "HomeCurrentEventModel{" +
                "EventId=" + EventId +
                ", ActionType=" + ActionType +
                '}';
    }
}
