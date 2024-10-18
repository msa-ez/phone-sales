
package commterminalsales.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "spec", url = "${api.url.spec}", fallback = SpecServiceImpl.class)

public interface SpecService {
    @GetMapping(path = "/specs/{id}")
    public Spec getSpecDetail(@PathVariable String id);

}
