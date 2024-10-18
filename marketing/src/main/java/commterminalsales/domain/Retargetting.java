package commterminalsales.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.Table;

import commterminalsales.MarketingApplication;
import lombok.Data;

@Entity
@Table(name = "Retargetting_table")
@Data
// <<< DDD / Aggregate Root
public class Retargetting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String customerId;

    private String productId;

    private Integer returnCount;

    @PostPersist
    public void onPostPersist() {
        DiscountPolicyActivated discountPolicyActivated = new DiscountPolicyActivated(
                this);
        discountPolicyActivated.publishAfterCommit();
    }

    public static RetargettingRepository repository() {
        RetargettingRepository retargettingRepository = MarketingApplication.applicationContext.getBean(
                RetargettingRepository.class);
        return retargettingRepository;
    }

    // <<< Clean Arch / Port Method
    public static void 스펙재방문시타켓팅수치증가(SpecCompared specCompared) {
        // SpecCompared 이벤트에서 필요한 정보를 가져옵니다.
        String customerId = specCompared.getCustomerId();
        String productId = specCompared.getProductId();

        // 고객 ID와 제품 ID로 Retargetting 엔티티를 조회합니다.
        repository().findByCustomerIdAndProductId(customerId, productId).ifPresentOrElse(retargetting -> {
            // 방문 횟수를 증가시킵니다.
            retargetting.setReturnCount(retargetting.getReturnCount() + 1);
            repository().save(retargetting);

            // 방문 횟수가 3회를 초과할 때만 이벤트를 발행합니다.
            if (retargetting.getReturnCount() > 3) {
                DiscountPolicyActivated discountPolicyActivated = new DiscountPolicyActivated(retargetting);
                discountPolicyActivated.publishAfterCommit();
            }
        }, () -> {
            // 엔티티가 존재하지 않을 경우 새로 생성합니다.
            Retargetting newRetargetting = new Retargetting();
            newRetargetting.setCustomerId(customerId);
            newRetargetting.setProductId(productId);
            newRetargetting.setReturnCount(1); // 최초 방문이므로 1로 설정
            repository().save(newRetargetting);
        });
    }
    // >>> Clean Arch / Port Method

}
// >>> DDD / Aggregate Root
