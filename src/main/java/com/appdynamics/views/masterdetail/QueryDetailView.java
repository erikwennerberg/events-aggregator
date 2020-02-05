package com.appdynamics.views.masterdetail;

import com.vaadin.flow.component.ClickEvent;
import org.springframework.beans.factory.annotation.Autowired;

import com.appdynamics.backend.BackendService;
import com.appdynamics.backend.Employee;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.appdynamics.MainView;
@Route(value = "Master-Detail", layout = MainView.class)
@PageTitle("Master Detail")
@CssImport("styles/views/masterdetail/master-detail-view.css")
public class QueryDetailView extends Div implements AfterNavigationObserver {

    @Autowired
    private BackendService service;

    private Grid<Employee> employees;

    private TextField index1 = new TextField();
    private TextField index2 = new TextField();
    private TextField key = new TextField();
    private TextField query = new TextField();

    private Button run = new Button("Run");

    private Binder<Employee> binder;

    public QueryDetailView() {
        setId("master-detail-view");

        Div editorDiv = new Div();
        editorDiv.setId("editor-layout");
        FormLayout formLayout = new FormLayout();
        addFormItem(editorDiv, formLayout, index1, "Index1");
        addFormItem(editorDiv, formLayout, index2, "Index2");
        addFormItem(editorDiv, formLayout, key, "Joining Key");
        addFormItem(editorDiv, formLayout, query, "ADQL Query");




        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        run.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        run.addClickListener(this::runQuery);
        buttonLayout.add(run);


        formLayout.add(buttonLayout);

        add(formLayout);



    }

//    private void createButtonLayout(Div editorDiv) {
//        HorizontalLayout buttonLayout = new HorizontalLayout();
//        buttonLayout.setId("button-layout");
//        buttonLayout.setWidthFull();
//        buttonLayout.setSpacing(true);
//        run.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        buttonLayout.add(run);
//
//        editorDiv.add(buttonLayout);
//    }
    public void runQuery(ClickEvent event){
        service.doAggregation(index1.getValue(), index2.getValue(), key.getValue(), query.getValue());
    }

    private void addFormItem(Div wrapper, FormLayout formLayout,
            AbstractField field, String fieldName) {
        formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        field.getElement().getClassList().add("full-width");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Lazy init of the grid items, happens only when we are sure the view will be
        // shown to the user
        //employees.setItems(service.getEmployees());
    }

}
