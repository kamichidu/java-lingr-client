package jp.michikusa.chitose.lingr;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

import jp.michikusa.chitose.lingr.Room.Message;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Events
    implements LingrResponse, Serializable
{
    @Data
    public static class Event
        implements Serializable
    {
        @Key
        private long eventId;
        @Key
        private Message message;
        @Key
        private Presence presence;
        @Key
        private Membership membership;
    }

    @Data
    public static class Presence
        implements Serializable
    {
        @Key
        private String room;
        @Key("public_session_id")
        private String publicSessionId;
        @Key
        private String username;
        @Key
        private String nickname;
        @Key("icon_url")
        private String iconUrl;
        @Key
        private String timestamp;
        @Key
        private String status;
        @Key
        private String text;
    }

    @Data
    public static class Membership
        implements Serializable
    {
        @Key("icon_url")
        private String iconUrl;
        @Key
        private String username;
        @Key
        private String name;
        @Key("is_owner")
        private boolean isOwner;
        @Key("is_online")
        private boolean isOnline;
        @Key
        private boolean pokeable;
        @Key
        private String timestamp;
        @Key
        private String action;
        @Key
        private String room;
        @Key
        private String text;
    }

    @Key
    private long counter;
    @Key
    private List<Event> events;

    @Key
    private String status;
    @Key
    private String code;
    @Key
    private String detail;
}
