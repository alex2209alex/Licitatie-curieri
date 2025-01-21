package ro.fmi.unibuc.licitatie_curieri;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import ro.fmi.unibuc.licitatie_curieri.common.orderhandler.OrderHandler;

@SpringBootApplication
@RequiredArgsConstructor
public class LicitatieCurieriApplication {
	private final OrderHandler orderHandler;

	public static void main(String[] args) {
		SpringApplication.run(LicitatieCurieriApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup() {
		orderHandler.handleOrdersInAuctionOnApplicationStartup();
	}
}
