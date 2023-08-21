package nextstep.subway.domain.service.fee;

import lombok.Builder;
import nextstep.member.domain.Member;

import java.math.BigDecimal;

public class TeenAgerDiscountFeeCalculator extends AbstractStationPathDiscountFeeCalculator {
    private static final BigDecimal DISCOUNT_RATE_OF_TEENAGER_USER = BigDecimal.valueOf(0.2);
    private static final BigDecimal DEDUCTION_PRICE = BigDecimal.valueOf(250);

    @Builder
    public TeenAgerDiscountFeeCalculator(AbstractStationPathDiscountFeeCalculator nextDiscountFeeCalculator) {
        super(nextDiscountFeeCalculator);
    }

    @Override
    public boolean support(StationPathDiscountFeeContext context) {
        final Member member = context.getMember();

        return member.isTeenager();
    }

    @Override
    protected BigDecimal calculateDiscountFee(StationPathDiscountFeeContext context) {
        final BigDecimal totalFee = context.getTotalFee();

        return totalFee.subtract(DEDUCTION_PRICE).multiply(DISCOUNT_RATE_OF_TEENAGER_USER);
    }
}
