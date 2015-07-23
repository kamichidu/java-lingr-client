package jp.michikusa.chitose.lingr;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Rooms
    implements LingrResponse, Serializable
{
    @Key
    private List<String> rooms;

    @Key
    private String status;
    @Key
    private String code;
    @Key
    private String detail;
}
