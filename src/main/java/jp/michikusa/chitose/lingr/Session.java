package jp.michikusa.chitose.lingr;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Session
	implements LingrResponse, Serializable
{
	@Key
	private String nickname;
	@Key("public_id")
	private String publicId;
	@Key
	private String session;
	@Key
	private User user;

	@Key
	private String status;
	@Key
	private String code;
	@Key
	private String detail;
}
