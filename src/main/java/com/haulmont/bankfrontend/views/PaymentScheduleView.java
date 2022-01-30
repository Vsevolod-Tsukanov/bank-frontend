package com.haulmont.bankfrontend.views;

import com.haulmont.bankfrontend.dto.responses.BankResponse;
import com.haulmont.bankfrontend.dto.responses.PaymentScheduleResponse;
import com.haulmont.bankfrontend.service.BankRestService;
import com.haulmont.bankfrontend.service.PaymentScheduleRestService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("/schedules")
public class PaymentScheduleView extends VerticalLayout {

    private final PaymentScheduleRestService paymentScheduleRestService;
    Div content;

    private Grid<PaymentScheduleResponse> grid = new Grid<>(PaymentScheduleResponse.class);
    private RestTemplate restTemplate;


    public PaymentScheduleView() {

        setSizeFull();

        initRestTemplate();
        paymentScheduleRestService = new PaymentScheduleRestService(restTemplate);
        configureGrid();

        content = new Div(grid);
        content.setSizeFull();

        add( content);
        refreshData();
    }

    private void configureGrid() {

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.removeAllColumns();

        grid.addColumn(PaymentScheduleResponse::getId).setHeader("Id");
        grid.addColumn(PaymentScheduleResponse::getDateOfFirstPayment).setHeader("First Payment");
        grid.addColumn(PaymentScheduleResponse::getDateOfLastPayment).setHeader("Last Payment");
        grid.addColumn(PaymentScheduleResponse::getSumOfPayment).setHeader("Payment");
        grid.addColumn(PaymentScheduleResponse::getSumOfMonthlyPayment).setHeader("Monthly Payment");
        grid.addColumn(PaymentScheduleResponse::getSumOfPrincipal).setHeader("Credit Body");
        grid.addColumn(PaymentScheduleResponse::getSumOfPercent).setHeader("Percent");
        grid.addColumn(schedule -> schedule.getCreditOffer().getId())
                .setHeader("Credit Offer ID");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private RestTemplate initRestTemplate() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public void refreshData() {
        List<PaymentScheduleResponse> banks = paymentScheduleRestService.getPaymentSchedules();
        grid.setItems(banks);
    }
}
