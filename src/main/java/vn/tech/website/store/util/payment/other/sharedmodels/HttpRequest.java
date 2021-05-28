package vn.tech.website.store.util.payment.other.sharedmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HttpRequest {
    private String method;
    private String endpoint;
    private String payload;
    private String contentType;
}
