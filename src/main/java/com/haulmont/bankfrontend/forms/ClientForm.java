package com.haulmont.bankfrontend.forms;

import com.haulmont.bankfrontend.dto.responses.BankResponse;
import com.haulmont.bankfrontend.dto.responses.ClientResponse;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import java.util.stream.Collectors;

public class ClientForm extends FormLayout {

    private ClientResponse client;

    private Button save;
    private Button delete;
    private Button close;

    private TextField telephoneNumber;
    private EmailField email;
    private TextField passport;
    private ComboBox<String> bank;

    Binder<ClientResponse> binder = new Binder<>(ClientResponse.class);

    public ClientForm(List<BankResponse> banks) {

        addClassName("client-form");
        configureFields(banks);
        configureBinder();

        add(
                telephoneNumber,
                email,
                passport,
                bank,
                createButtonLayout());
    }

    private void configureBinder() {
        binder.forField(telephoneNumber)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter("Not a number"))
                .bind(ClientResponse::getTelephoneNumber, ClientResponse::setTelephoneNumber);
        binder.forField(passport)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter("Not a number"))
                .bind(ClientResponse::getPassportNumber, ClientResponse::setPassportNumber);
        binder.forField(bank).bind(ClientResponse::getBankId, ClientResponse::setBankId);
        binder.bindInstanceFields(this);

    }

    public void setClient(ClientResponse client) {
        this.client = client;
        binder.readBean(client);
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, client)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void configureFields(List<BankResponse> banks) {

        save = new Button("Save");
        delete = new Button("Delete");
        close = new Button("Cancel");
        telephoneNumber = new TextField("Telephone Number");
        email = new EmailField("Email");
        passport = new TextField("Passport Number");
        bank = new ComboBox<>("Bank");

        List<String> uuids = banks.stream().map(BankResponse::getId).collect(Collectors.toList());
        bank.setItems(uuids);

    }

    private void validateAndSave() {
        try {
            binder.writeBean(client);
            fireEvent(new SaveEvent(this, client));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class ClientFormEvent extends ComponentEvent<ClientForm> {
        private ClientResponse client;

        protected ClientFormEvent(ClientForm source, ClientResponse client) {

            super(source, false);
            this.client = client;
        }

        public ClientResponse getClient() {
            return client;
        }
    }

    public static class SaveEvent extends ClientFormEvent {
        SaveEvent(ClientForm source, ClientResponse client) {
            super(source, client);
        }
    }

    public static class DeleteEvent extends ClientFormEvent {
        DeleteEvent(ClientForm source, ClientResponse client) {
            super(source, client);
        }

    }

    public static class CloseEvent extends ClientFormEvent {
        CloseEvent(ClientForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {


        return getEventBus().addListener(eventType, listener);
    }
}
