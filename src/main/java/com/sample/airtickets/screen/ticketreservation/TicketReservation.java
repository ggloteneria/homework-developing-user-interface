package com.sample.airtickets.screen.ticketreservation;

import com.sample.airtickets.app.TicketService;
import com.sample.airtickets.entity.Airport;
import com.sample.airtickets.entity.Flight;
import com.sample.airtickets.entity.Ticket;
import com.sample.airtickets.screen.ticket.TicketBrowse;
import io.jmix.core.DataManager;
import io.jmix.ui.*;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.app.inputdialog.DialogActions;
import io.jmix.ui.app.inputdialog.DialogOutcome;
import io.jmix.ui.app.inputdialog.InputDialog;
import io.jmix.ui.app.inputdialog.InputParameter;
import io.jmix.ui.component.*;
import io.jmix.ui.executor.BackgroundTask;
import io.jmix.ui.executor.BackgroundWorker;
import io.jmix.ui.executor.TaskLifeCycle;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@UiController("TicketReservation")
@UiDescriptor("ticket-reservation.xml")
public class TicketReservation extends Screen {
    @Autowired
    protected DataManager dataManager;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private EntityComboBox<Airport> filterFrom;
    @Autowired
    private EntityComboBox<Airport> filterTo;
    @Autowired
    private DateField<OffsetDateTime> filterDate;
    @Autowired
    private CollectionContainer<Flight> flightsDc;
    @Autowired
    private Notifications notifications;
    @Autowired
    private ProgressBar progressBar;
    @Autowired
    private BackgroundWorker backgroundWorker;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private GroupTable<Flight> flightsTable;


    @Subscribe("search")
    public void onSearchClick(Button.ClickEvent event) {
        if (filterFrom.getValue() == null && filterTo.getValue() == null) {
            notifications.create()
                    .withCaption("Please fill at least one filter field")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }
        BackgroundTask<Integer, Void> backgroundTask = new SearchTask();
        progressBar.setVisible(true);

        backgroundWorker.handle(backgroundTask).execute();
    }


    @Install(to = "flightsTable.reserve", subject = "columnGenerator")
    private Component flightsTableReserveColumnGenerator(Flight flight) {
        LinkButton linkButton = uiComponents.create(LinkButton.class);
        linkButton.setCaption("Reserve");
        linkButton.setAction(new BaseAction("reserve") {
            @Override
            public void actionPerform(Component component) {
                dialogs.createInputDialog(TicketReservation.this)
                        .withCaption("Get values")
                        .withParameters(
                                InputParameter.stringParameter("passengerName")
                                        .withCaption("Passenger name"),
                                InputParameter.stringParameter("passportNumber")
                                        .withCaption("Passport number"),
                                InputParameter.stringParameter("telephone")
                                        .withCaption("Telephone")
                        )
                        .withActions(DialogActions.OK_CANCEL)
                        .withCloseListener(closeEvent -> {
                            if (closeEvent.closedWith(DialogOutcome.OK)) {
                                String passengerName = closeEvent.getValue("passengerName");
                                String passportNumber = closeEvent.getValue("passportNumber");
                                String telephone = closeEvent.getValue("telephone");

                                Ticket ticket = dataManager.create(Ticket.class);
                                ticket.setPassengerName(passengerName);
                                ticket.setPassportNumber(passportNumber);
                                ticket.setTelephone(telephone);
                                ticket.setFlight(flight);

                                ticketService.saveTicket(ticket);

                                screenBuilders.screen(TicketReservation.this)
                                        .withScreenClass(TicketBrowse.class)
                                        .withOpenMode(OpenMode.NEW_TAB)
                                        .show();

                            }
                        }).show();
            }
        });
        return linkButton;
    }

    @Subscribe("inputDialog")
    public void onInputDialogInputDialogClose(InputDialog.InputDialogCloseEvent closeEvent) {
        if (closeEvent.closedWith(DialogOutcome.OK)) {
            String passengerName = closeEvent.getValue("passengerName");
            String passportNumber = closeEvent.getValue("passportNumber");
            String telephone = closeEvent.getValue("telephone");

            Ticket ticket = dataManager.create(Ticket.class);
            ticket.setPassengerName(passengerName);
            ticket.setPassportNumber(passportNumber);
            ticket.setTelephone(telephone);
            ticket.setFlight(flightsTable.getSingleSelected());

            ticketService.saveTicket(ticket);

            screenBuilders.screen(TicketReservation.this)
                    .withScreenClass(TicketBrowse.class)
                    .withOpenMode(OpenMode.NEW_TAB)
                    .show();
        }
    }


    private class SearchTask extends BackgroundTask<Integer, Void> {

        private List<Flight> flightList;

        protected SearchTask() {
            super(10, TimeUnit.MINUTES, TicketReservation.this);
        }

        @Override
        public Void run(TaskLifeCycle<Integer> taskLifeCycle) {

            flightList = ticketService.searchFlights(filterFrom.getValue(), filterTo.getValue(), Objects.requireNonNull(filterDate.getValue()).toLocalDate());

            return null;
        }

        @Override
        public void done(Void result) {
            flightsDc.setItems(flightList);
            progressBar.setVisible(false);
        }
    }
}
