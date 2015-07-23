package jp.michikusa.chitose.lingr;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper=true)
public class GenericLingrResponse
    extends GenericJson
    implements LingrResponse, Serializable
{
    @Key
    private String status;
    @Key
    private String code;
    @Key
    private String detail;
}
