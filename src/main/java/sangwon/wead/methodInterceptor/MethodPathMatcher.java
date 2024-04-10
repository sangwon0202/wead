package sangwon.wead.methodInterceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MethodPathMatcher {

    @AllArgsConstructor
    static private class PatternAndMethod {
        public String pattern;
        public HttpMethod httpMethod;
    }

    private List<PatternAndMethod> includes = new ArrayList<>();
    private List<PatternAndMethod> excludes = new ArrayList<>();
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public void addInclude(String pattern, HttpMethod httpMethod) {
        includes.add(new PatternAndMethod(pattern, httpMethod));
    }

    public void addExclude(String pattern, HttpMethod httpMethod) {
        excludes.add(new PatternAndMethod(pattern, httpMethod));
    }

    public boolean match(String URI, String method) {
        HttpMethod httpMethod = method.equals("GET") ? HttpMethod.GET :  HttpMethod.POST;

        boolean includeFlag = includes.stream().anyMatch(patternAndMethod -> {

                    boolean result = antPathMatcher.match(patternAndMethod.pattern, URI)
                            && ((patternAndMethod.httpMethod == HttpMethod.ALL) || httpMethod == patternAndMethod.httpMethod);

                    return result;
                });

        boolean excludeFlag = excludes.stream().anyMatch(patternAndMethod ->
                antPathMatcher.match(patternAndMethod.pattern, URI)
                        && (patternAndMethod.httpMethod == HttpMethod.ALL) || httpMethod == patternAndMethod.httpMethod);

        return includeFlag && !excludeFlag;
    }

}
