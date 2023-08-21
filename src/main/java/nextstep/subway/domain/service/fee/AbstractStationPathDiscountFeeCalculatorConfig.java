package nextstep.subway.domain.service.fee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AbstractStationPathDiscountFeeCalculatorConfig {

    @Bean
    public AbstractStationPathDiscountFeeCalculator discountFeeCalculator() {
        final AbstractStationPathDiscountFeeCalculator childrenDiscountFeeCalculator = ChildrenDiscountFeeCalculator.builder()
                .build();

        final AbstractStationPathDiscountFeeCalculator teenAgerDiscountFeeCalculator = TeenAgerDiscountFeeCalculator.builder()
                .nextDiscountFeeCalculator(childrenDiscountFeeCalculator)
                .build();

        return teenAgerDiscountFeeCalculator;
    }
}
