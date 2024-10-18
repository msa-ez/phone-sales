package commterminalsales.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import commterminalsales.config.kafka.KafkaProcessor;
import commterminalsales.domain.OrderPlaced;
import commterminalsales.domain.SalesStatus;
import commterminalsales.external.Spec;
import commterminalsales.external.SpecService;

@Service
public class SalesStatusViewHandler {

    // <<< DDD / CQRS
    @Autowired
    private SalesStatusRepository salesStatusRepository;

    @Autowired
    private SpecService specService;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderPlaced_then_CREATE_1(
            @Payload OrderPlaced orderPlaced) {
        try {
            if (!orderPlaced.validate())
                return;

            // view 객체 생성
            SalesStatus salesStatus = new SalesStatus();
            // view 객체에 이벤트의 Value 를 set 함
            salesStatus.setProductId(orderPlaced.getProductId());
            salesStatus.setOrderId(orderPlaced.getId());
            salesStatus.setInsurance(
                    Boolean.valueOf(orderPlaced.getInsuranceOption()));

            // Spec 을 API 호출하여 얻어옴
            Spec spec = specService.getSpecDetail(orderPlaced.getProductId());
            salesStatus.setPhoneColor(spec.getPhoneColor());
            salesStatus.setManufacturer(spec.getManufacturer().toString());

            // view 레파지 토리에 save
            salesStatusRepository.save(salesStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // >>> DDD / CQRS
}
