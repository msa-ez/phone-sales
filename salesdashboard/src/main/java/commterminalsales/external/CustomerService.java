
package commterminalsales.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer", url = "${api.url.customer}")

public interface CustomerService {
    @GetMapping(path = "/customers/{id}")
    public Customer getCustomer(@PathVariable String id);

}
