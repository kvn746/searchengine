package searchengine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "parser-settings")
public class ParserSettings
{
    private String userAgent;
    private String referrer;
    private String getParameters;
}
