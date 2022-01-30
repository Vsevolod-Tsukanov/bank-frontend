package com.haulmont.bankfrontend.forms;

import com.haulmont.bankfrontend.dto.responses.ClientResponse;
import com.haulmont.bankfrontend.dto.responses.CreditDetailsResponse;
import com.haulmont.bankfrontend.dto.responses.CreditOfferResponse;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import java.util.stream.Collectors;

public class CreditOfferForm extends FormLayout {

    private CreditOfferResponse creditOffer;

    private Button save;
    private Button delete;
    private Button close;

    private ComboBox<String> clientId;
    private ComboBox<String> creditId;
    private BigDecimalField sumOfCredit;
    private TextField monthsOfCredit;
    private TextField paymentScheduleId;

    Binder<CreditOfferResponse> binder = new Binder<>(CreditOfferResponse.class);


    public CreditOfferForm(List<ClientResponse> clients, List<CreditDetailsResponse> details) {

        addClassName("client-form");
        configureFields(clients, details);
        configureBinder();

        add(
                clientId,
                creditId,
                sumOfCredit,
                monthsOfCredit,
                paymentScheduleId,
                createButtonLayout());
    }

    private void configureBinder() {
        binder.forField(monthsOfCredit)
                .withNullRepresentation("")
                .withConverter(new StringToIntegerConverter("Not a number"))
                .bind(CreditOfferResponse::getMonthsOfCredit, CreditOfferResponse::setMonthsOfCredit);
        binder.forField(clientId).bind(CreditOfferResponse::getClientId, CreditOfferResponse::setClientId);
        binder.forField(creditId).bind(CreditOfferResponse::getCreditId, CreditOfferResponse::setCreditId);
        binder.bindInstanceFields(this);

    }

    public void setCreditOffer(CreditOfferResponse creditOffer) {
        this.creditOffer = creditOffer;
        binder.readBean(creditOffer);
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, creditOffer)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void configureFields(List<ClientResponse> clients, List<CreditDetailsResponse> details) {

        save = new Button("Save");
        delete = new Button("Delete");
        close = new Button("Cancel");

        clientId = new ComboBox<>("Client ID");
        creditId = new ComboBox<>("Credit ID");
        sumOfCredit = new BigDecimalField("Sum Of Credit");
        monthsOfCredit = new TextField("Month Of Credit");
        paymentScheduleId = new TextField("Payment Schedule ID");
        paymentScheduleId.setReadOnly(true);

        List<String> clientsIds = clients.stream().map(ClientResponse::getId).collect(Collectors.toList());
        List<String> creditIds = details.stream().map(CreditDetailsResponse::getId).collect(Collectors.toList());
        clientId.setItems(clientsIds);
        creditId.setItems(creditIds);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(creditOffer);
            fireEvent(new SaveEvent(this, creditOffer));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class CreditOfferFormEvent extends ComponentEvent<CreditOfferForm> {
        private CreditOfferResponse creditOffer;

        protected CreditOfferFormEvent(CreditOfferForm source, CreditOfferResponse creditOffer) {

            super(source, false);
            this.creditOffer = creditOffer;
        }

        public CreditOfferResponse getCreditOffer() {
            return creditOffer;
        }
    }

    public static class SaveEvent extends CreditOfferForm.CreditOfferFormEvent {
        SaveEvent(CreditOfferForm source, CreditOfferResponse creditOffer) {
            super(source, creditOffer);
        }
    }

    public static class DeleteEvent extends CreditOfferForm.CreditOfferFormEvent {
        DeleteEvent(CreditOfferForm source, CreditOfferResponse creditOffer) {
            super(source, creditOffer);
        }

    }

    public static class CloseEvent extends CreditOfferForm.CreditOfferFormEvent {
        CloseEvent(CreditOfferForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {


        return getEventBus().addListener(eventType, listener);
    }
}
