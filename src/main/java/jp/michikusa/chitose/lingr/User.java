package jp.michikusa.chitose.lingr;

import java.io.Serializable;

import com.google.api.client.util.Key;

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
