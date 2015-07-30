package jp.michikusa.chitose.lingr;

import com.google.api.client.http.HttpTransport;

public class LingrClientFactory
{
    public static LingrClientFactory newLingrClientFactory(HttpTransport transport)
    {
        return new LingrClientFactory(transport);
    }

    public LingrClient newLingrClient()
    {
        return new LingrClient(this.transport);
    }

    private LingrClientFactory(HttpTransport transport)
    {
        this.transport= transport;
    }

    private final HttpTransport transport;
}
