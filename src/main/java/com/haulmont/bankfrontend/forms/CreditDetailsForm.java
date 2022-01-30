package com.haulmont.bankfrontend.forms;

import com.haulmont.bankfrontend.dto.responses.BankResponse;
import com.haulmont.bankfrontend.dto.responses.CreditDetailsResponse;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import java.util.stream.Collectors;


public class CreditDetailsForm extends FormLayout {

    private CreditDetailsResponse creditDetails;

    private Button save;
    private Button delete;
    private Button close;

    private BigDecimalField creditLimit;
    private BigDecimalField creditPercent;
    private ComboBox<String> bank;

    Binder<CreditDetailsResponse> binder = new Binder<>(CreditDetailsResponse.class);

    public CreditDetailsForm(List<BankResponse> banks) {

        addClassName("credit-details-form");
        configureFields(banks);
        configureBinder();

        add(
                creditLimit,
                creditPercent,
                bank,
                createButtonLayout());
    }


    private void configureBinder() {
        binder.forField(bank).bind(CreditDetailsResponse::getBankId, CreditDetailsResponse::setBankId);
        binder.bindInstanceFields(this);
    }

    public void setCreditDetails(CreditDetailsResponse creditDetails) {
        this.creditDetails = creditDetails;
        binder.readBean(creditDetails);
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, creditDetails)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void configureFields(List<BankResponse> banks) {

        save = new Button("Save");
        delete = new Button("Delete");
        close = new Button("Cancel");
        creditLimit = new BigDecimalField("Credit Limit");
        creditPercent = new BigDecimalField("Credit Percent");
        bank = new ComboBox<>("Bank");

        List<String> uuids = banks.stream().map(BankResponse::getId).collect(Collectors.toList());
        bank.setItems(uuids);

    }

    private void validateAndSave() {
        try {
            binder.writeBean(creditDetails);
            fireEvent(new SaveEvent(this, creditDetails));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    // Events
    public static abstract class CreditDetailsFormEvent extends ComponentEvent<CreditDetailsForm> {
        private CreditDetailsResponse creditDetails;

        protected CreditDetailsFormEvent(CreditDetailsForm source, CreditDetailsResponse creditDetails) {

            super(source, false);
            this.creditDetails = creditDetails;
        }

        public CreditDetailsResponse getCreditDetails() {
            return creditDetails;
        }
    }

    public static class SaveEvent extends CreditDetailsForm.CreditDetailsFormEvent {
        SaveEvent(CreditDetailsForm source, CreditDetailsResponse creditDetails) {
            super(source, creditDetails);
        }
    }

    public static class DeleteEvent extends CreditDetailsForm.CreditDetailsFormEvent {
        DeleteEvent(CreditDetailsForm source, CreditDetailsResponse creditDetails) {
            super(source, creditDetails);
        }

    }

    public static class CloseEvent extends CreditDetailsForm.CreditDetailsFormEvent {
        CloseEvent(CreditDetailsForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {


        return getEventBus().addListener(eventType, listener);
    }
}


