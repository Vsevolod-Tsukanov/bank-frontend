package com.haulmont.bankfrontend.forms;

import com.haulmont.bankfrontend.dto.responses.BankResponse;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;


public class BankForm extends FormLayout {

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    private Binder<BankResponse> binder = new Binder<>(BankResponse.class);
    private BankResponse bank;

    public BankForm() {
        addClassName("bank-form");
        add(createButtonLayout());
    }


    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new BankForm.DeleteEvent(this, bank)));
        close.addClickListener(event -> fireEvent(new BankForm.CloseEvent(this)));


        return new HorizontalLayout(save, delete, close);
    }

    public void setBank(BankResponse bank) {
        this.bank = bank;
        binder.readBean(bank);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(bank);
            fireEvent(new SaveEvent(this, bank));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class BankFormEvent extends ComponentEvent<BankForm> {
        private BankResponse bank;

        protected BankFormEvent(BankForm source, BankResponse bank) {

            super(source, false);
            this.bank = bank;
        }

        public BankResponse getBank() {
            return bank;
        }
    }

    public static class SaveEvent extends BankForm.BankFormEvent {
        SaveEvent(BankForm source, BankResponse bank) {
            super(source, bank);
        }
    }

    public static class DeleteEvent extends BankForm.BankFormEvent {
        DeleteEvent(BankForm source, BankResponse bank) {
            super(source, bank);
        }

    }

    public static class CloseEvent extends BankForm.BankFormEvent {
        CloseEvent(BankForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {


        return getEventBus().addListener(eventType, listener);
    }
}
