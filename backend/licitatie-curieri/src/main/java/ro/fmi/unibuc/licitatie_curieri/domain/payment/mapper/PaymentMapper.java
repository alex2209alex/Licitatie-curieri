package ro.fmi.unibuc.licitatie_curieri.domain.payment.mapper;

import com.twilio.rest.api.v2010.account.call.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.fmi.unibuc.licitatie_curieri.controller.stripe_payment.models.CreateIntentResponseDto;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PaymentMapper {
    CreateIntentResponseDto toCreateIntentResponseDto(String clientSecret, String paymentIntentId);
}
