package nextstep.subway.domain.service.fee;

import java.math.BigDecimal;
import java.util.Optional;

public abstract class AbstractStationPathDiscountFeeCalculator {

    public AbstractStationPathDiscountFeeCalculator(AbstractStationPathDiscountFeeCalculator nextDiscountFeeCalculator) {
        this.nextDiscountFeeCalculator = nextDiscountFeeCalculator;
    }

    private final AbstractStationPathDiscountFeeCalculator nextDiscountFeeCalculator;

    public BigDecimal calculateTotalDiscountFee(StationPathDiscountFeeContext context) {
        final BigDecimal nextTotalDiscountFee = Optional.ofNullable(nextDiscountFeeCalculator)
                .map(nextDiscountFeeCalculator -> nextDiscountFeeCalculator.calculateTotalDiscountFee(context))
                .orElse(BigDecimal.ZERO);

        if (!support(context)) {
            return nextTotalDiscountFee;
        }

        return calculateDiscountFee(context).add(nextTotalDiscountFee);
    }

    public abstract boolean support(StationPathDiscountFeeContext context);

    protected abstract BigDecimal calculateDiscountFee(StationPathDiscountFeeContext context);
}
