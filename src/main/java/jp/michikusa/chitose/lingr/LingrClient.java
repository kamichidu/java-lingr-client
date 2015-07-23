package jp.michikusa.chitose.lingr;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.protocol.HttpRequestExecutor;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Key;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import jp.michikusa.chitose.lingr.Events.Event;
import jp.michikusa.chitose.lingr.Room.Message;
import lombok.Data;

import static com.google.common.base.Preconditions.*;

public class LingrClient
{
	public Session createSession(CharSequence user, CharSequence password)
		throws IOException, LingrException
	{
		return this.createSession(user, password, null);
	}

	public Session createSession(CharSequence user, CharSequence password, CharSequence apiKey)
		throws IOException, LingrException
	{
		final GenericUrl url= endpoint.clone();
		url.appendRawPath("session/create");

		final GenericJson data= new GenericJson();
		data.set("user", user.toString());
		data.set("password", password.toString());
		if(apiKey != null && !"".equals(apiKey.toString()))
		{
			data.set("apiKey", apiKey.toString());
		}

		final HttpRequest req= this.newPostRequest(url, data);
		return this.sendRequest(req, Session.class);
	}

	public Session verifySession(Session session)
		throws IOException
	{
		final GenericUrl url= endpoint.clone();
		url.appendRawPath("session/verify");
		
		final GenericJson data= new GenericJson();
		data.set("session", session.getSession());

		final HttpRequest req= this.newPostRequest(url, data);
		try
		{
			return this.sendRequest(req, Session.class);
		}
		catch(LingrException e)
		{
			// expired
			return null;
		}
	}

	public void destroySession(Session session)
		throws IOException, LingrException
	{
		final GenericUrl url= endpoint.clone();
		url.appendRawPath("session/destroy");
		
		final GenericJson data= new GenericJson();
		data.set("session", session.getSession());

		final HttpRequest req= this.newPostRequest(url, data);
		this.sendRequest(req, GenericLingrResponse.class);
	}

	public Iterable<String> getRooms(Session session)
		throws IOException
	{
		final GenericUrl url= endpoint.clone();
		url.appendRawPath("user/get_rooms");

		final GenericJson data= new GenericJson();
		data.set("session", session.getSession());

		final HttpRequest req= this.newPostRequest(url, data);
		final Rooms rooms= this.sendRequest(req, Rooms.class);
		return rooms.getRooms();
	}
	
	public Room showRoom(Session session, Iterable<? extends CharSequence> roomIds)
		throws IOException, LingrException
	{
		checkArgument(!Iterables.isEmpty(roomIds));

		final GenericUrl url= endpoint.clone();
		url.appendRawPath("room/show");
		
		final GenericJson data= new GenericJson();
		data.set("session", session.getSession());
		data.set("rooms", Joiner.on(',').join(roomIds));
		
		final HttpRequest req= this.newPostRequest(url, data);
		return this.sendRequest(req, Room.class);
	}

	public Room showRoom(Session session, CharSequence roomId, CharSequence... roomIds)
		throws IOException, LingrException
	{
		final Set<String> ids= new HashSet<>();
		ids.add(roomId.toString());
		for(final CharSequence id : roomIds)
		{
			ids.add(id.toString());
		}
		return this.showRoom(session, ids);
	}
	
	public Archive getArchive(Session session, CharSequence roomId, CharSequence lastMessageId, int limit)
		throws IOException, LingrException
	{
		final GenericUrl url= endpoint.clone();
		url.appendRawPath("room/get_archives");

		final GenericJson data= new GenericJson();
		data.set("session", session.getSession());
		data.set("room", roomId.toString());
		data.set("before", lastMessageId.toString());
		data.set("limit", limit);
		
		final HttpRequest req= this.newPostRequest(url, data);
		return this.sendRequest(req, Archive.class);
	}
	
	public long subscribe(Session session, boolean reset, Iterable<? extends CharSequence> roomIds)
		throws IOException, LingrException
	{
		final GenericUrl url= endpoint.clone();
		url.appendRawPath("room/subscribe");

		final GenericJson data= new GenericJson();
		data.set("session", session.getSession());
		data.set("rooms", Joiner.on(',').join(roomIds));
		data.set("reset", reset);
		
		final HttpRequest req= this.newPostRequest(url, data);
		final GenericLingrResponse resp= this.sendRequest(req, GenericLingrResponse.class);
		final Number counter= (Number)resp.get("counter");
		return counter.longValue();
	}

	public long subscribe(Session session, boolean reset, CharSequence roomId, CharSequence... roomIds)
		throws IOException, LingrException
	{
		final Set<String> ids= new HashSet<>();
		ids.add(roomId.toString());
		for(final CharSequence id : roomIds)
		{
			ids.add(id.toString());
		}
		return this.subscribe(session, reset, ids);
	}
	
	public long unsubscribe(Session session, Iterable<? extends CharSequence> roomIds)
		throws IOException, LingrException
	{
		final GenericUrl url= endpoint.clone();
		url.appendRawPath("room/unsubscribe");

		final GenericJson data= new GenericJson();
		data.set("session", session.getSession());
		data.set("rooms", Joiner.on(',').join(roomIds));

		final HttpRequest req= this.newPostRequest(url, data);
		final GenericLingrResponse resp= this.sendRequest(req, GenericLingrResponse.class);
		final Number counter= (Number)resp.get("counter");
		return counter.longValue();
	}

	public long unsubscribe(Session session, CharSequence roomId, CharSequence...roomIds)
		throws IOException, LingrException
	{
		final Set<String> ids= new HashSet<>();
		ids.add(roomId.toString());
		for(final CharSequence id : roomIds)
		{
			ids.add(id.toString());
		}
		return this.unsubscribe(session, ids);
	}
	
	public Message say(Session session, CharSequence roomId, CharSequence nickname, CharSequence text)
		throws IOException, LingrException
	{
		final GenericUrl url= endpoint.clone();
		url.appendRawPath("room/say");
		
		final GenericJson data= new GenericJson();
		data.set("session", session.getSession());
		data.set("room", roomId.toString());
		data.set("nickname", nickname.toString());
		data.set("text", text.toString());
		
		final HttpRequest req= this.newPostRequest(url, data);
		final Say say= this.sendRequest(req, Say.class);
		return say.getMessage();
	}
	
	public Events observe(Session session, long counter, final long timeoutLength, final TimeUnit unit)
		throws IOException, LingrException
	{
		final GenericUrl url= endpoint.clone();
		url.setPort(8080);
		url.appendRawPath("event/observe");

		url.set("session", session.getSession());
		url.set("counter", counter);
		
		final HttpRequestFactory requestFactory= transport.createRequestFactory(new HttpRequestInitializer(){
			@Override
			public void initialize(HttpRequest request) throws IOException {
				request.setParser(factory.createJsonObjectParser());
				request.setConnectTimeout((int)TimeUnit.SECONDS.toMillis(5));
				request.setReadTimeout((int)unit.toMillis(timeoutLength));
			}
		});
		final HttpRequest request= requestFactory.buildGetRequest(url);
		try
		{
			return this.sendRequest(request, Events.class);
		}
		catch(SocketTimeoutException e)
		{
			final Events events= new Events();
			events.setStatus("ok");
			events.setCounter(counter);
			events.setEvents(Collections.<Event>emptyList());
			return events;
		}
	}

	private HttpRequest newPostRequest(GenericUrl url, GenericJson data)
		throws IOException
	{
		final HttpRequestFactory requestFactory= transport.createRequestFactory(new HttpRequestInitializer(){
			@Override
			public void initialize(HttpRequest request)
				throws IOException
			{
				request.setParser(factory.createJsonObjectParser());
				request.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(5));
			}
		});
		return requestFactory.buildPostRequest(url, new JsonHttpContent(factory, data));
	}
	
	private <T extends LingrResponse> T sendRequest(HttpRequest request, Class<T> type)
		throws IOException, LingrException
	{
		final HttpResponse res= request.execute();
		try
		{
			if(res.getStatusCode() >= 200 && res.getStatusCode() < 300)
			{
				final T ret= res.parseAs(type);
				if("ok".equals(ret.getStatus()))
				{
					return ret;
				}
				else
				{
					throw new LingrException(ret);
				}
			}
			else
			{
				throw new IOException(String.format("%d - %s", res.getStatusCode(), res.getStatusMessage()));
			}
		}
		finally
		{
			res.disconnect();
		}
	}

	private static final HttpTransport transport= new NetHttpTransport();
	private static final JsonFactory factory= new GsonFactory();
	private static final GenericUrl endpoint= new GenericUrl("http://lingr.com/api/");
}