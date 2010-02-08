package org.rhq.enterprise.gui.inventory.browse;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.model.DataModel;

import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.criteria.ResourceCriteria;
import org.rhq.core.domain.resource.Resource;
import org.rhq.core.domain.util.PageControl;
import org.rhq.core.domain.util.PageList;
import org.rhq.core.gui.util.FacesContextUtility;
import org.rhq.core.gui.util.StringUtility;
import org.rhq.enterprise.gui.common.framework.PagedDataTableUIBean;
import org.rhq.enterprise.gui.common.paging.PageControlView;
import org.rhq.enterprise.gui.common.paging.PagedListDataModel;
import org.rhq.enterprise.gui.util.EnterpriseFacesContextUtility;
import org.rhq.enterprise.server.resource.ResourceManagerLocal;
import org.rhq.enterprise.server.util.LookupUtil;

public class BrowseResourcesUIBean extends PagedDataTableUIBean {
    public static final String MANAGED_BEAN_NAME = "BrowseResourcesUIBean";
    private static final String FORM_PREFIX = "browseResourcesForm:";

    private String filter;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public DataModel getDataModel() {
        if (dataModel == null) {
            dataModel = new ResultsDataModel(PageControlView.BrowseResources, MANAGED_BEAN_NAME);
        }
        return dataModel;
    }

    private ResourceManagerLocal resourceManager = LookupUtil.getResourceManager();

    private class ResultsDataModel extends PagedListDataModel<Resource> {

        public ResultsDataModel(PageControlView view, String beanName) {
            super(view, beanName);
        }

        public PageList<Resource> fetchPage(PageControl pc) {
            getDataFromRequest();
            String filter = getFilter();

            PageList<Resource> results;

            ResourceCriteria criteria = new ResourceCriteria();
            criteria.setPageControl(pc);
            if (filter != null && !filter.equals("")) {
                criteria.addFilterName(filter);
            }
            criteria.fetchParentResource(true);

            results = resourceManager.findResourcesByCriteria(getSubject(), criteria);
            return results;
        }

        private void getDataFromRequest() {
            String filter = FacesContextUtility.getOptionalRequestParameter(FORM_PREFIX + "filter");
            BrowseResourcesUIBean.this.filter = filter;
        }
    }

    public String uninventorySelectedResources() {
        try {
            Subject subject = EnterpriseFacesContextUtility.getSubject();

            String[] selectedResources = getSelectedItems();
            int[] resourceIds = StringUtility.getIntArray(selectedResources);

            resourceManager.uninventoryResources(subject, resourceIds);
            FacesContextUtility.addMessage(FacesMessage.SEVERITY_INFO, "Uninventoried selected resources");
        } catch (Exception e) {
            FacesContextUtility.addMessage(FacesMessage.SEVERITY_ERROR, "Failed to uninventory selected resources", e);
        }

        return "success";
    }

    private String[] getSelectedItems() {
        return FacesContextUtility.getRequest().getParameterValues("selectedItems");
    }

    public class Suggestion {
        String label;
        String value;

        public Suggestion(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value;
        }
    }

    public List<Suggestion> autocomplete(Object suggest) {
        String currentInputText = (String) suggest;
        List<Suggestion> results = new ArrayList<Suggestion>();
        // offer suggestions based on current currentInputText
        return results;
    }

}
