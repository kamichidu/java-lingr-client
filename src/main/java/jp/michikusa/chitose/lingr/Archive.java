package jp.michikusa.chitose.lingr;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

import jp.michikusa.chitose.lingr.Room.Message;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Archive
	implements LingrResponse, Serializable
{
	@Key
	private List<Message> messages;

	@Key
	private String status;
	@Key
	private String code;
	@Key
	private String detail;
}
