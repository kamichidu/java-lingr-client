package jp.michikusa.chitose.lingr;

import com.google.api.client.util.Key;

import java.io.Serializable;

import jp.michikusa.chitose.lingr.Room.Message;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Say
    implements LingrResponse, Serializable
{
    @Key
    private Message message;

    @Key
    private String status;
    @Key
    private String code;
    @Key
    private String detail;
}
