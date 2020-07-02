package br.com.cauezito.util;

import java.util.Iterator;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;

public class ClearComponents {

	public static void cleanSubmittedValues(UIComponent component) {
        if (component instanceof EditableValueHolder) {
            EditableValueHolder evh = (EditableValueHolder) component;
            evh.resetValue();
        }
        // Dependendo de como se implementa um Composite Component, ele retorna
        // ZERO
        // na busca por filhos. Nesse caso devemos iterar sobre os componentes
        // que o
        // compõe de forma diferente.
        if (UIComponent.isCompositeComponent(component)) {
            Iterator<UIComponent> i = component.getFacetsAndChildren();
            while (i.hasNext()) {
                UIComponent comp = (UIComponent) i.next();

                if (comp.getChildCount() > 0) {
                    for (UIComponent child : comp.getChildren()) {
                        cleanSubmittedValues(child);
                    }
                }
            }
        }

        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                cleanSubmittedValues(child);
            }
        }
    }
}
