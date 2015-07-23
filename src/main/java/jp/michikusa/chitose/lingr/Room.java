package jp.michikusa.chitose.lingr;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Room
    implements LingrResponse, Serializable
{
    @Data
    public static class RoomInfo
        implements Serializable
    {
        @Key
        private String id;
        @Key("is_public")
        private boolean isPublic;
        @Key
        private String name;
        @Key
        private String blurb;
        @Key("faved_message_ids")
        private List<String> favedMessageIds;
        @Key
        private Roster roster;
        @Key
        private List<Message> messages;
    }

    @Data
    public static class Roster
        implements Serializable
    {
        @Key
        private List<Bot> bots;
        @Key
        private List<Member> members;
    }
    @Data
    public static class Bot
        implements Serializable
    {
        @Key
        private String id;
        @Key
        private String name;
        @Key("icon_url")
        private String iconUrl;
        @Key
        private String status;
    }

    @Data
    public static class Member
        implements Serializable
    {
        @Key
        private String name;
        @Key
        private String username;
        @Key("icon_url")
        private String iconUrl;
        @Key("is_online")
        private boolean isOnline;
        @Key("is_owner")
        private boolean isOwner;
        @Key
        private boolean pokeable;
        @Key
        private String timestamp;
    }

    @Data
    public static class Message
        implements Serializable
    {
        @Key
        private String id;
        @Key("local_id")
        private String localId;
        @Key("public_session_id")
        private String publicSessionId;
        @Key
        private String room;
        @Key("speaker_id")
        private String speakerId;
        @Key
        private String nickname;
        @Key("icon_url")
        private String iconUrl;
        @Key
        private String text;
        @Key
        private String timestamp;
        @Key
        private String type;
    }

    @Key
    private List<RoomInfo> rooms;

    @Key
    private String status;
    @Key
    private String code;
    @Key
    private String detail;
}
