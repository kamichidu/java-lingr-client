package jp.michikusa.chitose.lingr;

@SuppressWarnings("serial")
public class LingrException
	extends RuntimeException
{
	public LingrException(LingrResponse resp)
	{
		super(String.format("%s - %s", resp.getCode(), resp.getDetail()));
	}
}
