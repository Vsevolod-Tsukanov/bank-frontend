package com.haulmont.bankfrontend.views;

import com.haulmont.bankfrontend.dto.responses.BankResponse;
import com.haulmont.bankfrontend.layouts.MainLayout;
import com.haulmont.bankfrontend.service.BankRestService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Route(value = "",layout = MainLayout.class)
public class BankView extends VerticalLayout {

    private final BankRestService bankRestService;
    Div content;

    private Grid<BankResponse> grid = new Grid<>(BankResponse.class);
    private RestTemplate restTemplate;


    public BankView() {

        setSizeFull();

        initRestTemplate();
        bankRestService = new BankRestService(restTemplate);
        configureGrid();

        content = new Div(grid);
        content.setSizeFull();

        add(getBtns(), content);
        refreshData();
    }

    private HorizontalLayout getBtns() {
        Button addBankButton = new Button("Add Bank");
        addBankButton.addClickListener(click -> saveBank());
        Button delete = new Button("Delete");


        delete.addClickListener((ComponentEventListener<ClickEvent<Button>>) clickEvent -> {
            Set<BankResponse> selected = grid.getSelectedItems();
            if (selected != null && !selected.isEmpty()) {
                bankRestService.deleteBank(selected.iterator().next().getId());
                grid.deselectAll();
                refreshData();
            }
        });

        HorizontalLayout toolbar = new HorizontalLayout(addBankButton, delete);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassNames("bank-grid");
        grid.setSizeFull();
        grid.setColumns("id", "credits", "clients");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.removeAllColumns();

        grid.addColumn(BankResponse::getId).setHeader("Id");
        grid.addColumn(credit -> credit.getCredits().size())
                .setHeader("Credits count");
        grid.addColumn(client -> client.getClients().size())
                .setHeader("Clients count");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

    }

    private RestTemplate initRestTemplate() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    private void saveBank() {
        bankRestService.createBank(new BankResponse());
        refreshData();
    }


    public void refreshData() {
        List<BankResponse> banks = bankRestService.getBanks();
        grid.setItems(banks);
    }

}
