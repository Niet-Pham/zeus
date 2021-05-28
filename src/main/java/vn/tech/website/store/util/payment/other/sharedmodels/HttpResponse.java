package vn.tech.website.store.util.payment.other.sharedmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okhttp3.Headers;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HttpResponse {
    int status;
    String data;
    Headers headers;
}
