package com.haulmont.bankfrontend.layouts;

import com.haulmont.bankfrontend.views.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {


    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Haulmont Bank");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);


        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);


    }

    private void createDrawer() {
        RouterLink banksLink = new RouterLink("Banks", BankView.class);
        RouterLink clientLink = new RouterLink("Clients", ClientView.class);
        RouterLink creditDetailsLink = new RouterLink("Credit Details", CreditDetailsView.class);
        RouterLink creditOfferLink = new RouterLink("Credit Offers", CreditOfferView.class);
        RouterLink scheduleLink = new RouterLink("Payment Schedule", PaymentScheduleView.class);


        banksLink.setHighlightCondition(HighlightConditions.sameLocation());
        clientLink.setHighlightCondition(HighlightConditions.sameLocation());
        creditDetailsLink.setHighlightCondition(HighlightConditions.sameLocation());
        creditOfferLink.setHighlightCondition(HighlightConditions.sameLocation());
        scheduleLink.setHighlightCondition(HighlightConditions.sameLocation());


        addToDrawer(new VerticalLayout(
                banksLink,
                clientLink,
                creditDetailsLink,
                creditOfferLink,
                scheduleLink
        ));
    }
}
