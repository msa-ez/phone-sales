package commterminalsales.infra;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commterminalsales.domain.SpecRepository;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping("/specs")
@Transactional
public class SpecController {

    @Autowired
    private SpecRepository specRepository;

    // @GetMapping("/findByColorsAndManufacturer")
    // public Spec findByColorsAndManufacturer(@RequestParam(required = false)
    // String color,
    // @RequestParam(required = false) String manufacturer) {
    // Manufacturer manufacturerEnum = null;
    // if (manufacturer != null && !manufacturer.isEmpty()) {
    // try {
    // manufacturerEnum = Manufacturer.valueOf(manufacturer);
    // } catch (IllegalArgumentException e) {
    // // Handle the case where the manufacturer string does not match any enum
    // value
    // return null; // or throw a custom exception
    // }
    // }
    // return specRepository.FindByColorsAndManufacturer(color, manufacturerEnum);
    // }
}
// >>> Clean Arch / Inbound Adaptor
