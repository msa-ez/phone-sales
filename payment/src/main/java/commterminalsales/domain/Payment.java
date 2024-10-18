package commterminalsales.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.Table;

import commterminalsales.PaymentApplication;
import lombok.Data;

@Entity
@Table(name = "Payment_table")
@Data
// <<< DDD / Aggregate Root
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private String product;

    private String customerId;

    private String paymentMethod;

    @PostPersist
    public void onPostPersist() {
        PaymentCreated paymentCreated = new PaymentCreated(this);
        paymentCreated.publishAfterCommit();
    }

    public static PaymentRepository repository() {
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(
                PaymentRepository.class);
        return paymentRepository;
    }

    // <<< Clean Arch / Port Method
    public static void pg에결제요청(OrderPlaced orderPlaced) {
        System.out.println("PG 결재 요청");

        if (Math.random() > 0.6)
            throw new RuntimeException("PG 연동 실패");

        System.out.println("PG 결재 성공");

    }
    // >>> Clean Arch / Port Method

}
// >>> DDD / Aggregate Root
