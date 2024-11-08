package commterminalsales.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.Table;

import commterminalsales.SpecApplication;
import lombok.Data;

@Entity
@Table(name = "Spec_table")
@Data
// <<< DDD / Aggregate Root
public class Spec {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String spec;

    private String phoneColor;

    private String phoneType;

    private Double cm;

    private String chip;

    private String camera;

    private String texture;

    private String battery;
    
    private String chargeType;

    private Long price;

    @Embedded
    private Photo image;

    private Manufacturer manufacturer;

    @PostPersist
    public void onPostPersist() {
    }

    public static SpecRepository repository() {
        SpecRepository specRepository = SpecApplication.applicationContext.getBean(
                SpecRepository.class);
        return specRepository;
    }

    public void register() {
        // implement business logic here:

    }
}
// >>> DDD / Aggregate Root
