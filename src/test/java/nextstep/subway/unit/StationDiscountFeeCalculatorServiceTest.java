package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.fixture.MemberSpec;
import nextstep.subway.domain.service.fee.AbstractStationPathDiscountFeeCalculator;
import nextstep.subway.domain.service.fee.StationPathDiscountFeeContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class StationDiscountFeeCalculatorServiceTest {
    @Autowired
    private AbstractStationPathDiscountFeeCalculator discountFeeCalculator;

    @DisplayName("청소년 할인 적용된 요금 계산")
    @Test
    void calculateFeeTest_Teenager_DiscountFee() {
        //given
        final BigDecimal totalFee = BigDecimal.valueOf(1250);
        final Member member = MemberSpec.of("TEST@nextstep.com", 18);

        final StationPathDiscountFeeContext context = StationPathDiscountFeeContext.builder()
                .totalFee(totalFee)
                .member(member)
                .build();

        //when
        final BigDecimal discountFee = discountFeeCalculator.calculateTotalDiscountFee(context);

        //then
        final BigDecimal twentyPercent = BigDecimal.valueOf(0.2);
        final BigDecimal expectedDiscountFee = totalFee.subtract(BigDecimal.valueOf(250)).multiply(twentyPercent);

        Assertions.assertEquals(0, discountFee.compareTo(expectedDiscountFee));
    }

    @DisplayName("어린이 할인 적용된 요금 계산")
    @Test
    void calculateFeeTest_Children_DiscountFee() {
        //given
        final BigDecimal totalFee = BigDecimal.valueOf(1250);
        final Member member = MemberSpec.of("TEST@nextstep.com", 6);

        final StationPathDiscountFeeContext context = StationPathDiscountFeeContext.builder()
                .totalFee(totalFee)
                .member(member)
                .build();

        //when
        final BigDecimal discountFee = discountFeeCalculator.calculateTotalDiscountFee(context);

        //then
        final BigDecimal fiftyPercent = BigDecimal.valueOf(0.5);
        final BigDecimal expectedDiscountFee = totalFee.subtract(BigDecimal.valueOf(250)).multiply(fiftyPercent);

        Assertions.assertEquals(0, discountFee.compareTo(expectedDiscountFee));
    }
}
