package com.haulmont.bankfrontend.views;


import com.haulmont.bankfrontend.dto.responses.CreditDetailsResponse;
import com.haulmont.bankfrontend.forms.CreditDetailsForm;
import com.haulmont.bankfrontend.layouts.MainLayout;
import com.haulmont.bankfrontend.service.BankRestService;
import com.haulmont.bankfrontend.service.CreditDetailsRestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route(value = "/creditDetails", layout = MainLayout.class)
public class CreditDetailsView extends VerticalLayout {

    private final CreditDetailsRestService creditDetailsRestService;
    private final BankRestService bankRestService;

    private final Grid<CreditDetailsResponse> grid = new Grid<>(CreditDetailsResponse.class);
    private RestTemplate restTemplate;

    CreditDetailsForm form;
    Div content;

    public CreditDetailsView() {

        setSizeFull();

        initRestTemplate();
        creditDetailsRestService = new CreditDetailsRestService(restTemplate);
        bankRestService = new BankRestService(restTemplate);
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
        form.setCreditDetails(null);
        form.setVisible(false);
    }

    private void configureGrid() {
        grid.addClassNames("credit-detail-grid");
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.removeAllColumns();

        grid.addColumn(CreditDetailsResponse::getId).setHeader("ID");
        grid.addColumn(CreditDetailsResponse::getCreditLimit).setHeader("Credit LImit");
        grid.addColumn(CreditDetailsResponse::getCreditPercent).setHeader("Credit Percent");
        grid.addColumn(CreditDetailsResponse::getBankId).setHeader("Bank ID");
        grid.addColumn(offer -> offer.getCreditOffers().size())
                .setHeader("Credit Offer Count");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editCreditDetails(event.getValue()));

    }

    private void editCreditDetails(CreditDetailsResponse creditDetails) {
        if (creditDetails == null) {
            closeEditor();
        } else {
            form.setCreditDetails(creditDetails);
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
        List<CreditDetailsResponse> creditDetails = creditDetailsRestService.getCreditDetails();
        grid.setItems(creditDetails);
    }

    private void configureForm() {
        form = new CreditDetailsForm(bankRestService.getBanks());
        form.addListener(CreditDetailsForm.SaveEvent.class, this::saveCreditDetails);
        form.addListener(CreditDetailsForm.DeleteEvent.class, this::deleteCreditDetails);
        form.addListener(CreditDetailsForm.CloseEvent.class, e -> closeEditor());
    }

    private HorizontalLayout getAddBtn() {
        Button addCreditDetailsButton = new Button("Add Credit Detail");
        addCreditDetailsButton.addClickListener(click -> addCreditDetails());
        addCreditDetailsButton.addClickListener(click -> form.getDelete().setVisible(false));

        HorizontalLayout toolbar = new HorizontalLayout(addCreditDetailsButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addCreditDetails() {
        grid.asSingleSelect().clear();
        editCreditDetails(new CreditDetailsResponse());
    }

    private void saveCreditDetails(CreditDetailsForm.SaveEvent event) {
        if (event.getCreditDetails().getId() != null){
            creditDetailsRestService.updateCreditDetail(event.getCreditDetails());
        }else {
            creditDetailsRestService.createCreditDetail(event.getCreditDetails());
        }
        refreshData();
        closeEditor();
    }

    private void deleteCreditDetails(CreditDetailsForm.DeleteEvent event) {
        creditDetailsRestService.deleteCreditDetails(event.getCreditDetails().getId());
        refreshData();
        closeEditor();
    }

}
