package com.haulmont.bankfrontend.views;

import com.haulmont.bankfrontend.dto.responses.ClientResponse;
import com.haulmont.bankfrontend.forms.ClientForm;
import com.haulmont.bankfrontend.layouts.MainLayout;
import com.haulmont.bankfrontend.service.BankRestService;
import com.haulmont.bankfrontend.service.ClientRestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Route(value = "/clients",layout = MainLayout.class)
public class ClientView extends VerticalLayout {

    private final ClientRestService clientRestService;
    private final BankRestService bankRestService;

    ClientForm form;
    Div content;

    private final Grid<ClientResponse> grid = new Grid<>(ClientResponse.class);
    private RestTemplate restTemplate;

    public ClientView() {

        setSizeFull();

        initRestTemplate();
        clientRestService = new ClientRestService(restTemplate);
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
        form.setClient(null);
        form.setVisible(false);
    }

    private void configureGrid() {
        grid.addClassNames("client-grid");
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.removeAllColumns();

        grid.addColumn(ClientResponse::getId).setHeader("ID");
        grid.addColumn(ClientResponse::getTelephoneNumber).setHeader("Telephone");
        grid.addColumn(ClientResponse::getEmail).setHeader("Email");
        grid.addColumn(ClientResponse::getPassportNumber).setHeader("Passport Number");
        grid.addColumn(ClientResponse::getBankId).setHeader("Bank Id");
        grid.addColumn(offer -> offer.getCreditOffers().size())
                .setHeader("Credit Offer Count");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editClient(event.getValue()));

    }

    private void editClient(ClientResponse client) {
        if (client == null) {
            closeEditor();
        } else {
            form.setClient(client);
            form.getDelete().setVisible(true);
            removeWhiteSpacesInOutput(form);
            form.setVisible(true);

        }
    }

    private void removeWhiteSpacesInOutput(ClientForm form){
        String trimTelephoneNumber = form.getTelephoneNumber().getValue().trim().replaceAll("\\D","");
        String trimPassport = form.getPassport().getValue().trim().replaceAll("\\D","");

        form.getTelephoneNumber().setValue(trimTelephoneNumber);
        form.getPassport().setValue(trimPassport);
    }

    private RestTemplate initRestTemplate() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    private void refreshData() {
        List<ClientResponse> clients = clientRestService.getClients();
        grid.setItems(clients);
    }

    private void configureForm() {
        form = new ClientForm(bankRestService.getBanks());
        form.addListener(ClientForm.SaveEvent.class, this::saveClient);
        form.addListener(ClientForm.DeleteEvent.class, this::deleteClient);
        form.addListener(ClientForm.CloseEvent.class, e -> closeEditor());
    }

    private HorizontalLayout getAddBtn() {
        Button addClientButton = new Button("Add Client");
        addClientButton.addClickListener(click -> addClient());
        addClientButton.addClickListener(click -> form.getDelete().setVisible(false));

        HorizontalLayout toolbar = new HorizontalLayout(addClientButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addClient() {
        grid.asSingleSelect().clear();
        editClient(new ClientResponse());
    }

    private void saveClient(ClientForm.SaveEvent event) {

        if (event.getClient().getId() != null){
            clientRestService.updateClient(event.getClient());
        }else {
            clientRestService.createClient(event.getClient());
        }

        refreshData();
        closeEditor();
    }

    private void deleteClient(ClientForm.DeleteEvent event) {
        clientRestService.deleteClient(event.getClient().getId());
        refreshData();
        closeEditor();
    }

}
