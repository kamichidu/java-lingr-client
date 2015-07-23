package jp.michikusa.chitose.lingr;

import com.google.api.client.util.Key;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class User
    implements Serializable
{
    @Key
    private String name;
    @Key
    private String username;
}
