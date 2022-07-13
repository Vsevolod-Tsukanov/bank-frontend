package com.haulmont.bankfrontend.views;

import com.haulmont.bankfrontend.dto.responses.CreditDetailsResponse;
import com.haulmont.bankfrontend.dto.responses.CreditOfferResponse;
import com.haulmont.bankfrontend.forms.CreditOfferForm;
import com.haulmont.bankfrontend.layouts.MainLayout;
import com.haulmont.bankfrontend.service.ClientRestService;
import com.haulmont.bankfrontend.service.CreditDetailsRestService;
import com.haulmont.bankfrontend.service.CreditOfferRestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Route(value = "/creditOffers",layout = MainLayout.class)
public class CreditOfferView extends VerticalLayout {

    private final CreditOfferRestService creditOfferRestService;
    private final CreditDetailsRestService creditDetailsRestService;
    private final ClientRestService clientRestService;

    CreditOfferForm form;
    Div content;

    private final Grid<CreditOfferResponse> grid = new Grid<>(CreditOfferResponse.class);
    private RestTemplate restTemplate;

    public CreditOfferView() {

        setSizeFull();

        initRestTemplate();
        clientRestService = new ClientRestService(restTemplate);
        creditOfferRestService = new CreditOfferRestService(restTemplate);
        creditDetailsRestService = new CreditDetailsRestService(restTemplate);
        configureGrid();

        configureForm();
        form.setSizeFull();
        content = new Div(grid, form);
        content.setSizeFull();

        add(getAddBtn(), content);
        refreshData();
        closeEditor();
    }

    private void closeEditor() {
        form.setCreditOffer(null);
        form.setVisible(false);
    }

    private void configureGrid() {
        grid.addClassNames("client-grid");
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.removeAllColumns();

        grid.addColumn(CreditOfferResponse::getId).setHeader("ID");
        grid.addColumn(CreditOfferResponse::getClientId).setHeader("Client ID");
        grid.addColumn(CreditOfferResponse::getCreditId).setHeader("Credit ID");
        grid.addColumn(CreditOfferResponse::getSumOfCredit).setHeader("Sum of Credit");
        grid.addColumn(CreditOfferResponse::getMonthsOfCredit).setHeader("Months of Credit");
        grid.addColumn(CreditOfferResponse::getPaymentScheduleId).setHeader("Payment Schedule Id");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editCreditOffer(event.getValue()));

    }

    private void editCreditOffer(CreditOfferResponse creditOffer) {
        if (creditOffer == null) {
            closeEditor();
        } else {
            form.setCreditOffer(creditOffer);
            form.getDelete().setVisible(true);
            form.setVisible(true);
        }
    }

    private RestTemplate initRestTemplate() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    private void refreshData() {
        List<CreditOfferResponse> creditOffers = creditOfferRestService.getCreditOffers();
        grid.setItems(creditOffers);
    }

    private void configureForm() {
        form = new CreditOfferForm(clientRestService.getClients(), creditDetailsRestService.getCreditDetails());
        form.addListener(CreditOfferForm.SaveEvent.class, this::saveCreditOffer);
        form.addListener(CreditOfferForm.DeleteEvent.class, this::deleteCreditOffer);
        form.addListener(CreditOfferForm.CloseEvent.class, e -> closeEditor());
    }

    private HorizontalLayout getAddBtn() {
        Button addCreditOffer = new Button("Add Credit Offer");
        addCreditOffer.addClickListener(click -> addCreditOffer());

        HorizontalLayout toolbar = new HorizontalLayout(addCreditOffer);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addCreditOffer() {
        grid.asSingleSelect().clear();
        editCreditOffer(new CreditOfferResponse());
    }

    private void saveCreditOffer(CreditOfferForm.SaveEvent event) {

        CreditDetailsResponse detail = creditDetailsRestService.getCreditDetails(event.getCreditOffer().getCreditId());

        if (event.getCreditOffer().getId() != null){
            creditOfferRestService.updateCreditOffer(event.getCreditOffer());
        }else {
            CreditOfferResponse creditOffer = event.getCreditOffer();
            creditOffer.setCreditDetails(detail);
            creditOfferRestService.createCreditOffer(creditOffer);
        }
        refreshData();
        closeEditor();
    }

    private void deleteCreditOffer(CreditOfferForm.DeleteEvent event) {
        creditOfferRestService.deleteCreditOffer(event.getCreditOffer().getId());
        refreshData();
        closeEditor();
    }
}
