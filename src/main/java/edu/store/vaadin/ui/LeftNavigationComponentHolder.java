package edu.store.vaadin.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import java.io.Serializable;
import org.vaadin.firitin.components.accordion.VAccordionPanel;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

public class LeftNavigationComponentHolder implements Serializable {

    /** */
    private static final long serialVersionUID = -8182008500541381936L;

    private Class<? extends Component> navigationTarget;
    private Component contentComponent;
    private VButton actionButton;
    private Image actionImage;
    private VAccordionPanel accordionPanel;
    private VVerticalLayout layout;

    public LeftNavigationComponentHolder() {}

    public Class<? extends Component> getNavigationTarget() {
        return navigationTarget;
    }

    public void setNavigationTarget(Class<? extends Component> navigationTarget) {
        this.navigationTarget = navigationTarget;
    }

    public Component getContentComponent() {
        return contentComponent;
    }

    public void setContentComponent(Component conentComponent) {
        this.contentComponent = conentComponent;
    }

    public VButton getActionButton() {
        return actionButton;
    }

    public void setActionButton(VButton actionButton) {
        this.actionButton = actionButton;
    }

    public VAccordionPanel getAccordionPanel() {
        return accordionPanel;
    }

    public void setAccordionPanel(VAccordionPanel accordionPanel) {
        this.accordionPanel = accordionPanel;
    }

    public VVerticalLayout getLayout() {
        return layout;
    }

    public void setLayout(VVerticalLayout layout) {
        this.layout = layout;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accordionPanel == null) ? 0 : accordionPanel.hashCode());
        result = prime * result + ((actionButton == null) ? 0 : actionButton.hashCode());
        result = prime * result + ((contentComponent == null) ? 0 : contentComponent.hashCode());
        result = prime * result + ((layout == null) ? 0 : layout.hashCode());
        result = prime * result + ((navigationTarget == null) ? 0 : navigationTarget.hashCode());
        return result;
    }

    public Image getActionImage() {
        return actionImage;
    }

    public void setActionImage(Image actionImage) {
        this.actionImage = actionImage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LeftNavigationComponentHolder other = (LeftNavigationComponentHolder) obj;
        if (accordionPanel == null) {
            if (other.accordionPanel != null) return false;
        } else if (!accordionPanel.equals(other.accordionPanel)) return false;
        if (actionButton == null) {
            if (other.actionButton != null) return false;
        } else if (!actionButton.equals(other.actionButton)) return false;
        if (contentComponent == null) {
            if (other.contentComponent != null) return false;
        } else if (!contentComponent.equals(other.contentComponent)) return false;
        if (layout == null) {
            if (other.layout != null) return false;
        } else if (!layout.equals(other.layout)) return false;
        if (navigationTarget == null) {
            return other.navigationTarget == null;
        } else return navigationTarget.equals(other.navigationTarget);
    }

    @Override
    public String toString() {
        return (
            "LeftNavigationComponentHolder [navigationTarget=" +
            navigationTarget +
            ", contentComponent=" +
            contentComponent +
            ", actionButton=" +
            actionButton +
            ", accordionPanel=" +
            accordionPanel +
            ", layout=" +
            layout +
            "]"
        );
    }
}
