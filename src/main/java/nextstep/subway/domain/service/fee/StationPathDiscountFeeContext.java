package nextstep.subway.domain.service.fee;

import lombok.Builder;
import lombok.Getter;
import nextstep.member.domain.Member;

import java.math.BigDecimal;

@Getter
@Builder
public class StationPathDiscountFeeContext {
    private BigDecimal totalFee;
    private Member member;
}
