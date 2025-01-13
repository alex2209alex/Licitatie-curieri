package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.stripe_payment.api.StripePaymentApi;
import ro.fmi.unibuc.licitatie_curieri.controller.stripe_payment.models.CreateIntentDto;
import ro.fmi.unibuc.licitatie_curieri.controller.stripe_payment.models.CreateIntentResponseDto;
import ro.fmi.unibuc.licitatie_curieri.service.PaymentService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController implements StripePaymentApi {
    private final PaymentService paymentService;

    @Override
    public CreateIntentResponseDto createPaymentIntent(@RequestBody CreateIntentDto createIntentDto) {
         log.info(LogMessageUtils.PAYMENT_METHOD, createIntentDto.getAmount(), createIntentDto.getCurrency());

        return paymentService.createPaymentIntent(createIntentDto);
    }
}
