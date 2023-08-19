package app.core.utils;

import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Singleton
public class HttpService {

    static HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    @Inject
    Logger log;

    // todo from GET to generic "fetch(method, url, body, header, etc...)"
    public CompletableFuture<HttpResponse<String>> GET(String url, String token) {
        log.info("-> HTTP Request / GET -" + url);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
                .header("X-Auth-Token", token)
                .timeout(Duration.ofSeconds(3))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
